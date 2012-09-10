<%@ page contentType="text/html; charset=utf-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/common/head.jsp"%>
<style type="text/css">
</style>
<script type="text/javascript">
	require([ "dijit/form/DropDownButton", "dijit/form/Button" ]);
	function gotoUrl(url) {
		document.location.href = url;
	}
</script>
</head>
<body class="claro">
	<div class="rtlist">
		<div class="map_top">权限管理</div>
		<div class="rtnr">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				id="table_one">
				<tbody>
					<tr>
						<th width="17%">姓名</th>
						<th width="30%">邮箱</th>
						<th width="23%">联系电话</th>
						<th width="15%">状态</th>
						<th width="15%">操作</th>
					</tr>
					<c:forEach var="user" items="${users}">
						<tr>
							<td>${user.realName}</td>
							<td>${user.email}</td>
							<td>${user.mobile}</td>
							<td><c:if test="${user.isAdmin}">管理员</c:if> <c:if
									test="${not user.isAdmin}">普通用户</c:if></td>
							<td><c:if test="${not user.isAdmin}">
									<div data-dojo-type="dijit.form.DropDownButton">
										<span>操作</span>
										<button data-dojo-type="dijit.form.Button" type="button">
											分配矿区权限
											<script type="dojo/on" data-dojo-event="click"
												data-dojo-args="evt">gotoUrl("${ctx}/admin/user/${user.id}/sharePlacesForm");</script>
										</button>
									</div>
								</c:if></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>
