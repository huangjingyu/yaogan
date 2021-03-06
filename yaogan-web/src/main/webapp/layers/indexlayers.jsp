<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <!-- openlayers需要的css -->
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/js/openlayers/theme/default/style.css"/>
        <!-- 遥感页面需要的css -->
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/yaoganlayers.css"/>
        <script src='<%=request.getContextPath()%>/static/js/jquery-1.7.1.js'></script>  
        <!--openlayers脚本地址 开发时可以用lib目录 发布时需要使用压缩版 -->     
        <script src='<%=request.getContextPath()%>/static/js/openlayers/OpenLayers.js'></script>
        <script src='<%=request.getContextPath()%>/static/js/openlayers/lib/OpenLayers/Lang/zh-CN.js'></script>

        <!-- 遥感layers的控制脚本 -->
        <script src='<%=request.getContextPath()%>/static/js/yaoganlayers.js'></script>

        <!-- JQuery-UI -->
        <script src='<%=request.getContextPath()%>/static/js/jquery-ui-1.8.23.custom.min.js'></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/ui-lightness/jquery-ui-1.8.23.custom.css"/>
        
        
   </head>
   <body>
      <div id="middle2" style="min-width:910px">
         <%@include file="yaoganshow.jsp" %>
      </div>
   </body>
</html>