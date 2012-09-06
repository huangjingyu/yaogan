    /**地图控制部分*/
    (function(){
    	var layerGroupWmsUrl = "http://www.rockloudtest.com/geoserver/wms";
    	var layerWmsUrl = "http://www.rockloudtest.com/geoserver/yaogan/wms";
        /**全局命名空间R*/
        var R = window.R = {
        		    /*选择要素的图层的名字*/
        		    SELECT_LNAME : "selectLayer",
               /*二维图层的名字*/
                TD_LNAME : "tdLayer",
                /*二维layer配置*/
                tdLayer : {
                  wmsUrl : layerGroupWmsUrl,
                  layerName : "shanxi"
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
                  layerName : "yaogan:pingshuo_KQ"
                },
                /*土地利用配置*/
                 tdlyLayer : {
                   wmsUrl : layerWmsUrl,
                   layerName : "yaogan:tdly2011"
                },
                /*地表塌陷配置*/
                 dbtxLayer : {
                   wmsUrl : layerWmsUrl,
                   layerName : "yaogan:dbtx2011"
                },
                /*土壤侵蚀配置*/
                 trqsLayer : {
                   wmsUrl : layerWmsUrl,
                   layerName : "yaogan:trqs2011"
                },
                /*高清遥感配置*/
                gqygLayer : {
                  wmsUrl : layerWmsUrl,
                  layerName : "yaogan:TH01_2011"
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
            } else if(value == R.selectOp.SELECT) {
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
         this.baseLayerName = $("#mapSelect input[type='radio'][checked='checked']").val() + "Layer";
        	/*添加基础图层*/
         this.addWmsLayer(this.baseLayerName, {isBaseLayer : true});
         
        	/*专题图层名称*/
        	var spcLayerNames = this.spcLayerNames = [];
        	$("#mapSelect input[type='checkbox'][checked=='checked']").each(function(){
        		spcLayerNames[spcLayerNames.length] = $(this).val() + "Layer";
        	});
        	/*叠加各专题图层*/
        	for(var i = 0; i < spcLayerNames.length; i++) {
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
         
         /*将地图调整为边界大小*/
         this.map.zoomToExtent(R.BOUNDS);
        };
    })();
    
    /**页面元素控制部分*/
    $(function(){    
      /**底层图片的切换*/
      $("#mapSelect input[type='radio']").bind("click", function() {
     	 var layerName = $(this).val() + "Layer";
     	 /*如果此图层已存在 则直接返回*/
     	 if(R.layerMap[layerName]) {
     		 return;
     	 }
    	 if(! R[layerName]) {
    		 alert("该图尚不存在");
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
   *清除按钮的选择
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
     /**页面装载完毕后对地图进行初始化*/
     R.layerMap.init();
    });