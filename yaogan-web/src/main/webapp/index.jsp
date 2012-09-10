<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<html>
<% %>
<head></head>
<body>
	<p><h1>Welcome</h1></p><br />
	<a href="${pageContext.request.contextPath}/doLogout">logout</a>
	<br />
	<a href="home.jsp">home</a>
	<br />
	<a href="shapefile/print?comment=hello&placeId=1&time=1989&category= FILE_LAND_TYPE">打印</a>
</body>
</html>