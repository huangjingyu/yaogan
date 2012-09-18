<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ include file="/common/includes.jsp"%>
    <c:set var="ctx" value="<%=request.getContextPath()%>" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>专题图打印</title>
</head>
<body>
<center>
<form action="./print/preview" method="post">
请输入标题：<input type="text" size="25" name="comment" />
<input type="submit" value="打印预览"><br>
<input type="text" name="mapPath" style="visibility: hidden;" value="${mapPath}"><br />
<img alt="加载中。。。" src="${ctx}/static/temp/${mapName}" height="30%" width="30%"><br />
</form>
</center>
</body>
</html>