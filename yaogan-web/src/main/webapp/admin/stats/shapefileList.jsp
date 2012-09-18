<%@page import="com.rockontrol.yaogan.vo.Page"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="org.springframework.web.util.WebUtils"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Collection"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.HashMap"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="/common/head.jsp"%>
<title>Shapefiles</title>
<script type="text/javascript">
	function query() {
		document.getElementById("query_files").submit();
	}
	require([ "dijit/form/Select" ]);
	function showAvaTimes(placeId) {
		var placeSelectWidget = dijit.byId("placeId");
		var timeSelectWidget = dijit.byId("shootTime");
		timeSelectWidget.removeOption(timeSelectWidget.getOptions());
		timeSelectWidget.addOption({
			label : '全部时间',
			value : ''
		});
		if (!placeId)
			return;
		require([ "dojo/_base/xhr" ], function(xhr) {
			xhr.get({
				url : "${ctx}/api/place/" + placeId + "/availableTimes.json",
				handleAs : "json",
				load : function(times) {
					var options = [];
					require([ "dojo/_base/array" ], function(arrayUtil) {
						arrayUtil.forEach(times, function(time) {
							var opt = {};
							opt.label = time;
							opt.value = time;
							options.push(opt);
						});
					});
					timeSelectWidget.addOption(options);
				},
				error : function() {
				}
			});
		});
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
								<td width="85">请选择地区:</td>
								<td width="155"><select id="placeId" name="placeId"
									style="margin-bottom: 8px;" data-dojo-type="dijit.form.Select"
									onchange="showAvaTimes(this.value)">
										<option value="">全部地区</option>
										<c:forEach var="place" items="${places}">
											<option value="${place.id}"
												<c:if test="${place.id eq curPlaceId}">selected="selected"</c:if>>${place.name}</option>
										</c:forEach>
								</select></td>
								<td width="65">拍摄年份:</td>
								<td width="90"><select id="shootTime" name="shootTime"
									style="margin-bottom: 8px;" data-dojo-type="dijit.form.Select">
										<option value="">全部时间</option>
										<c:forEach var="shootTime" items="${shootTimes}">
											<option value="${shootTime}"
												<c:if test="${shootTime eq curShootTime}">selected="selected"</c:if>>${shootTime}</option>
										</c:forEach>
								</select></td>
								<td width="79"><img
									style="margin-top: 5px; margin-left: 25px;" width="50"
									height="24" alt="查询" src="${ctx}/static/img/butt_search.gif"
									onclick="query()"></td>
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
							<th width="20%">地区名称</th>
							<th width="10%">年份</th>
							<th width="15%">文件类型</th>
							<th width="35%">文件名</th>
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

				<c:if test="${page!=null}">
					<pg:pager url="${actionUrl}" items="${page.totalItemNum}"
						index="center" maxPageItems="${page.pageSize}" maxIndexPages="10"
						isOffset="true" export="offset,currentPageNumber=pageNumber"
						scope="request">
						<pg:param name="pageSize" value="12" />
						<pg:param name="placeId" value="${placeId}" />
						<pg:param name="shootTime" value="${shootTime}" />
						<pg:index>
							<pg:prev export="pageUrl">&nbsp;<a href="<%=pageUrl%>">
									上一页</a>
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
							<pg:next export="pageUrl">&nbsp;<a href="<%=pageUrl%>">下一页</a>
							</pg:next>
							<br>
							</font>
						</pg:index>
					</pg:pager>
				</c:if>
			</div>
		</div>
	</div>
</body>
</html>