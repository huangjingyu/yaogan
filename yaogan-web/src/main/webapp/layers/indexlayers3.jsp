<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/js/openlayers/theme/default/style.css"/>
        <style type="text/css">
        <%--地图框样式--%>
            #map {
                clear: both;
                position: relative;
                width: 698px;
                height: 512px;
                border: 1px solid black;
            }
            #left {
               float : left
            }
        </style>  
       <%--openlayers脚本地址 开发时可以用lib目录 发布时需要使用压缩版 --%>     
   <script src='<%=request.getContextPath()%>/static/js/openlayers/lib/OpenLayers.js'></script>
   <script src='<%=request.getContextPath()%>/static/js/jquery-1.7.1.js'></script>  
   
    <script type="text/javascript">
    function(){
        <%--全局命名空间R--%>
        var R = window.R = {
                <%--底层baselayer的wms地址--%>
                BASE_WMSURL : "http://www.rockloudtest.com/geoserver/wms",
                <%--底层的layer名称--%>
                BASE_LAYER : "shanxi",
                <%--边界值 目前为一个省的边界值--%>
                BOUNDS : new OpenLayers.Bounds(
                        37428758.96201212, 3828315.2563775433,
                        37826351.11229358, 4517940.277902247
                  ),
                  <%--坐标系--%>
                SRS : "EPSG:2413"
        };
        
        <%--Layers为地图层的大对象--%>
        R.Layers = function(){
        <%--this引用，用于传值--%>
        var _self = this;
        <%--图片格式  各种格式的优缺点未具体调研--%>
        this.format = 'image/png';
        <%--创建地图  --%>
        this.createMap = function() {
        	  <%--创建一个可以包含多个层的地图--%>
           var map = this.map = new OpenLayers.Map('map');
        	  
        };
        <%--添加一个底层 目前为30m地图--%>
        this.addBaseLayer = function() {
            var baseLayer = this.baseLayer =  new OpenLayers.Layer.WMS(
            		<%--name,url,param,options--%>
                    "base layer", R.BASE_WMSURL,
                    {
                        LAYERS: R.BASE_LAYER,
                        STYLES: '',
                        format: this.format
                    },
                    {
                        buffer: 0,
                        displayOutsideMaxExtent: true,
                        isBaseLayer: true,
                        yx : {R.SRS : true}
                    } 
                );  
        	    this.map.addLayers([baseLayer]);
        };
        <%--添加控件--%>
        this.addControl = function() {
        	
        	
        }
        };
        R.layerMap = new R.Layers();
        <%--页面装载完毕后执行--%>
        R.layerMap.onLoad = function() {
           this.createMap();
           this.addBaseLayer();
        }
    }();
    </script>
   </head>
   <body onload="R.layerMap.onLoad()">
   
   </body>
</html>