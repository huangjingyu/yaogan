    /**地图控制部分*/
    (function(){
    	var layerGroupWmsUrl = "/geoserver/wms";
    	var layerWmsUrl = "/geoserver/yaogan/wms";
    	var test = false;
    	var kqLayerName = test ? "yaogan:pingshuo_KQ" : null;
    	var tdlyLayerName = test ? "yaogan:tdly2011" : null;
    	var dbtxLayerName = test ? "yaogan:dbtx2011" : null;
    	var trqsLayerName = test ? "yaogan:trqs2011" : null;
    	var gqygLayerName = test ? "yaogan:TH01_2011" : null;
    	var dlfLayerName = test ? "yaogan:groundcrack2011" : null;
        /**全局命名空间R*/
        var R = window.R = {
        	    /*选择要素的图层的名字*/
        	   SELECT_LNAME : "selectLayer",
               /*二维图层的名字*/
                TD_LNAME : "tdLayer",
                /*二维layer配置*/
                tdLayer : {
                  wmsUrl : layerGroupWmsUrl,
                  layerName : "shanximap"
                },
                /*遥感层名字*/
                YG_LNAME : "ygLayer",
                /*遥感layer配置*/
                ygLayer : {
                	wmsUrl : layerWmsUrl,
                layerName : "yaogan:shanxisheng"
                },
                /*矿区layer名字*/
                KQ_LNAME : "kqLayer",
                /*矿区layer配置*/
                kqLayer : {
                  wmsUrl : layerWmsUrl,
                  layerName : kqLayerName
                },
                /*土地利用配置*/
                 tdlyLayer : {
                   wmsUrl : layerWmsUrl,
                   layerName : tdlyLayerName
                },
                /*地裂缝配置*/
                dlfLayer : {
                  wmsUrl : layerWmsUrl,
                  layerName : dlfLayerName
               },                
                /*地表塌陷配置*/
                 dbtxLayer : {
                   wmsUrl : layerWmsUrl,
                   layerName : dbtxLayerName
                },
                /*土壤侵蚀配置*/
                 trqsLayer : {
                   wmsUrl : layerWmsUrl,
                   layerName :  trqsLayerName
                },
                /*高清遥感配置*/
                gqygLayer : {
                  wmsUrl : layerWmsUrl,
                  layerName :  gqygLayerName
               },                
                /*边界值 目前为一个省的边界值*/
                BOUNDS : new OpenLayers.Bounds(
                		110.220484, 34.582452,
                		114.552878, 40.741043
                  ),
                  /*坐标系*/
                SRS : "EPSG:4326",
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
                	QUERYSTATS : "./layers/queryEnvStats"
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
          var options =  {
        		          /*是否是基础层*/
        		          isBaseLayer : (option && option.isBaseLayer) ? option.isBaseLayer : false}; 

             /*name,url,param,options*/
          var layer = new OpenLayers.Layer.WMS(name, R[name].wmsUrl, param, options);
          return layer;    
        };
        /**创建vector图层*/
        this.createVectorLayer = function(name, options) {
        	if(! options) {
        	   return new OpenLayers.Layer.Vector(name);
        	} else {
        		return new OpenLayers.Layer.Vector(name, options);
        	}
        };
        /**添加控件*/
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
        /**根据选择的类型 调整组件的可用性*/
        this.adjustControls = function() {
            var boxCtl = this.boxControl;
            var pynCtl = this.pynControl;
            var gfCtl = this.gfControl;
            var selectCtl = this.selectControl;
            var selectOp = this.selectOp;
            /*导航*/
            if(selectOp == R.selectOp.NAV) {
               boxCtl.deactivate();
               pynCtl.deactivate();
               selectCtl.deactivate();
               if(gfCtl) {
                  gfCtl.activate();
               }
            /*区域选择*/
            } else if(selectOp == R.selectOp.BOX) {
               boxCtl.activate();
               pynCtl.deactivate();
               selectCtl.deactivate();
               if(gfCtl) {
                  gfCtl.deactivate();
               }
            /*多边形选择*/
            } else if(selectOp == R.selectOp.PYN) {
               boxCtl.deactivate();
               pynCtl.activate();
               selectCtl.deactivate();
               if(gfCtl) {
                gfCtl.deactivate();
               }
               /*图形选择*/
            } else if(selectOp == R.selectOp.SELECT) {
                  boxCtl.deactivate();
                  pynCtl.deactivate();
                  selectCtl.activate();
                  if(gfCtl) {
                   gfCtl.deactivate();
                  }
               }
        	
        };
        /**添加获取特征值的控件 定为矿山的特征值*/
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
        /**移除获取特征值的控件*/
        this.removeGfControl = function() {
        	map.removeControl(this.gfControl);
        };
        /**添加wms图层*/
        this.addWmsLayer = function(name, option) {
           var layer = this.createWmsLayer(name, option);
           this[name] = layer;
           map.addLayers([layer]);
        };
        /**删除wms图层*/
        this.removeWmsLayer=function(name) {
           var layer = this[name];
           if(layer) {
              map.removeLayer(layer);
              this[name] = null;
           }
        };
        /**将选择图层置于顶层*/
        this.resetLayerIdx=function() {
        	var num = map.getNumLayers();
        	map.setLayerIndex(this[R.SELECT_LNAME],num - 1);
        };
        /**添加vector图层*/
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
        /**初始化函数 用于页面加载和矿区选择*/
        R.layerMap.init = function(){
         /*创建地图*/
         this.createMap();
        	/*基础图层名称*/
         this.baseLayerName = $("#mapSelect input[type='radio']:checked").val() + "Layer";
        	/*添加基础图层*/
         this.addWmsLayer(this.baseLayerName, {isBaseLayer : true});
         
        	/*专题图层名称*/
        	var spcLayerNames = this.spcLayerNames = [];
        	$("#mapSelect input[type='checkbox'][checked='checked']").each(function(){
        		spcLayerNames[spcLayerNames.length] = $(this).val() + "Layer";
        	});
        	/*叠加各专题图层*/
        	for(var i = 0; i < spcLayerNames.length; i++) {
        		if(! R.spcLayerNames[i].layerName) {
        			continue;
        		}
        		this.addWmsLayer(spcLayerNames[i], {transparent : true});
                /*如果是矿区图 则需要添加getFeature控件*/
                if(R.KQ_LNAME == layerName) {
                   this.addGfControl();
                   /*如果是导航模式 则激活控件*/
                   if(R.layerMap.selectOp == R.selectOp.NAV) {
                      this.gfControl.activate();
                   }
                }
        	}
        	
         /*添加选择图层*/
         this.addVectorLayer(R.SELECT_LNAME);         
         /*添加控件*/
         this.addControls();
         
         /*当前选择的操作类型*/
         this.selectOp = $("#selectOp input").val();
         /*调整控件可用性*/
         this.adjustControls();
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
      });
      
      /**点击查询地图按钮*/
      $("#queryMap").bind("click", function() {
    	  var placeId = $("#placeSelect").val();
    	  var time = $("#timeSelect").val();
    	  if(placeId == "" || time == "") {
    		  alert("请选择地区和时间");
    		  return;
    	  }
    	  $.getJSON(R.reqUrl.QUERYMAP + "?placeId=" + placeId + "&time=" + time, function(result) {
    		  if(result.length == 0) {
    			  alert("目前没有该地区和日期的数据");
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
              queryPlaceData(R.layerMap.placeId,R.layerMap.time);
    	  });
      });
      
      /**底层图片的切换*/
      $("#mapSelect input[type='radio']").bind("click", function() {
     	 var layerName = $(this).val() + "Layer";
     	 /*如果此图层已存在 则直接返回*/
     	 if(R.layerMap[layerName]) {
     		 return;
     	 }
    	 if(! R[layerName].layerName) {
    		 alert("该类型专题图尚不存在");
    		 return;
    	 }
    	 var addLayer = layerName;
    	 var rmLayer;
    	 /*如果选择的是二维图 则删除遥感图层 添加二维图层*/
    	 if(R.TD_LNAME == layerName) {
    		 rmLayer = R.YG_LNAME;
         /*否则删除二维图层 添加遥感图层*/
    	 } else {
    		 rmLayer = R.TD_LNAME;
    	 }
    	 R.layerMap.addWmsLayer(addLayer, {isBaseLayer : true});
    	 R.layerMap.removeWmsLayer(rmLayer);
      });
      
      /**专题层的选择*/
     $("#mapSelect input[type='checkbox']").bind("click", function() {
    	 var layerName = $(this).val() + "Layer";
    	 if(! R[layerName].layerName) {
    		 alert("该图尚不存在");
    		 return;
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
    		 R.layerMap.removeWmsLayer(layerName);
    	 }
     });
 	/**
 	*操作类型的选择
 	*/
   $("#selectOp input[type='radio']").bind("click", function() {
 	  var value = $(this).val();
 	  /*存放入对象中 以便添加矿区层时使用*/
     R.layerMap.selectOp = value;
 	  R.layerMap.adjustControls();
   });
 	
   /**
   *清除按钮的点击
   */
   $("#selectOp input[type='button']").bind("click", function() {
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
   });  
   /**
    * 查询整个矿区数据
    * */
   var queryPlaceData = function(placeId,time) {
	   $.getJSON(R.reqUrl.QUERYSTATS + "?placeId=" + placeId + "&time=" + time, function(result) {
		   $("#placeData span").empty();
		   if($("#queryType input[name='swfd']").attr("checked") == "checked") {
			   $("#plcswfd").text(result.abio);
		   }
		   if($("#queryType input[name='zbfg']").attr("checked") == "checked") {
			   $("#plczbfg").text(result.aveg);
		   }
		   if($("#queryType input[name='tdth']").attr("checked") == "checked") {
			   $("#plctdth").text(result.aero);
		   }
		   if($("#queryType input[name='dzhj']").attr("checked") == "checked") {
			   $("#plcdzhj").text(result.asus);
		   }		   
	   });	 
   };
   
   /**
    * 查询地区数据
    */
   var queryAreaData = function(placeId,time,geometry) {
	   $.getJSON(R.reqUrl.QUERYSTATS + "?placeId=" + placeId + "&time=" + time + "&" + geometry, function(result) {
		   $("#areaData span").empty();
		   if($("#queryType input[name='swfd']").attr("checked") == "checked") {
			   $("#areaswfd").text(result.abio);
		   }
		   if($("#queryType input[name='zbfg']").attr("checked") == "checked") {
			   $("#areazbfg").text(result.aveg);
		   }
		   if($("#queryType input[name='tdth']").attr("checked") == "checked") {
			   $("#areatdth").text(result.aero);
		   }
		   if($("#queryType input[name='dzhj']").attr("checked") == "checked") {
			   $("#areadzhj").text(result.asus);
		   }		   
	   });	 
   };
   
   /**查询结果按钮点击*/
   $("#queryData").bind("click", function() {
	   var placeId = R.layerMap.placeId;
	   var time = R.layerMap.time;
	   if(! (placeId && time)) {
		   alert("请先查询地图");
		   return;
	   }
	   var features = R.layerMap[R.SELECT_LNAME].selectedFeatures;
	   if(features.length > 1) {
		   alert("目前仅支持单个图形查询");
		   return;
	   }
	   var geometry = "";
	   if(features.length > 0) {
		   geometry = "geometry=" + features[0].geometry;
	   }
	   queryAreaData(placeId,time,geometry);
  
   });
   
   /**页面装载完毕后对地图进行初始化*/
   R.layerMap.init();
    });