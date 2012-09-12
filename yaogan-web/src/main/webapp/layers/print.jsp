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
<table align="center">
<tr>
<td>
<form action="${ctx }/user/print/preview" method="post">
请输入备注：<textarea rows="3" cols="4" name="comment"></textarea>
<input type="text" name="mapPath" style="visibility: hidden;" value="${mapPath}">
<input type="submit" value="打印预览">
<img alt="加载中。。。" src="${ctx}/static/temp/111.jpg" height="50%" width="50%">
</form>
</td><td>
模版：<br /><img alt="模版" src="${ctx}/static/template/template1.jpg" height="50%" width="50%">
</td></tr>
</table> 
</body>
</html>