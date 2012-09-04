<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ include file="/common/includes.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Shapefiles</title>
</head>
<body>
<table >
<tr>
<td>区域</td><td>年份</td>
</tr>
<c:foreach items="${shapefiles}" var="file">
<td><a href="#">${file.place}</a></td>
<td>${file.shootTime}</td>
</c:foreach>
</table>
</body>
</html>