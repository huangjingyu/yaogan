<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="com.rockontrol.yaogan.util.Functions"%>
<%@ page import="com.rockontrol.yaogan.model.User"%>
<%@ include file="/includes.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%
   String orgName = "";
   if (Functions.getSecMgr() != null) {
      User user = Functions.getSecMgr().currentUser();
      if (user != null && user.getOrganization() != null) {
         orgName = user.getOrganization().getName();
      }
      pageContext.setAttribute("curUserShowName",
            Functions.getUserRealNameOrName());
   }
%>
<%@taglib prefix="decorator"
	uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page"%>
<!doctype html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/ref.jsp"%>
<title><decorator:title default="企业管理中心" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="0">
<link rel="stylesheet" type="text/css"
	href="<c:url value='/static/css/css.css'/>" />
<link rel="stylesheet" type="text/css"
	href="<c:url value='/static/css/style.css'/>" />
<link rel="stylesheet" type="text/css"
	href="<c:url value='/static/css/cloud.css'/>" />
<link rel="stylesheet" type="text/css"
	href="<c:url value='/static/css/header-2.css'/>" />
<link rel="stylesheet" type="text/css"
	href="<c:url value='/static/css/left.css'/>" />
<link rel="stylesheet" type="text/css"
	href="<c:url value='/static/css/right.css'/>" />
<decorator:head />
</head>
<body>
	<page:applyDecorator name="cucUserHeader" />
	<div id="main">
		<div id="left">
			<div class="left_member_title">
				<img src="${ctx}/static/img/left_title_pic_default.jpg" width="42"
					height="45" alt="rockontrol" align="middle" /><span><a
					href="#">${curUserShowName}</a></span>
			</div>
			<div class="left_text">
				<div class="left_member_list">
					<h2>
						<a href="#">企业用户</a>
					</h2>
					<ul>						
						<li class="p2"><a href="#">编辑我的个人档案</a></li>
					</ul>
				</div>
				<div class="left_nav">
					<page:applyDecorator name="cucUserMenu" />
				</div>
			</div>
		</div>
		<div id="right">
			<div id="content">
				<decorator:body />
			</div>
			<div class="clear"></div>
		</div>
		<div class="clear"></div>
	</div>
	<page:applyDecorator name="cucUserFooter" />
</body>
</html>
