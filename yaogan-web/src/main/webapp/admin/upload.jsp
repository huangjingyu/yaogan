<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/includes.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>upload shapefile</title>
</head>
<body>
	<form method="post" action="${ctx}/admin/upload/submit" enctype="multipart/form-data">
		<label>地区</label><input type="text" name="region" /> <br/>
		<label>地下水下降量</label><input type="text" name="groundWaterDesc"></input><br/>
		<label>拍摄时间</label><input type="text" name="shootTime" /> <br/>
		<label>土地类型文件</label><input type="file" name="landType" /> <br/>
		<label>土壤侵蚀文件</label><input type="file" name="landSoil" /> <br/>
		<label>矿区边界文件</label><input type="file" name="boundary" /> <br/>
		<label>地塌陷文件</label><input type="file" name="collapse" /> <br/>
		<label>地裂缝文件</label><input type="file" name="fracture" /> <br/>
		<input type="submit" value="提交"/>
	</form>
</body>
</html>