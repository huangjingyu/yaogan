<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <!--  %@ include file="/common/includes.jsp"%>-->
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Shapefiles</title>
</head>
<body>
<form action="" method="post">
<lable>地区</lable><input type="text" name="place"></input>
<lable>年份</lable><input type="text" name="shootTime"></input>
<input type="submit" value="查询"></input>
</form>
<table >
<th>
<td>地区名称</td>
<td>年份</td>
<td>文件类型</td>
<td>文件名</td>
</th>
<c:foreach items="${shapefiles}" var="file">
<td><a href="#">${file.place}</a></td>
<td>${file.shootTime}</td>
<td>${file.category}</td>
<td>${file.fileName}</td>
<td></td>
</c:foreach>
</table>
</body>
</html>