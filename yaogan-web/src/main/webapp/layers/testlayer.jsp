<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>OpenLayers map preview</title>
<!-- Import OL CSS, auto import does not work with our minified OL.js build -->
<link rel="stylesheet" type="text/css" href="http://www.rockloudtest.com:80/geoserver/openlayers/theme/default/style.css"/>
<!-- Basic CSS definitions -->
<style type="text/css">
/* General settings */
body {
font-family: Verdana, Geneva, Arial, Helvetica, sans-serif;
font-size: small;
}
/* Toolbar styles */
#toolbar {
position: relative;
padding-bottom: 0.5em;
display: none;
}
#toolbar ul {
list-style: none;
padding: 0;
margin: 0;
}
#toolbar ul li {
float: left;
padding-right: 1em;
padding-bottom: 0.5em;
}
#toolbar ul li a {
font-weight: bold;
font-size: smaller;
vertical-align: middle;
color: black;
text-decoration: none;
}
#toolbar ul li a:hover {
text-decoration: underline;
}
#toolbar ul li * {
vertical-align: middle;
}
/* The map and the location bar */
#map {
clear: both;
position: relative;
width: 360px;
height: 512px;
border: 1px solid black;
}
#location {
float: right;
}
</style>
<!-- Import OpenLayers, reduced, wms read only version -->
   <script src='<%=request.getContextPath()%>/static/js/openlayers/lib/OpenLayers.js'></script>
<script defer="defer" type="text/javascript">
var map;
var untiled;
function init(){
format = 'image/png';
var bounds = new OpenLayers.Bounds(
        110.220484, 34.582452,
        114.552878, 40.741043
     );
var options = {
maxExtent: bounds,
projection: "EPSG:4326"
};
map = new OpenLayers.Map('map', options);
/*参数*/
var param =  {LAYERS : 'shanximap',
           style : '',
           format : 'image/png'
          };
/*选项*/
var options =                      {
        singleTile: true, 
        ratio: 1, 
        isBaseLayer: true,
        yx : {'EPSG:4326' : true}
     } ; 
   /*name,url,param,options*/
var layer = new OpenLayers.Layer.WMS("tdLayer", "/geoserver/wms", param, options);
map.addLayers([layer]);
map.addControl(new OpenLayers.Control.PanZoomBar({
    position: new OpenLayers.Pixel(2, 28)
}));
/*添加比例尺*/
map.addControl(new OpenLayers.Control.Scale($('scale')));
/*添加坐标值*/
map.addControl(new OpenLayers.Control.MousePosition({element: $('location')}));
/*添加拖动鼠标的导航*/
//map.addControl(new OpenLayers.Control.Navigation());
map.zoomToExtent(bounds);
}
</script>
</head>
<body onload="init()">
<div id="map">
</div>
<div id="wrapper">
<div id="location">location</div>
<div id="scale">
</div>
</div>
</body>
</html> 