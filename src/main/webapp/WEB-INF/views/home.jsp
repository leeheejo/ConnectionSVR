<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page session="true"%>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<meta name="viewport"
	content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="https:////code.jquery.com/jquery-1.12.4.js"></script>

<script
	src="https://cdn.datatables.net/1.10.15/js/jquery.dataTables.min.js"></script>
<link rel="stylesheet" href="resources/main.css" />
<title>Main Page</title>
</head>
<script type="text/javascript">
	function erchk() {
		if (document.getElementById("uId").value == ""
				|| document.getElementById("uPwd").value == "") {
			alert('ID/Password를 입력하세요:D');
		} else
			document.LoginForm.submit();
	}
</script>
<style type="text/css">
@media screen and (max-width: 400px) {
	form {
		width: 100%;
	}
	img {
		width: 90%;
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
		<div class="container">
			<div>
				<img src="<c:url value="/resources/logo.png"/>" alt="" />
			</div>
			<br>
			<div class="container">
				<form id="LoginForm" name="LoginForm" method="post" action="login">
					<div class="form-group">
						<input type="text" class="form-control" id="uId" name="uId"
							placeholder="ID" />
					</div>
					<div class="form-group">
						<input type="password" class="form-control" id="uPwd" name="uPwd"
							placeholder="Password" />
					</div>
					<br>
					<div>
						<input type="button" class="btn btn-primary"
							class="btn btn-primary" class="btn btn-default" onclick="erchk()"
							value="LOGIN" style="width: 49%"> <input type="button"
							class="btn btn-default" value="JOIN"
							onClick="location.href='joinPage'" style="width: 49%" />
					</div>

				</form>
			</div>
		</div>
	</center>
</body>

</html>

