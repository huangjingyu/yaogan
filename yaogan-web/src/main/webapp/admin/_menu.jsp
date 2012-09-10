<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="com.rockontrol.yaogan.util.Functions"%>
<%@ include file="/common/tag.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%
   String servletPath = request.getServletPath();
   String style = "style=\"background-color: #a9d54d\"";
   String layersStyle = "";
   String statsStyle = "";
   String placeStyle = "";
   String userStyle = "";
   if (Functions.matchAntPath("/admin/layers", servletPath)
         || Functions.matchAntPath("/admin/layers/**", servletPath)) {
      layersStyle = style;
   } else if (Functions.matchAntPath("/admin/envstats/**", servletPath)) {
      statsStyle = style;
   } else if (Functions.matchAntPath("/admin/place", servletPath)
         || Functions.matchAntPath("/admin/place/**", servletPath)) {
      placeStyle = style;
   } else if (Functions.matchAntPath("/admin/user", servletPath)
         || Functions.matchAntPath("/admin/user/**", servletPath)) {
      userStyle = style;
   }
%>
<ul>
	<li <%=placeStyle%>><img src="${ctx}/static/img/menu_icon_3.png"
		width="16" height="16"><a href="${ctx}/admin/place">地图管理</a>
	</li>
	<li <%=layersStyle%>><img src="${ctx}/static/img/menu_icon_1.png"
		width="16" height="16"><a href="${ctx}/admin/layers">地图展示</a>
	</li>
	<li <%=statsStyle%>><img src="${ctx}/static/img/menu_icon_2.png"
		width="16" height="16"><a
		href="${ctx}/admin/envstats/timeCompare">统计分析</a>
	</li>
	<li <%=userStyle%>><img src="${ctx}/static/img/menu_icon_4.png"
		width="16" height="16"><a href="${ctx}/admin/user">权限管理</a>
	</li>
</ul>