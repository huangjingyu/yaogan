<%@ page contentType="text/html; charset=utf-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/common/head.jsp"%>
<script type="text/javascript">
	function timeCompare() {
		var placeId = "1";
		var times = "2010,2011";
		var apiUrl = "${ctx}/api/envstats/place/" + placeId + "/times/" + times;
		require([ "dojo/_base/xhr", "dojo/dom", "dojo/_base/array",
				"dojo/domReady!" ], function(xhr, dom, arrayUtil) {
			xhr.get({
				url : apiUrl,
				handleAs : "json",
				load : function(jsonData) {
					var chartData = new Array();
					var axisLabels = new Array();
					var showMinorTick = true;
					arrayUtil.forEach(jsonData, function(pair, i) {
						if (i == 0) {
							if (instanceUsage.total <= 1) {
								showMinorTick = false;
							}
						}
						var ind = i + 1;
						var dataObj = {};
						dataObj.x = ind;
						dataObj.y = instanceUsage.total;
						dataObj.color = colorArr[i % 3];
						chartData.push(dataObj);

						var labelObj = {};
						labelObj.value = ind;
						labelObj.text = instanceUsage.typeName;
						axisLabels.push(labelObj);
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
					"dojox/charting/plot2d/Columns",

					//	We want to use Markers
					"dojox/charting/plot2d/Markers",

					//	We'll use default x/y axes
					"dojox/charting/axis2d/Default",

					// Wait until the DOM is ready
					"dojo/domReady!" ],
							function(Chart, theme, Tooltip, Magnify) {

								// Create the chart within it's "holding" node
								var chart = new Chart("instanceUsageNode");

								// Set the theme
								chart.setTheme(theme);

								// Add the only/default plot 
								chart.addPlot("default", {
									type : "Columns",
									markers : true,
									gap : 20
								});

								// Add axes
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
										if (value == 0) {
											return "主机数量";
										}
										return value;
									}
								});

								// Add the series of data
								chart.addSeries("主机使用人数", chartData);

								var tip = new Tooltip(chart, "default");
								var mag = new Magnify(chart, "default");

								// Render the chart!
								chart.render();
							});

				},
				error : function() {
				}
			});
		});
	}
</script>
</head>
<body class="claro">
	<div id="chartNode" style="width: 700px; height: 480px;"></div>
</body>
</html>
