<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<script>
	function erchk() {
		if (document.getElementById("uId").value == ""
				|| document.getElementById("uPwd").value == "") {
			alert('ID/Password를 입력하세요:D');
		} else
			document.LoginForm.submit();
	}
</script>
<style type="text/css">
#center {
	position: absolute;
	top: 30%;
	width: 100%;
}
</style>

<body>
	<center>
		<div id="center">
			<div>
				<img src="<c:url value="/resources/logo.png"/>" height="42" width="90%" alt="" />
			</div>
			<br>
			<div class="container">
				<form name="LoginForm" method="post" action="login"
					style="width: 100%">
					<div class="form-group">
						<input type="text" class="form-control" id="uId" name="uId" placeholder="ID"/>
					</div>
					<div class="form-group">
						<input type="password" class="form-control" id="uPwd" name="uPwd" placeholder="Password"/>
					</div>
					<br>
					<div>
						<input type="button" class="btn btn-primary" class="btn btn-primary" class="btn btn-default" onclick="erchk()" value="LOGIN" style="width: 49%"> 
						<input type="button" class="btn btn-default" value="JOIN" onClick="location.href='joinPage'" style="width: 49%"/>
					</div>
				</form>
			</div>
		</div>
	</center>

</body>

</html>

