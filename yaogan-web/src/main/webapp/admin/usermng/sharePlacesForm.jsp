<%@ page contentType="text/html; charset=utf-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/common/head.jsp"%>
<style type="text/css">
#sharedPlacesDiv div {
	margin-left: 5px;
}

#toSharePlacesDiv div {
	margin-left: 5px;
}
</style>
<script type="text/javascript">
	require([ "dojo/parser", "dijit/TitlePane", "dijit/form/CheckBox",
			"dijit/layout/ContentPane" ]);
	function unsharePlaces() {
		var userId = <c:out value="${editingUser.id}" />;
		var placeIdArr = [];
		require([ "dojo/query", "dojo/NodeList-dom" ], function(query) {
			query("input:checked", "sharedPlacesDiv").forEach(function(node) {
				var w = dijit.getEnclosingWidget(node);
				placeIdArr.push(w.value);
			});
		});
		var placeIds = placeIdArr.join(",");
		var apiUrl = "${ctx}/api/places/" + placeIds + "/unshare/user/"
				+ userId + ".json";
		require([ "dojo/_base/xhr" ], function(xhr) {
			xhr.get({
				url : apiUrl,
				handleAs : "json",
				load : function(jsonData) {
					document.location.replace(document.location.href);
				},
				error : function() {
					alert("bad");
				}
			});
		});
	}

	function sharePlaces() {
		var userId = <c:out value="${editingUser.id}" />;
		var placeIdArr = [];
		require([ "dojo/query", "dojo/NodeList-dom" ], function(query) {
			query("input:checked", "toSharePlacesDiv").forEach(function(node) {
				var w = dijit.getEnclosingWidget(node);
				placeIdArr.push(w.value);
			});
		});
		var placeIds = placeIdArr.join(",");
		var apiUrl = "${ctx}/api/places/" + placeIds + "/share/user/" + userId
				+ ".json";
		require([ "dojo/_base/xhr" ], function(xhr) {
			xhr.get({
				url : apiUrl,
				handleAs : "json",
				load : function(jsonData) {
					document.location.replace(document.location.href);
				},
				error : function() {
				}
			});
		});
	}
</script>
</head>
<body class="claro">
	<div id="middle2">
		<div id="messageDiv"></div>
		<div id="sharedPlacesDiv" data-dojo-type="dijit.TitlePane"
			data-dojo-props="title: '已分配权限矿区'">
			<c:forEach var="place" items="${sharedPlaces}" varStatus="status">
				<input id="sharedPlace${status.index}" name="sharedPlaceId"
					data-dojo-type="dijit.form.CheckBox" value="${place.id}" />
				<label for="sharedPlace${status.index}">${place.name}</label>
			</c:forEach>
			<div style="height: 10px;"></div>
			<c:if test="${not empty sharedPlaces}">
				<div>
					<button data-dojo-type="dijit.form.Button" type="button">
						撤销权限
						<script type="dojo/on" data-dojo-event="click"
							data-dojo-args="evt">unsharePlaces();</script>
					</button>
				</div>
			</c:if>
			<c:if test="${empty sharedPlaces}">
		还没有分配任何矿区给此用户
		</c:if>
		</div>
		<br />
		<br />
		<br />
		<div id="toSharePlacesDiv" data-dojo-type="dijit.TitlePane"
			data-dojo-props="title: '待分配权限矿区'">
			<c:forEach var="place" items="${toSharePlaces}" varStatus="status">
				<input id="toSharePlace${status.index}" name="toSharePlaceId"
					data-dojo-type="dijit.form.CheckBox" value="${place.id}" />
				<label for="toSharePlace${status.index}">${place.name}</label>
			</c:forEach>
			<div style="height: 10px;"></div>
			<div>
				<button data-dojo-type="dijit.form.Button" type="button">
					分配权限
					<script type="dojo/on" data-dojo-event="click" data-dojo-args="evt">sharePlaces();</script>
				</button>
			</div>
		</div>
	</div>
</body>
</html>
