<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/js/openlayers/theme/default/style.css"/>
        <style type="text/css">
            /*地图div样式*/
            #map {
                clear: both;
                position: relative;
                width: 698px;
                height: 512px;
                border: 1px solid black;
            }
            /*坐标信息*/
            #location {
             float : right
            } 
            
            
            /*控制左右div水平排放*/
            #leftDiv {
               float : left
            }
            
            #kqSelect {
               list-style-type : none
            }
            #kqSelect li{
               float : left
            }            
            #mapSelect {
               list-style-type : none
            }
            #mapSelect li{
               float : left
            }            
        </style>
   <script src='<%=request.getContextPath()%>/static/js/jquery-1.7.1.js'></script>  
       <!--openlayers脚本地址 开发时可以用lib目录 发布时需要使用压缩版 -->     
   <script src='<%=request.getContextPath()%>/static/js/openlayers/lib/OpenLayers.js'></script>
   
    <script type="text/javascript">
    /*地图控制部分*/
    (function(){
        /*全局命名空间R*/
        var R = window.R = {
        		    /*选择要素的图层的名字*/
        		    SELECT_LNAME : "selectLayer",
               /*二维图层的名字*/
                TD_LNAME : "tdLayer",
                /*二维layer配置*/
                tdLayer : {
                  /*二维baselayer的wms地址*/
                  wmsUrl : "http://www.rockloudtest.com/geoserver/wms",
                  /*二维的layer名称*/
                  layerName : "shanxi"
                },
                /*矿区layer名字*/
                KQ_LNAME : "kqLayer",
                /*矿区layer配置*/
                kqLayer : {
                  wmsUrl : "http://www.rockloudtest.com/geoserver/yaogan/wms",
                  layerName : "yaogan:pingshuo_KQ"
                },
                tdlyLayer : {
                	wmsUrl : "http://www.rockloudtest.com/geoserver/yaogan/wms",
                    layerName : "yaogan:pingshuo_DL_2010"
                },
                /*边界值 目前为一个省的边界值*/
                BOUNDS : new OpenLayers.Bounds(
                        37428758.96201212, 3828315.2563775433,
                        37826351.11229358, 4517940.277902247
                  ),
                  /*坐标系*/
                SRS : "EPSG:2413",
                /*区域模式常量*/
                selectOp : {
                	NAV : "nav",
                	BOX : "box",
                	PYN : "pyn",
                	SELECT : "select",
                	RM : "rm",
                	RMALL : "rmAll"
                },
                /*矢量图层样式*/
                vectorStyle : {
                	SELECT : "select"
                }
        };
        
        /*Layers为地图层的大对象*/
        R.Layers = function(){
        /*this引用，用于传值*/
        var _self = this;
        /*图片格式  各种格式的优缺点未具体调研*/
        this.format = 'image/png';
        var map = this.map = null;
        /*创建地图  */
        this.createMap = function() {
           /*创建一个可以包含多个层的地图*/
           var options = {controls : [], maxExtent: R.BOUNDS, projection: R.SRS};
           map = this.map = new OpenLayers.Map('map', options);
           
        };
        this.layers = [];
        /*创建wms图层*/
        this.createWmsLayer = function(name, option) {
          /*参数*/
          var param =  {LAYERS : R[name].layerName,
                     style : '',
                     format : this.format,
                     /*透明度*/
                     transparent : (option && option.transparent) ? option.transparent : false
                    };
          /*选项*/
          var options =  {
        		          /*是否是基础层*/
        		          isBaseLayer : (option && option.isBaseLayer) ? option.isBaseLayer : false}; 

             /*name,url,param,options*/
          var layer = new OpenLayers.Layer.WMS(name, R[name].wmsUrl, param, options);
          return layer;    
        };
        /*创建vector图层*/
        this.createVectorLayer = function(name, options) {
        	if(! options) {
        	   return new OpenLayers.Layer.Vector(name);
        	} else {
        		return new OpenLayers.Layer.Vector(name, options);
        	}
        };
        /*添加控件*/
        this.addControls = function() {
            /*添加左边的竖状工具栏 两个参数值为坐标值*/
            map.addControl(new OpenLayers.Control.PanZoomBar({
                position: new OpenLayers.Pixel(2, 28)
            }));
            /*添加比例尺*/
            map.addControl(new OpenLayers.Control.Scale($("#scale")[0]));
            /*添加坐标值*/
            map.addControl(new OpenLayers.Control.MousePosition({element: $("#location")[0]}));
            /*添加鼠标操作的的导航*/
            this.navControl = new OpenLayers.Control.Navigation();
            map.addControl(this.navControl);
            /*区域选择*/
            this.boxControl = new OpenLayers.Control.DrawFeature(this[R.SELECT_LNAME],
                                            OpenLayers.Handler.RegularPolygon,
                                            {handlerOptions: {sides: 4, irregular: true}});
            map.addControl(this.boxControl);
            /*多边形选择*/
            this.pynControl = new OpenLayers.Control.DrawFeature(this[R.SELECT_LNAME],
                    OpenLayers.Handler.Polygon);
            map.addControl(this.pynControl);
            /*选择元素控件*/
            this.selectControl = new OpenLayers.Control.SelectFeature(
            		this[R.SELECT_LNAME],
            		{
            		clickout: false, toggle: false,
            		multiple: false, hover: false,
            		toggleKey: "ctrlKey", // ctrl key removes from selection
            		multipleKey: "shiftKey", // shift key adds to selection
            		box: false
            		}
            		);
            map.addControl(this.selectControl);
            /*多边形选择*/
        };
        /*添加获取特征值的控件 定为矿山的特征值*/
        this.addGfControl = function() {
            this.gfControl = new OpenLayers.Control.GetFeature({
                protocol: OpenLayers.Protocol.WFS.fromWMSLayer(this[R.KQ_LNAME]),
                box: false,
                hover: false,
                multipleKey: "shiftKey",
                toggleKey: "ctrlKey"
            });
            this.gfControl.events.register("featureselected", this, function(e) {
                this[R.SELECT_LNAME].addFeatures([e.feature]);
            });
            this.gfControl.events.register("featureunselected", this, function(e) {
                this[R.SELECT_LNAME].removeFeatures([e.feature]);
            });
            map.addControl(this.gfControl);
 
        };
        /*移除获取特征值的控件*/
        this.removeGfControl = function() {
        	map.removeControl(this.gfControl);
        };
        /*添加wms图层*/
        this.addWmsLayer = function(name, option) {
           var layer = this.createWmsLayer(name, option);
           this[name] = layer;
           map.addLayers([layer]);
        };
        /*删除wms图层*/
        this.removeWmsLayer=function(name) {
        	  map.removeLayer(name);
        	  this[name] = null;
        };
        this.resetLayerIdx=function() {
        	var num = map.getNumLayers();
        	map.setLayerIndex(this[R.SELECT_LNAME],num - 1);
        };
        /*添加vector图层*/
        this.addVectorLayer=function(name, option) {
           var ot = {};
           if(option && option.style == R.vectorStyle.SELECT) {
        	   ot.styleMap = new OpenLayers.Style(OpenLayers.Feature.Vector.style["select"]);
           }
           var layer = this.createVectorLayer(name, ot);
        	  this[name] = layer;
           map.addLayers([layer]);
        };
        };
        R.layerMap = new R.Layers();
        /*页面装载完毕后执行*/
        R.layerMap.onLoad = function() {
        	  this.selectOp = $("#selectOp input").val();
           /*创建地图*/
           this.createMap();
           /*添加二维图层*/
           this.addWmsLayer(R.TD_LNAME, {isBaseLayer : true});
           /*添加选择图层*/
           this.addVectorLayer(R.SELECT_LNAME);         
           /*添加控件*/
           this.addControls();
           /*将地图调整为边界大小*/
           this.map.zoomToExtent(R.BOUNDS);
        };
    })();
    /*页面元素控制部分*/
    $(function(){
      $("#selectOp input").bind("click", function() {
    	  var value = $(this).val();
    	  /*存放入对象中 以便添加矿区层时使用*/
        R.layerMap.selectOp = value;
    	  var boxCtl =  R.layerMap.boxControl;
    	  var pynCtl = R.layerMap.pynControl;
    	  var gfCtl = R.layerMap.gfControl;
    	  var selectCtl = R.layerMap.selectControl;
    	  /*导航*/
    	  if(value == R.selectOp.NAV) {
    		  boxCtl.deactivate();
    		  pynCtl.deactivate();
    		  selectCtl.deactivate();
    		  if(gfCtl) {
    			  gfCtl.activate();
    		  }
    	  /*区域选择*/
    	  } else if(value == R.selectOp.BOX) {
    		  boxCtl.activate();
    		  pynCtl.deactivate();
    		  selectCtl.deactivate();
    		  if(gfCtl) {
    			  gfCtl.deactivate();
    		  }
    	  /*多边形选择*/
    	  } else if(value == R.selectOp.PYN) {
    		  boxCtl.deactivate();
           pynCtl.activate();
           selectCtl.deactivate();
           if(gfCtl) {
        	   gfCtl.deactivate();
           }
           /*图形选择*/
    	  } else if(value == R.selectOp.SELECT) {
              boxCtl.deactivate();
              pynCtl.deactivate();
              selectCtl.activate();
              if(gfCtl) {
               gfCtl.deactivate();
              }
           } else {
        	   var name = $(this).attr('name');
        	   /*清除选中*/
        	   if(name == R.selectOp.RM) {
        		   var selectLayer = R.layerMap[R.SELECT_LNAME];
        		   selectLayer.removeFeatures(selectLayer.selectedFeatures);
               /*清除全部*/
        		} else if(name == R.selectOp.RMALL) {
        			var selectLayer = R.layerMap[R.SELECT_LNAME];
        			selectLayer.removeAllFeatures();
             }
           }
      });
      /*底层图片的切换*/
      $("#mapSelect input[type='radio']").bind("click", function() {
          
      });
      /*专题层的选择*/
     $("#mapSelect input[type='checkbox']").bind("click", function() {
    	 var layerName = $(this).val() + "Layer";
    	 if(! R[layerName]) {
    		 alert("该图尚不存在");
    	 }
    	 var checked = $(this).attr("checked") == "checked";
    	 /*如果选中了 则添加图层 否则删除图层*/
    	 if(checked) {
    		 R.layerMap.addWmsLayer(layerName, {transparent : true});
    		 /*如果是矿区图 则需要添加getFeature控件*/
          if(R.KQ_LNAME == layerName) {
        	    R.layerMap.addGfControl();
        	    /*如果是导航模式 则激活控件*/
        	    if(R.layerMap.selectOp == R.selectOp.NAV) {
        		    R.layerMap.gfControl.activate();
        	    }
          }
    		 R.layerMap.resetLayerIdx();
    	 } else {
    		 /*如果是矿区图 则先把gf控件移除*/
    		 if(R.KQ_LNAME == layerName) {
    			 R.layerMap.removeGfControl();
           }
    		 R.layerMap.removeWmsLayer(R.layerMap[layerName]);
    	 }
     });
    });
    
    </script>
   </head>
   <body onload="R.layerMap.onLoad()">
   <!-- 左边 -->
      <div id="leftDiv">
      <div id="mapSelect">
      <!-- 矿区选择 -->
         <ul id="kqSelect">
            <li>
               <select>
                  <option value ="">--选择矿区--</option>
                  <option value ="pingshuo">平朔</option>
               </select>
            </li>
            <li>
               <select>
                  <option value ="">--选择日期--</option>
                  <option value ="2010">2010</option>
                  <option value ="2011">2011</option>
               </select>
            </li>
            <li style="float:none"><input id="queryMap" type="button" value="地图查询"></input></li>
         </ul>
      </div>
      <!-- 地图选择 -->
      <div>
         <ul id="mapSelect">
            <li style="margin-right:30px">
               <input type="radio" value="td" name="baseType" checked="checked"/>二维图
               <input type="radio" value="yg" name="baseType"/>遥感图
            </li>
            <li>
               <input type="checkbox" value="kq"/>矿区
               <input type="checkbox" value="tdly"/>土地利用
               <input type="checkbox" value="dbtx"/>地表塌陷
               <input type="checkbox" value="trqs"/>土壤侵蚀
               <input type="checkbox" value="gqyg"/>高清遥感图
            </li>
         </ul> 
      </div>
      <div id="map"></div>
               <div id="wrapper">
         <!-- 地理位置信息 -->
         <div id="location">位置</div>
         <!-- 比例尺信息 -->
         <div id="scale">Scale</div> 
       </div>
       <!-- 区域选择的类型 -->
       <div id="selectOp">
       <input type="radio" value="nav" checked="checked" name="op"/>导航
       <input type="radio" value="box" name="op"/>区域绘制
       <input type="radio" value="pyn" name="op"/>多边形绘制
       <input type="radio" value="select" name="op"/>区块选择
       <input type="button" value="清除所选" name="rm" id="rm"/>
       <input type="button" value="清除全部" name="rmAll" id="rmAll"/>
       </div>
    </div>
    <!-- 右边 -->
     <div id="rightDiv">
      <input id="queryData" type="button" value="查询指数"/><br/>
      查询结果：<br/>
      <div id="content"></div>
     </div>
   </body>
</html>