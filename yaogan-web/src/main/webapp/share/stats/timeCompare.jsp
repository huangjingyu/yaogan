<%@ page contentType="text/html; charset=utf-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/common/head.jsp"%>
<style type="text/css">
#timesDiv div {
	margin-left: 5px;
}
</style>
<script type="text/javascript">
	function timeCompare() {
		var colorArr = [ "#d14b4c", "#784ca5", "#2a6fae", "#72b238", "#d4d42f",
				"#ffa520", "#a4784b" ];
		var colorNum = colorArr.length;
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
							dataObj.color = colorArr[i % colorNum];
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
	require([ "dijit/form/Select", "dijit/form/Button",
			"dojox/layout/TableContainer", "dijit/layout/ContentPane" ]);
</script>
</head>
<body class="claro">
	<div id="middle2">
		<div class="rtlist">
			<div class="middle_tab">
				<ul class="tabs">
					<li><a href="timeCompare" class="current">时间分析</a></li>
					<li><a href="placeCompare" class="">空间分析</a></li>
				</ul>
				<div class="pane" style="display: block; height: 800px;">
					<form id="form1" name="form1" method="post" action="">
						<table width="700px" border="0" cellspacing="0" cellpadding="0"
							id="table_two">
							<tbody>
								<tr>
									<td>选择区域：</td>
									<td><select id="placeId" name="placeId"
										data-dojo-type="dijit.form.Select" title="请选择区域:"
										onchange="showAvaTimes(this.value)">
											<option value="">请选择</option>
											<c:forEach var="place" items="${places}">
												<option value="${place.id}">${place.name}</option>
											</c:forEach>
									</select></td>
									<td width="14%" rowspan="2"><a href="#"
										onclick="timeCompare()"><img
											src="${ctx}/static/img/butt_fx.gif" width="50" height="24"></a></td>
								</tr>
								<tr>
									<td width="16%">请选择时间：</td>
									<td><div id="timesDiv"
											data-dojo-type="dijit.layout.ContentPane" title="请选择时间:"></div></td>
								</tr>
							</tbody>
						</table>
					</form>
					<div class="pane_pic" id="chartNode"
						style="width: 800px; height: 500px;"></div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
