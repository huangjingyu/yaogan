<%@ page contentType="text/html; charset=utf-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/common/head.jsp"%>
<style type="text/css">
#placesDiv div {
	margin-left: 5px;
}
</style>
<script type="text/javascript">
	function placeCompare() {
		dojo.empty("chartNode");
		var time = dijit.byId("time").attr("value");
		var placeIdArr = [];
		require([ "dojo/query", "dojo/NodeList-dom" ], function(query) {
			query("input:checked", "placesDiv").forEach(function(node) {
				var w = dijit.getEnclosingWidget(node);
				placeIdArr.push(w.value);
			});
		});
		var placeIds = placeIdArr.join(",");
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
		var apiUrl = "${ctx}/api/envstats/time/" + time + "/places/" + placeIds
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

	function showAvaPlaces(time) {
		var placesDiv = dojo.byId("placesDiv");
		require([ "dojo/_base/array" ], function(arrayUtil) {
			arrayUtil.forEach(dijit.findWidgets(placesDiv), function(w) {
				w.destroy();
			});
		});
		dojo.empty(placesDiv);
		if (!time)
			return;
		require([ "dojo/_base/xhr" ], function(xhr) {
			xhr.get({
				url : "${ctx}/api/places.json?time=" + time,
				handleAs : "json",
				load : function(places) {
					require([ "dojo/_base/array" ], function(arrayUtil) {
						arrayUtil.forEach(places, function(place, i) {
							require([ "dijit/form/CheckBox" ], function(
									CheckBox) {
								var chkId = "place" + i;
								var checkBox = new CheckBox({
									id : chkId,
									name : "placeId",
									value : place['id'],
									checked : false
								}, "checkBox");
								var chkLabel = dojo.create("label", {
									"for" : chkId,
									innerHTML : place.name
								});
								dojo.attr(chkLabel, "class",
										"dijitFocusedLabel");
								checkBox.placeAt("placesDiv");
								dojo.place(chkLabel, "placesDiv");
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
					<li><a href="timeCompare" class="">时间分析</a>
					</li>
					<li><a href="placeCompare" class="current">空间分析</a>
					</li>
				</ul>
				<div data-dojo-type="dojox.layout.TableContainer"
					data-dojo-props="cols:1" id="tc1">
					<select id="time" name="time" data-dojo-type="dijit.form.Select"
						title="请选择时间:" onchange="showAvaPlaces(this.value)">
						<option value="">请选择</option>
						<c:forEach var="time" items="${times}">
							<option value="${time}">${time}</option>
						</c:forEach>
					</select>
					<div id="placesDiv" data-dojo-type="dijit.layout.ContentPane"
						title="请选择矿区:"></div>
				</div>
				<div>
					<button data-dojo-type="dijit.form.Button" type="button">
						分析
						<script type="dojo/on" data-dojo-event="click"
							data-dojo-args="evt">placeCompare();</script>
					</button>
				</div>
				<div id="chartNode" style="width: 700px; height: 480px; margin-top: 10px;"></div>
			</div>
		</div>
	</div>
</body>
</html>
