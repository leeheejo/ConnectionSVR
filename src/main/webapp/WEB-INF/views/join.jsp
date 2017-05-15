<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<meta name="viewport"
	content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<link rel="stylesheet" href="resources/main.css" />
<style type="text/css">
@media screen and (max-width: 400px) {
	form {
		width: 100%;
	}
	img {
		width: 100%;
		height: 42px;
	}
}

@media screen and (min-width: 401px) and (max-width: 800px) {
	form {
		width: 100%;
	}
	img {
		width: 90%;
		height: 42px;
	}
}

@media screen and (min-width: 801px) {
	form {
		width: 300px;
	}
	img {
		width: 300px;
		height: 4s2px;
	}
}
</style>
<body>
<br><br><br><br><br><br>
	<center>
		<div class="container" id="center">
			<div>
				<img src="<c:url value="/resources/logo.png"/>" height="42"
					width="90%" alt="" />
			</div>
			<br>
			<form name="JoinForm" method="post" action="join" style="width: 100%">
				<!-- <form method="get"> -->
				<div class="form-group">
					<input type="text" name="uId" class="form-control" placeholder="ID">
				</div>
				<br>
				<div class="form-group">
					<input type="password" name="uPwd" class="form-control"
						placeholder="Password">
				</div>
				<br>
				<div class="form-group">
					<input type="text" name="uEmail" class="form-control"
						placeholder="Email">
				</div>
				<div>
					<button type="submit" class="btn btn-primary btn-block">submit</button>
				</div>
			</form>
		</div>
	</center>
</body>
</html>