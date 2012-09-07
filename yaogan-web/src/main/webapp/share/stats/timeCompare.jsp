<%@ page contentType="text/html; charset=utf-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/common/head.jsp"%>
<style type="text/css">
#timesDiv div {
	margin-left: 15px;
}
</style>
<script type="text/javascript">
	function timeCompare() {
		dojo.empty("chartNode");
		var placeId = dijit.byId("placeId").attr("value");
		var timeArr = [];
		require([ "dojo/query", "dojo/NodeList-dom" ], function(query) {
			query("input:checked", "timesDiv").forEach(function(node) {
				timeArr.push(node.value);
			});
		});
		var times = timeArr.join(",");
		var statsKeys = [ "abio", "aveg", "aero", "asus" ];
		var axisLabels = [ {
			value : 1,
			text : "生物丰度指数"
		}, {
			value : 2,
			text : "植被覆盖指数"
		}, {
			value : 3,
			text : "土地退化指数"
		}, {
			value : 4,
			text : "土地环境指数"
		} ];
		var apiUrl = "${ctx}/api/envstats/place/" + placeId + "/times/" + times
				+ ".json";
		require([ "dojo/_base/xhr", "dojo/dom", "dojo/_base/array",
				"dojo/domReady!" ], function(xhr, dom, arrayUtil) {
			xhr.get({
				url : apiUrl,
				handleAs : "json",
				load : function(jsonData) {
					var seriesArr = new Array();
					var showMinorTick = true;
					arrayUtil.forEach(jsonData, function(pair, i) {
						var seriesObj = {};
						seriesObj.name = pair.first;
						seriesObj.data = [];
						var envStats = pair.second;
						arrayUtil.forEach(statsKeys, function(key, ind) {
							var dataObj = {};
							dataObj.x = ind + 1;
							dataObj.y = envStats[key];
							dataObj.tooltip = pair.first + " "
									+ axisLabels[ind].text + "为"
									+ envStats[key];
							seriesObj.data.push(dataObj);
							//dataObj.color = colorArr[i % 3];
						});
						seriesArr.push(seriesObj);
					});

					require([
					// Require the basic chart class
					"dojox/charting/Chart",

					// Require the theme of our choosing
					"dojox/charting/themes/Claro",

					"dojox/charting/action2d/Tooltip",

					"dojox/charting/action2d/Magnify",

					// Charting plugins: 

					// 	We want to plot Columns 
					"dojox/charting/plot2d/ClusteredColumns",

					//	We want to use Markers
					"dojox/charting/plot2d/Markers",

					//	We'll use default x/y axes
					"dojox/charting/axis2d/Default",

					// Wait until the DOM is ready
					"dojo/domReady!" ],
							function(Chart, theme, Tooltip, Magnify) {
								var chart = new Chart("chartNode");
								chart.setTheme(theme);
								chart.addPlot("default", {
									type : "ClusteredColumns",
									markers : true,
									gap : 20
								});

								chart.addAxis("x", {
									labels : axisLabels,
									minorTicks : false
								});
								chart.addAxis("y", {
									vertical : true,
									fixLower : "major",
									fixUpper : "major",
									minorTicks : showMinorTick,
									includeZero : true,
									labelFunc : function(value) {
										return value;
									}
								});

								arrayUtil.forEach(seriesArr,
										function(seriesObj) {
											chart.addSeries(seriesObj.name,
													seriesObj.data);
										});

								var tip = new Tooltip(chart, "default");
								var mag = new Magnify(chart, "default");

								chart.render();
							});
				},
				error : function() {
				}
			});
		});
	}

	function showAvaTimes(placeId) {
		var timesDiv = dojo.byId("timesDiv");
		require([ "dojo/_base/array" ], function(arrayUtil) {
			arrayUtil.forEach(dijit.findWidgets(timesDiv), function(w) {
				w.destroy();
			});
		});
		dojo.empty(timesDiv);
		if (!placeId)
			return;
		var times = [];
		require([ "dojo/_base/xhr" ], function(xhr) {
			xhr.get({
				url : "${ctx}/api/place/" + placeId + "/availableTimes.json",
				handleAs : "json",
				load : function(times) {
					require([ "dojo/_base/array" ], function(arrayUtil) {
						arrayUtil.forEach(times, function(time, i) {
							require([ "dijit/form/CheckBox" ], function(
									CheckBox) {
								var chkId = "time" + i;
								var checkBox = new CheckBox({
									id : chkId,
									name : "time",
									value : time,
									checked : false
								}, "checkBox");
								var chkLabel = dojo.create("label", {
									"for" : chkId,
									innerHTML : time
								});
								dojo.attr(chkLabel, "class",
										"dijitFocusedLabel");
								checkBox.placeAt("timesDiv");
								dojo.place(chkLabel, "timesDiv");
							});
						});
					});
				},
				error : function() {
				}
			});
		});
	}
</script>
<script type="text/javascript">
	require([ "dijit/form/Select" ]);
	require([ "dojo/ready" ],
			function(ready) {
				ready(function() {
					require([ "dojo/parser", "dijit/form/Select",
							"dijit/form/Button" ], function(parser, Select,
							Button) {
						MySelect = Select;
						MyButton = Button;
						parser.parse();
					});
				});
			});
</script>
</head>
<body class="claro">
	<div>
		<c:if test="${currentUser.isAdmin}">
			<a href="${ctx}/admin/envstats/timeCompare">时间分析</a>&nbsp;
			<a href="${ctx}/admin/envstats/placeCompare">空间分析</a>
		</c:if>
	</div>
	<table border="0">
		<tr>
			<td width="10%" align="right">请选择区域:</td>
			<td align="left"><div id="placeDiv">
					<select id="placeId" name="placeId" data-dojo-type="MySelect"
						onchange="showAvaTimes(this.value)">
						<option value="">请选择</option>
						<c:forEach var="place" items="${places}">
							<option value="${place.id}">${place.name}</option>
						</c:forEach>
					</select>
				</div>
			</td>
		</tr>
		<tr>
			<td>请选择时间:</td>
			<td><div id="timesDiv"></div></td>
		</tr>
		<tr>
			<td><button data-dojo-type="MyButton" type="button">
					分析
					<script type="dojo/on" data-dojo-event="click" data-dojo-args="evt">timeCompare();</script>
				</button></td>
			<td></td>
		</tr>
	</table>
	<div id="chartNode" style="width: 700px; height: 480px;"></div>
</body>
</html>
