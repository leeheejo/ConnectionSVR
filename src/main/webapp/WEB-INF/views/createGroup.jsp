<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page session="true"%>

<html>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR"
	charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">s
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="https://code.jquery.com/jquery-1.9.1.js"></script>


<title>Main Page</title>
</head>
<style type="text/css">
@media screen and (max-width: 400px) {

	img {
		width: 80%;
		height: 42px;
	}
}

@media screen and (min-width: 401px) and (max-width: 800px) {
	img {
		width: 90%;
		height: 42px;
	}
}

@media screen and (min-width: 801px) {
	img {
		width: 300px;
		height: 42px;
	}
}
</style>
<script type="text/javascript">
	$(document).ready(
			function() {
				$("#clicker").click(
						function() {
							var select = "";
							var groupName = $('#groupName').val();
							$.each($("input[name='device']:checked"),
									function() {
										select += $(this).val() + ";";
									});
							document.location.href = "insertGroup?dIds="
									+ select + "&name=" + groupName
									+ "&dtId=group&cmpCode=4";
						});
			});
</script>

<body>
	<center>
		<br>
		<div class="container" align="center">
			<img src="<c:url value="/resources/logo.png"/>" height="32"
				width="78%" alt="" onClick="location.href='success'" />
			<button type="button" class="btn btn-default btn-sm "
				onClick="location.href='logout'">
				<span class="glyphicon glyphicon-off"></span>
			</button>
		</div>
		<br>
		<div class="container">
			<div class="form-group">
				<label for="usr">Group Name:</label> <input type="text"
					class="form-control" id="groupName">
			</div>
			<c:choose>
				<c:when test="${fn:length(deviceList) > 0}">
					<c:forEach items="${deviceList}" var="row">
						<div class="checkbox">
							<label><input type="checkbox" value='${row.dId}'
								name="device">${row.name}</label>
						</div>
					</c:forEach>
				</c:when>
			</c:choose>
			<div>
				<button type="button" class="btn btn-primary" id="clicker"
					name="clicker">submit</button>
			</div>

		</div>
	</center>

</body>
</html>