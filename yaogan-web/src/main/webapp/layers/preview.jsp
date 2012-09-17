<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>专题图预览</title>
<script type="text/javascript"> 
  function printsetup(){ 
  // 打印页面设置 
  wb.execwb(8,1); 
  } 
  function printpreview(){ 
  // 打印页面预览 
     
  wb.execwb(7,1); 
  } 


  function printit() 
  { 
  if (confirm('确定打印吗？')) { 
  wb.execwb(6,6) 
  } 
  } 
  </script>


  <style media=print> 
.noprint{display:none;} 
</style> 
</head>
<body>
<center>
<object id=wb height=0 width=0 classid=clsid:8856f961-340a-11d0-a96b-00c04fd705a2 name=wb></object>
<input onclick="printit();" type="button" value="打印 " name="button_print" class='noprint'/> 
<input onclick="printsetup();" type="button" value="打印页面设置"  name="button_setup" class='noprint'/> 
(非IE浏览器请把图片另存到本地后打印。)<br />
<img alt="生成失败" src="/yaogan-web/static/themeImage/${fileName}">
</center>
</body>
</html>