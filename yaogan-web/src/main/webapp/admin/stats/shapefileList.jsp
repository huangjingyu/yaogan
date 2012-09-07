<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/includes.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Shapefiles</title>
</head>
<body>
	<div>
		<form action="" method="post">
			<lable>地区</lable>
			<input type="text" name="place"></input>
			<lable>年份</lable>
			<input type="text" name="shootTime"></input> <input type="submit"
				value="查询"></input>
		</form>
		<div>
			<a href="${ctx}/admin/upload/form">矿区数据上传</a>
		</div>
		<table border="1">
			<tr>
				<td>地区名称</td>
				<td>年份</td>
				<td>文件类型</td>
				<td>文件名</td>
				<td>上传时间</td>
			</tr>
			<c:forEach items="${shapefiles}" var="file">
				<tr>
					<td>${file.place.name}</td>
					<td>${file.shootTime}</td>
					<td>${file.typeString}</td>
					<td>${file.fileName}</td>
					<td>${file.uploadTime}</td>
				</tr>
			</c:forEach>
		</table>
	</div>
</body>
</html>