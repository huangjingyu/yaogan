<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/includes.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css"
	href="${ctx}/static/css/style.css" />
<title>upload shapefile</title>
<script type="text/javascript" src="${ctx}/static/js/jquery-1.7.1.js"></script>
<script type="text/javascript">
	function upload() {
		document.getElementById("uploadForm").submit();
	}
</script>

</head>
<body>
	<div class="rtlist">
		<div class="map_top">文件上传</div>
		<div class="rtnr">
			<form id="uploadForm" name="uploadForm" method="post"
				action="${ctx}/admin/upload/submit" enctype="multipart/form-data">
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					id="table_two">
					<tr>
						<td width="17%"><label>地区：</label></td>
						<td width="83%"><label> <input name="region"
								type="text" id="textfield" size="30" />
						</label></td>
					</tr>
					<tr>
						<td><label>地下水下降量：</label></td>
						<td><input name="groundWaterDesc" type="text" id="textfield2"
							size="30" /></td>
					</tr>
					<tr>
						<td><label>拍摄时间：</label></td>
						<td><input name="shootTime" type="text" id="textfield3"
							size="30" /></td>
					</tr>
					<tr>
						<td><label>土地类型文件：</label></td>
						<td><input name="landType" type="file" id="textfield4"
							size="30" /></td>
					</tr>
					<tr>
						<td><label>土壤侵蚀文件：</label></td>
						<td><input name="landSoil" type="file" id="textfield5"
							size="30" /></td>
					</tr>
					<tr>
						<td><label>矿区边界文件：</label></td>
						<td><input name="boundary" type="file" id="textfield6"
							size="30" /></td>
					</tr>
					<tr>
						<td><label>地塌陷文件：</label></td>
						<td><input name="collapse" type="file" id="textfield7"
							size="30" /></td>
					</tr>
					<tr>
						<td><label>地裂缝文件：</label></td>
						<td><input name="fracture" type="file" id="textfield8"
							size="30" /></td>
					</tr>
					<tr> 
						<td><label>高清遥感文件：</label></td>
						<td><input name="hig_def" type="file" id="textfield9"
							size="30" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><img src="${ctx}/static/img/butt_submit.gif" width="89"
							height="29" onclick="upload()" /></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	</div>
	<div class="clear"></div>
	</div>
	<div class="clear"></div>
</body>
</html>