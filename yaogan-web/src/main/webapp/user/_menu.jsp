<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="com.rockontrol.yaogan.util.Functions"%>
<%@ include file="/common/tag.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%
   String servletPath = request.getServletPath();
   String style = "style=\"background-color: #a9d54d\"";
   String layersStyle = "";
   String statsStyle = "";
   if (Functions.matchAntPath("/user/layers", servletPath)
         || Functions.matchAntPath("/user/layers/**", servletPath)) {
      layersStyle = style;
   } else if (Functions.matchAntPath("/user/envstats/**", servletPath)) {
      statsStyle = style;
   }
%>
<ul>
	<li <%=layersStyle%>><img src="${ctx}/static/img/menu_icon_1.png"
		width="16" height="16"><a href="${ctx}/user/layers">地图展示</a>
	</li>
	<li <%=statsStyle%>><img src="${ctx}/static/img/menu_icon_2.png"
		width="16" height="16"><a
		href="${ctx}/user/envstats/timeCompare">统计分析</a>
	</li>
</ul>