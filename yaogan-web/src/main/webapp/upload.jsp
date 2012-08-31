<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>upload shapefile</title>
</head>
<body>
	<form method="post" action="upload/gis" enctype="multipart/form-data">
		<label>地区</label><input type="text" name="region" /> <br/>
		<label>拍摄时间</label><input type="text" name="shootTime" /> <br/>
		<label>土地类型文件</label><input type="file" name="landType" /> <br/>
		 <label>土壤侵蚀文件</label><input type="file" name="landSoil" /> <br/>
		 <label>矿区边界文件</label><input type="file" name="boundary" /> <br/>
		 <input type="submit" value="上传"/>
	</form>
</body>
</html>