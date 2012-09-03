<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
        <script defer="defer" type="text/javascript">
 
        	var map = R.Layers.map = new OpenLayers.Map('map', R.Layers.options);
        	R.Layers.addProv(map);
        	R.Layers.addKq(map);
        	R.Layers.addSelect(map);
       // 	R.Layers.addHover(map);
         R.Layers.addControl(map);
         map.zoomToExtent(R.Layers.bounds);        	
        </script>