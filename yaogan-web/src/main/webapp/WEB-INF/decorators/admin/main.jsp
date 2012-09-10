<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/common/tag.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%@taglib prefix="decorator"
	uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page"%>
<!doctype html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><decorator:title default="遥感影像服务" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="0">
<link rel="stylesheet" type="text/css"
	href="${ctx}/static/css/style.css">
<decorator:head />
</head>
<body class="claro">
	<%
	   String domainName = "";
	   String host = request.getHeader("HOST");
	   String[] arr = host.split("\\.");
	   if (arr.length == 4) {
	      boolean isIp = false;
	      try {
	         Integer.parseInt(arr[0]);
	         isIp = true;
	      } catch (Exception e) {
	      }
	      if (isIp) {
	      } else {
	         domainName = new StringBuilder().append(arr[1]).append(".")
	               .append(arr[2]).append(".").append(arr[3]).toString();
	      }
	   }
	   String cloudScriptUrl = null;
	   if (domainName.length() > 0) {
	      cloudScriptUrl = "http://" + domainName + "/navbar/banner/partner";
	   }
	%>
	<%
	   if (cloudScriptUrl != null) {
	%>
	<script type="text/javascript" src="<%=cloudScriptUrl%>"></script>
	<%
	   }
	%>
	<page:applyDecorator name="adminHeader" />
	<div id="wrap">
		<div id="left">
			<div class="left_top">用户操作</div>
			<div class="left_menu">
				<page:applyDecorator name="adminMenu" />
			</div>
		</div>
		<decorator:body />
		<div class="clear"></div>
	</div>
</body>
</html>
