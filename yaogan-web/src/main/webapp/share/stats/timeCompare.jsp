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
		var testData = [ {
			"first" : "2008",
			"second" : {
				"abio" : 2.5,
				"aveg" : 1.3,
				"aero" : 1.0
			}
		}, {
			"first" : "2009",
			"second" : {
				"abio" : 2.5,
				"aveg" : 1.3,
				"aero" : 1.0
			}
		} ];
		var placeId = "1";
		var times = "2010,2011";
		var statsKeys = [ "abio", "aveg", "aero" ];
		var axisLabels = [ {
			value : 1,
			text : "生物丰度指数"
		}, {
			value : 2,
			text : "植被覆盖指数"
		}, {
			value : 3,
			text : "土地退化指数"
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
		dojo.empty("timesDiv");
		var times = [ "2008", "2009" ];
		require([ "dojo/_base/array" ], function(arrayUtil) {
			arrayUtil.forEach(times, function(time, i) {
				require([ "dijit/form/CheckBox" ], function(CheckBox) {
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
					dojo.attr(chkLabel, "class", "dijitFocusedLabel");
					checkBox.placeAt("timesDiv");
					dojo.place(chkLabel, "timesDiv");
				});
			});
		});
	}
</script>
</head>
<body class="claro">
	<div>
		<input type="button" value="time" onclick="showAvaTimes(1)" />
	</div>
	<div id="timesDiv"></div>
	<input type="button" value="test" onclick="timeCompare()" />
	<div id="chartNode" style="width: 700px; height: 480px;"></div>
</body>
</html>
