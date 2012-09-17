    /**地图控制部分*/
    (function(){
    	var layerGroupWmsUrl = "/geoserver/wms";
    	var layerWmsUrl = "/geoserver/yaogan/wms";
    	var legendUrl = layerWmsUrl + "?VERSION=1.1.1&FORMAT=image/png&SERVICE=WMS&REQUEST=GetLegendGraphic&TRANSPARENT=TRUE&WIDTH=40&LAYER=";
    	var kqLayerName = null;
    	var tdlyLayerName = null;
    	var dbtxLayerName = null;
    	var trqsLayerName = null;
    	var gqygLayerName = null;
    	var dlfLayerName = null;
    	
    	//OpenLayers.Lang.setCode("");
        /**全局命名空间R*/
        var R = window.R = {
        		/*顺序号 唯一标示的递增值*/
        		seq : 0,
        	    /*选择要素的图层的名字*/
        	   SELECT_LNAME : "selectLayer",
               /*二维图层的名字*/
                TD_LNAME : "tdLayer",
                /*二维layer配置*/
                tdLayer : {
                  wmsUrl : layerGroupWmsUrl,
                  layerName : "shanximap",
                  showName : "二维地图"
                },
                /*遥感层名字*/
                YG_LNAME : "ygLayer",
                /*遥感layer配置*/
                ygLayer : {
                   wmsUrl : layerWmsUrl,
                   layerName : "yaogan:shanxisheng",
                   showName : "遥感地图"
                },
                /*矿区layer名字*/
                KQ_LNAME : "kqLayer",
                /*矿区layer配置*/
                kqLayer : {
                  wmsUrl : layerWmsUrl,
                  layerName : kqLayerName,
                  idx : 7,
                  stamp : 0,
                  showName : "矿区"
                },
                /*土地利用配置*/
                 tdlyLayer : {
                   wmsUrl : layerWmsUrl,
                   layerName : tdlyLayerName,
                   idx : 5,
                   stamp : 0,
                   showName : "土地利用"
                },
                /*地裂缝配置*/
                dlfLayer : {
                  wmsUrl : layerWmsUrl,
                  layerName : dlfLayerName,
                  idx : 7,
                  stamp : 0,
                  showName : "地裂缝"
               },                
                /*地表塌陷配置*/
                 dbtxLayer : {
                   wmsUrl : layerWmsUrl,
                   layerName : dbtxLayerName,
                   idx : 7,
                   stamp : 0,
                   showName : "地塌陷"
                },
                /*土壤侵蚀配置*/
                 trqsLayer : {
                   wmsUrl : layerWmsUrl,
                   layerName :  trqsLayerName,
                   idx : 5,
                   stamp : 0,
                   showName : "土壤侵蚀"
                },
                /*高清遥感配置*/
                gqygLayer : {
                  wmsUrl : layerWmsUrl,
                  layerName :  gqygLayerName,
                  idx : 3,
                  stamp : 0,
                  showName : "高清遥感"
               }, 
               /*选择图层*/
               selectLayer : {
            	   idx : 9,
                   stamp : 0
               },
                /*边界值 目前为一个省的边界值*/
                BOUNDS : new OpenLayers.Bounds(
                		110.220484, 34.582452,
                		114.552878, 40.741043
                  ),
                  /*坐标系*/
                SRS : "EPSG:4326",
                /*矢量图层样式*/
                vectorStyle : {
                	SELECT : "select"
                },
                /*baseLayer的类型 二维或遥感*/
                baseType : {
                	TD : "td",
                	YG : "yg"
                },
                /*请求的url*/
                reqUrl: {
                	/*根据地区选择时间*/
                	GETTIME : "./layers/getAvailableTime",
                	/*查询地图信息*/
                	QUERYMAP : "./layers/queryMap",
                	/*查询指数信息*/
                	QUERYSTATS : "./layers/queryEnvStats",
                	/*图例URL part*/
                	LEGEND : legendUrl
                },
                getSeq : function() {
                	return this.seq++;
                }
        };
        
        /**Layers为地图层的大对象*/
        R.Layers = function(){
        /*this引用，用于传值*/
        var _self = this;
        /*图片格式  各种格式的优缺点未具体调研*/
        this.format = 'image/png';
        var map = this.map = null;
        /**创建地图  */
        this.createMap = function() {
           /*创建一个可以包含多个层的地图*/
           var options = {controls : [], maxExtent: R.BOUNDS, projection: R.SRS};
           map = this.map = new OpenLayers.Map('map', options);
           
        };
        this.layers = [];
        /**创建wms图层*/
        this.createWmsLayer = function(name, option) {
          /*参数*/
          var param =  {LAYERS : R[name].layerName,
                     style : '',
                     format : this.format,
                     /*透明度*/
                     transparent : (option && option.transparent) ? option.transparent : false
                    };
          /*选项*/
          var options =  {singleTile: true,
        		          /*是否是基础层*/
        		          isBaseLayer : (option && option.isBaseLayer) ? option.isBaseLayer : false}; 
             /*name,url,param,options*/
          var layer = new OpenLayers.Layer.WMS(R[name].showName, R[name].wmsUrl, param, options);
          layer.ygName = name;
          return layer;    
        };
        /**创建vector图层*/
        this.createVectorLayer = function(name, options) {
           options.displayInLayerSwitcher = false;
           var layer = new OpenLayers.Layer.Vector(name, options);
           layer.ygName = name;
           return layer;
        };
        /**添加控件*/
        /**添加控件*/
        this.addControls = function() {
            /*添加左边的竖状工具栏 两个参数值为坐标值*/
            map.addControl(new OpenLayers.Control.PanZoomBar({
                position: new OpenLayers.Pixel(2, 42)
            }));
            /*添加比例尺*/
            map.addControl(new OpenLayers.Control.Scale());
            /*添加比例尺*/
            map.addControl(new OpenLayers.Control.ScaleLine());
            /*添加坐标值*/
            map.addControl(new OpenLayers.Control.MousePosition());
            /*添加鼠标操作的的导航*/
            this.navControl = new OpenLayers.Control.Navigation({title : "导航模式(导航并可以选择矿区图上某一个矿查询指数)"});
            this.navControl.events.register("activate", this, function(){
            	if(_self.gfControl) {
            		_self.removeGfControl();
            		_self.addGfControl();
            		_self.gfControl.activate();
            	}
            });
            /*区域选择*/
            this.boxControl = new OpenLayers.Control.DrawFeature(this[R.SELECT_LNAME],
                                            OpenLayers.Handler.RegularPolygon,
                                            {handlerOptions: {sides: 4, irregular: true},
                                            	displayClass : "box",
                                            	title : "区域绘制(可以画一个四方形并查询指数)"});
            /*多边形选择*/
            this.pynControl = new OpenLayers.Control.DrawFeature(this[R.SELECT_LNAME],
                    OpenLayers.Handler.Polygon, {displayClass : "pyn",title : "多边形绘制(可以画一个多边形并查询指数)"});
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
            layerSwitcher = new OpenLayers.Control.LayerSwitcher();
            layerSwitcher.ascending = false;
            layerSwitcher.useLegendGraphics = true;
            
            //map.addControl(layerSwitcher); 
            var panel = new OpenLayers.Control.Panel({ defaultControl: this.navControl });   
            panel.addControls([this.navControl, this.boxControl, this.pynControl]);
            map.addControl(panel); 
            /*多边形选择*/
        };
        /**添加获取特征值的控件 定为矿山的特征值*/
        this.addGfControl = function() {
        	if(! this[R.KQ_LNAME]) {
        		return;
        	}
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
        /**移除获取特征值的控件*/
        this.removeGfControl = function() {
        	this.gfControl.deactivate();
        	map.removeControl(this.gfControl);
        };
        /**添加wms图层*/
        this.addWmsLayer = function(name, option) {
           var layer = this.createWmsLayer(name, option);
           this[name] = layer;
            this.addLayer(layer);
        };
        /**删除wms图层*/
        this.removeWmsLayer=function(name) {
           var layer = this[name];
           if(layer) {
              map.removeLayer(layer);
              this[name] = null;
           }
        };
        
        /**增加一个layer*/
        this.addLayer=function(layer) {
        	/*先将此layer添加到map中*/
        	map.addLayers([layer]);
        	/*如果是baseLayer 则直接返回 不用调整顺序*/
        	if(layer.isBaseLayer) {
        		return;
        	}
        	/*进行顺序调整 调整方法为：从layers的最顶层开始 逐个非baseLayer比较 以确定位置 比较时先比较idx优先级 再比较stamp idx大 stamp大的在顶层*/
        	var pos = map.layers.length - 1;
        	for(var i = map.layers.length - 2; i >= 0; i--) {
        		var current = map.layers[i];
        		if(current.isBaseLayer || (! current.ygName) || (! R[current.ygName])) {
        			continue;
        		}
        		if(R[current.ygName].idx < R [layer.ygName].idx) {
        			break;
        		}
        		if(R[current.ygName].idx > R [layer.ygName].idx) {
        			pos = i;
        		} else {
        			if(R[current.ygName].stamp < R [layer.ygName].stamp) {
        				break;
        			} else {
        				pos = i;
        			}
        		}
        	}
        	map.setLayerIndex(layer,pos);
        };
        /**添加vector图层*/
        this.addVectorLayer = function(name, option) {
           var ot = {};
           if(option && option.style == R.vectorStyle.SELECT) {
        	   ot.styleMap = new OpenLayers.Style(OpenLayers.Feature.Vector.style["select"]);
           }
           var layer = this.createVectorLayer(name, ot);
        	  this[name] = layer;
           this.addLayer(layer);
        };
        /**元素选择事件*/
        this.addFeatureEvent = function() {
        	/*元素添加事件*/
        	this[R.SELECT_LNAME].events.register("featureadded", this, function(event) {
        		var layer = event.object;
        		var feature = event.feature;
        		if(layer.features.length > 1) {
        			var rm = [];
        			for(var i = 0; i < layer.features.length - 1; i++) {
        				rm[rm.length] = layer.features[i];
        			}
        			layer.removeFeatures(rm);
        		}
        		var placeId = R.layerMap.placeId;
        		var time = R.layerMap.time;
        		if(placeId && time) {
        			var geometry = "geometry=" + feature.geometry;
        			/*顺序号 用于控制延迟*/
        			R.popupSeq = feature.popupSeq = R.getSeq();
        			R.queryAreaData(placeId, time, geometry, feature);
        		}
        	});
        	/*元素移除事件*/
        	this[R.SELECT_LNAME].events.register("featureremoved", this, function(event) {
        		if(event.feature.popup) {
        		  R.layerMap.map.removePopup(R.popup);
       		      R.popup.destroy();
       		      event.feature.popup == null;
        		}
        	});
        	
        };
        };
        R.layerMap = new R.Layers();
        /**
         * 查询整个矿区数据
         * */
        R.queryPlaceData = function(placeId,time) {
     	   $.getJSON(R.reqUrl.QUERYSTATS + "?placeId=" + placeId + "&time=" + time, function(result) {
     		   $("#placeData span").empty();
     		   if(result == null) {
     			   return;
     		   }
     		  $("#plcswfd").text(result.abio);
     		  $("#plczbfg").text(result.aveg);
     		  $("#plctdth").text(result.aero);
     		  $("#plcdzhj").text(result.asus);	   
     	   });	 
        };
        
        /**
         * 查询地区数据
         */
        R.queryAreaData = function(placeId,time,geometry,feature) {
     	   $.getJSON(R.reqUrl.QUERYSTATS + "?placeId=" + placeId + "&time=" + time + "&" + geometry, function(result) {
     		   /*如果顺序号过了 则说明点击了新的 当前的丢弃*/
     		   if(feature.popupSeq != R.popupSeq) {
     			   return;
     		   }
     		   if(result == null) {
     			   alert("未查询到相关指数");
     			   return;
     		   }
     		   $("#areaData span").empty();
     		   $("#areaData #areaswfd").text(result.abio);
     		   $("#areaData #areazbfg").text(result.aveg);
     		   $("#areaData #areatdth").text(result.aero);
     		   $("#areaData #areadzhj").text(result.asus);
     		   feature.popup=R.popup = new OpenLayers.Popup.FramedCloud("chicken", feature.geometry.getBounds().getCenterLonLat(),
     				 null, $("#areaData ul").html(), null, true, null); 
     		   R.layerMap.map.addPopup(R.popup);
     	   });	
        };
        
        /**初始化函数 用于页面加载和矿区选择*/
        R.layerMap.init = function(){
         /*创建地图*/
         this.createMap();
        	/*基础图层名称*/
         this.baseLayerName = $("#mapSelect input[type='radio']:checked").val() + "Layer";
        	/*添加基础图层*/
         this.addWmsLayer(R.TD_LNAME, {isBaseLayer : true});
         this.addWmsLayer(R.YG_LNAME, {isBaseLayer : true});
         this.map.setBaseLayer(this[this.baseLayerName]);
        	/*专题图层名称*/
        	var spcLayerNames = this.spcLayerNames = [];
        	$("#mapSelect input[type='checkbox']:checked").each(function(){
        		spcLayerNames[spcLayerNames.length] = $(this).val();
        	});
        	/*叠加各专题图层*/
        	for(var i = 0; i < spcLayerNames.length; i++) {
        		var name = spcLayerNames[i] + "Layer";
        		if(! R[name].layerName) {
        			continue;
        		}
        		this.addWmsLayer(name, {transparent : true});
                /*如果是矿区图 则需要添加getFeature控件*/
                if(R.KQ_LNAME == name) {
                   this.addGfControl();
                   this.gfControl.activate();
                }
                /*添加图例*/
                $("#mapSelect li[id='" + spcLayerNames[i] + "'] div").html("<img src='" + R.reqUrl.LEGEND + R[name].layerName + "'/>");
        	}
        	
         /*添加选择图层*/
         this.addVectorLayer(R.SELECT_LNAME);   
         /*元素选择事件*/
         this.addFeatureEvent();
         /*添加控件*/
         this.addControls();

         var bounds = R[R.KQ_LNAME].bounds ? R[R.KQ_LNAME].bounds : R.BOUNDS;
         /*将地图调整为边界大小*/
         this.map.zoomToExtent(bounds);
        };
    })();
    
   /**页面元素控制部分*/
   $(function(){    
	  /**地区下拉选择*/
      $("#placeSelect").bind("change", function() {
    	   var val = ($(this).val());
    	   timeSelect(val);
      });
      
      /**根据地区选择时间*/
      var timeSelect = function(val) {
   	   /**如果选择*/
   	   if(val == "") {
   		   return;
   	   }
   	  $.getJSON(R.reqUrl.GETTIME + "?placeId=" + val, function(result) {
   		  $("#timeSelect").empty();
   		  $("#timeSelect").append("<option value =''>--选择--</option>");
   		  for(var i = 0; i < result.length; i++) {
   			  $("#timeSelect").append("<option value ='" + result[i] + "'>" + result[i] + "</option>");
   		  }
   	  }); 
      };
      
      /**点击查询地图按钮*/
      $("#queryMap").bind("click", function() {
    	  var placeId = $("#placeSelect").val();
    	  var time = $("#timeSelect").val();
    	  if(placeId == "" || time == "") {
    		  alert("请选择地区和时间");
    		  return;
    	  }
    	  $.getJSON(R.reqUrl.QUERYMAP + "?placeId=" + placeId + "&time=" + time, function(result) {
    		  if(result == null) {
    			  alert("地图查询失败");
    			  return;
    		  }
    		  if(result.length == 0) {
    			  alert("目前没有该地区和日期的数据");
    			  return;
    		  }
    		  /*重新设置当前地图的placeId和time*/
    		  R.layerMap.placeId = placeId;
    		  R.layerMap.time = time;
    		  /*清空当前的专题图层名称*/
    	      R.kqLayer.layerName = R.tdlyLayer.layerName = R.dbtxLayer.layerName
    	      = R.trqsLayer.layerName = R.gqygLayer.layerName = null;
    	      /*设置新的当前的专题图层名称*/
    	      for(var i = 0; i < result.length; i++) {
    	    	  R[result[i].type + "Layer"].layerName = result[i].layerName;
    	    	  R[result[i].type + "Layer"].bounds = result[i].bounds;
    	      }
    	      /*清空现有地图*/
              R.layerMap.map.destroy();
              $("#map").empty();
              /*渲染新的地图*/
              R.layerMap.init();
              /*查询专题数据*/
              R.queryPlaceData(R.layerMap.placeId,R.layerMap.time);
    	  });
      });
      
      /**底层图片的切换*/
      $("#mapSelect input[type='radio']").bind("click", function() {
     	 var layerName = $(this).val() + "Layer";
     	 R.layerMap.map.setBaseLayer(R.layerMap[layerName]);
      });
      
      /**专题层的选择*/
     $("#mapSelect input[type='checkbox']").bind("click", function() {
    	 var layerName = $(this).val() + "Layer";
    	 if(! R[layerName].layerName) {
    		 return;
    	 }
    	 var checked = $(this).attr("checked") == "checked";
    	 /*如果选中了 则添加图层 否则删除图层*/
    	 if(checked) {
        	 R[layerName].stamp = R.getSeq();
    		 R.layerMap.addWmsLayer(layerName, {transparent : true});
          /*如果是矿区图 则需要添加getFeature控件*/
          if(R.KQ_LNAME == layerName) {
        	    R.layerMap.addGfControl();
        		R.layerMap.gfControl.activate();
          }
          /*添加图例*/
          $("#mapSelect li[id='" + $(this).val() + "'] div").html("<img src='" + R.reqUrl.LEGEND + R[layerName].layerName + "'/>");
    	 } else {
    		 /*如果是矿区图 则先把gf控件移除*/
    		 if(R.KQ_LNAME == layerName) {
    			 R.layerMap.removeGfControl();
           }
    		 R.layerMap.removeWmsLayer(layerName);
    		 /*删除图例*/
    		 $("#mapSelect li[id='" + $(this).val() + "'] div").empty();
    	 }
     });


   $("#thematicMapLink").bind("click", function(){
	   var placeId = $("#placeSelect").val();
 	   var time = $("#timeSelect").val();
 	   document.location.href = "./print?placeId=" + placeId + "&time=" + time + "&category=FILE_LAND_TYPE";
   });   
   /**页面装载完毕后对地图进行初始化*/
   R.layerMap.init();
   /**根据地区查询时间 firefox默认会记录下拉选择*/
   timeSelect($("#placeSelect").val());
    });