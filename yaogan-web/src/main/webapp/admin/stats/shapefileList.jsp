<%@page import="com.rockontrol.yaogan.vo.Page"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/common/includes.jsp"%>
<%@page import="org.springframework.web.util.WebUtils"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Collection"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.HashMap"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="${ctx}/static/css/style.css" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Shapefiles</title>
<script type="text/javascript">
	function query() {
		document.getElementById("query_files").submit();
	}
</script>
</head>
<body>
	<div id="middle2">
		<div class="rtlist">
			<div class="map_top">
				<form id="query_files" action="${ctx}/admin/place/fileList"
					method="post">
					<table cellspacing="0" cellpadding="0" border="0">
						<tbody>
							<tr>
								<td width="65">地区名称:</td>
								<td width="155"><input type="text" size="20" name="place"></input></td>
								<td width="65">拍摄年份:</td>
								<td width="90"><input type="text" size="20"
									name="shootTime"></input></td>
								<td width="79"><img width="50" height="24" alt="查询"
									src="${ctx}/static/img/butt_search.gif" onclick="query()"></td>
							</tr>
						</tbody>
					</table>
				</form>
			</div>
			<div class="rtnr">
				<div class="thnr_add">
					<a href="${ctx}/admin/upload/form">上传新矿区数据</a>
				</div>
				<table width="100%" cellspacing="0" cellpadding="0" border="0"
					id="table_one">
					<tbody>
						<tr>
							<th width="32%">地区名称</th>
							<th width="15%">年份</th>
							<th width="15%">文件类型</th>
							<th width="15%">文件名</th>
							<th width="15%">上传时间</th>
						</tr>
						<c:forEach items="${page.items}" var="group">
							<c:forEach items="${group.shootTimeGroupList}" var="stvo"
								varStatus="gsta">
								<c:forEach items="${stvo.fileList}" var="vo" varStatus="tsta">
									<tr>
										<c:if test="${gsta.index eq 0 and tsta.index eq 0}">
											<td rowspan="${group.count}">${group.placeName}</td>
										</c:if>
										<c:if test="${tsta.index eq 0}">
											<td rowspan="${stvo.count}">${vo.shootTime}</td>
										</c:if>
										<td>${ vo.typeString}</td>
										<td>${ vo.fileName}</td>
										<td>${ vo.uploadTime}</td>
									</tr>
								</c:forEach>
							</c:forEach>
						</c:forEach>
					</tbody>
				</table>

				<!-- 分页开始 -->
				<c:if test="${page!=null}">
					<pg:pager url="${actionUrl}" items="${page.totalItemNum}" index="center"
						maxPageItems="${page.pageSize}" maxIndexPages="10" isOffset="true"
						export="offset,currentPageNumber=pageNumber" scope="request">
						<pg:param name="pageSize" value="11"/>
						<pg:index>
							<pg:prev export="pageUrl">&nbsp;<a href="<%=pageUrl%>">
									[Prev]</a>
							</pg:prev>
							<pg:pages>
								<%
									   if (pageNumber.intValue() < 10) {
									%>&nbsp;<%
									   }
									                  if (pageNumber == currentPageNumber) {
									%><b><%=pageNumber%></b>
								<%
									   } else {
									%><a href="<%=pageUrl%>"><%=pageNumber%></a>
								<%
									   }
									%>
							</pg:pages>
							<pg:next export="pageUrl">&nbsp;<a href="<%=pageUrl%>">[Next]</a>
							</pg:next>
							<br>
							</font>
						</pg:index>
					</pg:pager>
				</c:if>
				<!-- 分页结束 -->

			</div>


		</div>
	</div>
</body>
</html>