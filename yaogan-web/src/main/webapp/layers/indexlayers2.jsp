<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/js/openlayers/theme/default/style.css"/>
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
                width: 698px;
                height: 512px;
                border: 1px solid black;
            }
            
            #wrapper {
                width: 398px;
            }
            
            #location {
                float: right;
            }
            
            #options {
                position: absolute;
                left: 13px;
                top: 7px;
                z-index: 3000;
            }

            /* Styles used by the default GetFeatureInfo output, added to make IE happy */
            table.featureInfo, table.featureInfo td, table.featureInfo th {
                border: 1px solid #ddd;
                border-collapse: collapse;
                margin: 0;
                padding: 0;
                font-size: 90%;
                padding: .2em .1em;
            }
            
            table.featureInfo th {
                padding: .2em .2em;
                text-transform: uppercase;
                font-weight: bold;
                background: #eee;
            }
            
            table.featureInfo td {
                background: #fff;
            }
            
            table.featureInfo tr.odd td {
                background: #eee;
            }
            
            table.featureInfo caption {
                text-align: left;
                font-size: 100%;
                font-weight: bold;
                text-transform: uppercase;
                padding: .2em .2em;
            }
            #left {
               float : left
            }
        </style>        
   <script src='<%=request.getContextPath()%>/static/js/openlayers/lib/OpenLayers.js'></script>
   <script src='<%=request.getContextPath()%>/static/js/jquery-1.7.1.js'></script>  
    <script defer="defer" type="text/javascript">
        var R = window.R = {};
        R.Layers=new function(){  
         var _self = this;
         this.format = 'image/png';
            this.bounds = new OpenLayers.Bounds(
                  37428758.96201212, 3828315.2563775433,
                  37826351.11229358, 4517940.277902247
            );
            this.options = {
                  controls: [],
                  maxExtent: _self.bounds,
                  maxResolution: 2693.8477403308752,
                  projection: "EPSG:2413",
                  units: 'm'
            };
            /*增加省*/
            this.addProv = function(map) {
               var prov =  new OpenLayers.Layer.WMS(
                        "shanxi-Tiled", "http://www.rockloudtest.com/geoserver/wms",
                        {
                            LAYERS: 'shanxi',
                            STYLES: '',
                            format: 'image/png',
                            tiled: true,
                            tilesOrigin : this.bounds.left + ',' + this.bounds.bottom
                        },
                        {
                            buffer: 0,
                            displayOutsideMaxExtent: true,
                            isBaseLayer: true,
                            yx : {'EPSG:2413' : true}
                        } 
                    );  
               map.addLayers([prov]);  
               
            };
            /*增加矿区*/
            this.addKq = function(map) {
               this.kq = new OpenLayers.Layer.WMS(
                        "Geoserver layers - Untiled", "http://www.rockloudtest.com/geoserver/yaogan/wms",
                        {
                            LAYERS: 'yaogan:pingshuo_KQ',
                            transparent: "true",
                            STYLES: '',
                            format: 'image/png'
                        },
                        {
                           singleTile: true, 
                           ratio: 1, 
                           isBaseLayer: false,
                           yx : {'EPSG:2413' : true}
                        } 
                    );
               map.addLayers([this.kq]);  
            };
            
            this.addSwfd = function(map) {
                this.swfd = new OpenLayers.Layer.WMS(
                         "Geoserver layers - Untiled", "http://www.rockloudtest.com/geoserver/yaogan/wms",
                         {
                             LAYERS: 'yaogan:pingshuo_DL_2010',
                             transparent: "true",
                             STYLES: '',
                             format: 'image/png'
                         },
                         {
                            singleTile: true, 
                            ratio: 1, 
                            isBaseLayer: false,
                            yx : {'EPSG:2413' : true}
                         } 
                     );
                map.addLayers([this.swfd]);  
             };            
            
            this.addSelect = function(map) {
               //var renderer = OpenLayers.Util.getParameters(window.location.href).renderer;
               //renderer = (renderer) ? [renderer] : OpenLayers.Layer.Vector.prototype.renderers;
               this.select = new OpenLayers.Layer.Vector("Selection");
               map.addLayers([this.select]);
               
            }
            /*
            this.addHover = function(map) {
                this.hover = new OpenLayers.Layer.Vector("Hover");
                map.addLayers([this.hover]);
            }*/
         
            this.addControl = function(map) {
                this.control = {};
                map.addControl(new OpenLayers.Control.PanZoomBar({
                     position: new OpenLayers.Pixel(2, 15)
                 }));
                 map.addControl(new OpenLayers.Control.Scale($('scale')));
                 //map.addControl(new OpenLayers.Control.LayerSwitcher());
                 map.addControl(new OpenLayers.Control.MousePosition({element: $('location')}));
                if(R.Layers.kq) {
                  /*获取特征值控件*/
                    var control = new OpenLayers.Control.GetFeature({
                        protocol: OpenLayers.Protocol.WFS.fromWMSLayer(R.Layers.kq),
                        box: true,
                        hover: true,
                        multipleKey: "shiftKey",
                        toggleKey: "ctrlKey"
                    });
                    control.events.register("featureselected", this, function(e) {
                     R.Layers.selectFeature = e.feature;
                        R.Layers.select.addFeatures([e.feature]);
                    });
                    control.events.register("featureunselected", this, function(e) {
                        R.Layers.select.removeFeatures([e.feature]);
                    });
                    /*
                    control.events.register("hoverfeature", this, function(e) {
                     R.Layers.hover.addFeatures([e.feature]);
                    });
                    control.events.register("outfeature", this, function(e) {
                     R.Layers.hover.removeFeatures([e.feature]);
                    }); */                   
                    map.addControl(control);
                    control.activate();                   
                    }
                /*多边形控件*/ 
                this.control.polygon = new OpenLayers.Control.DrawFeature(this.select, OpenLayers.Handler.Polygon);
                map.addControl(this.control.polygon);                
                map.addControl(new OpenLayers.Control.Navigation());

            }
        }();
        
        R.init = function() {
         var map = R.Layers.map = new OpenLayers.Map('map', R.Layers.options);
         R.Layers.addProv(map);
         R.Layers.addSelect(map);
         R.Layers.addControl(map);
         map.zoomToExtent(R.Layers.bounds);
         
        }
        
        </script>
    </head>
    <body onload="R.init()">
    <div id="left">
      <div id="selectImg">
         <table>
            <tr>
               <td>
                  <select>
                     <option value ="">--选择矿区--</option>
                     <option value ="pingshuo">平朔</option>
                   </select>
               </td>
               <td>
               <select id="type">
                     <option value ="type">--选择类型--</option>
                     <option value ="kq">整体图</option>
                     <option value ="swfd">生物丰度图</option>
                     <option value ="zbfg">植被覆盖图</option>
                     <option value ="tdth">土地退化图</option>
                   </select>
               </td>
 
               <td>
               <select>
                     <option value ="">--选择日期--</option>
                     <option value ="2010">2010</option>
                     <option value ="2011">2011</option>
                </select>
               </td>   
               <td><input id="queryMap" type="button" value="地图查询"></input></td>          
            </tr>
         </table>
      </div>
       <div id="map"></div><div id="map2"></div>
       <div id="selectOp">
       <input type="radio" value="nav" checked="checked" name="op"/>导航
       <input type="radio" value="box" name="op"/>区域选择
       <input type="radio" value="pyn" name="op"/>多边形选择
       </div>
    </div><br/><br/>
     <div id="right">
      <input id="queryData" type="button" value="查询指数"/><br/>
      查询结果：<br/>
      <div id="content"></div>
     </div>
     <div id="hidden"></div>
     <script type="text/javascript">
     /*查询地图按钮选择*/
      $("#queryMap").bind("click", function() {
           R.Layers.map.destroy();
           $("#map").empty();
         var type = $("#type option:selected").val();  
         $("#hidden").load("<%=request.getContextPath()%>/layers/" + type + "layers.jsp");
      });
      /*查询结果按钮选择*/
      $("#queryData").bind("click", function(){
            $("#content").empty();
         if(!R.Layers.selectFeature) {
               $("#content").append("<div>生物丰度指数:48.1</div>");
               $("#content").append("<div>植被覆盖指数:85.3</div>");
               $("#content").append("<div>土地退化指数:79.8</div>");
            return;
         }
         var bound = R.Layers.selectFeature.geometry.toString();
         alert(bound);
            var type = $("#type option:selected").val();
         if("kq" == type) {  
          $("#content").empty();
         $("#content").append("<div>生物丰度指数:35.1</div>");
         $("#content").append("<div>植被覆盖指数:65.3</div>");
         $("#content").append("<div>土地退化指数:75.2</div>");
         } else if("swfd" == type) {
          $("#content").append("<div>生物丰度指数:35.1</div>");
         } else if("tdth" == type) {
             $("#content").append("<div>土地退化指数:75.2</div>");
         } else if("zbfg" == type) {
             $("#content").append("<div>植被覆盖指数:65.3</div>");
         }
      });
      /*导航按钮选择*/
      $("#selectOp input[value='nav']").bind("click", function() {
         R.Layers.control.polygon.deactivate();
      });
      /*多边形按钮选择*/
      $("#selectOp input[value='pyn']").bind("click", function() {
         R.Layers.control.polygon.activate();
      });
     </script>
    </body>
</html>