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
	<table border="1">
		<thead>
			<tr>
				<th>用户名</th>
				<th>邮箱</th>
				<th>电话</th>
				<th>角色</th>
				<th>操作</th>
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
						</c:if>
					</td>
				</tr>
			</c:forEach>
		</thead>
	</table>
</body>
</html>
