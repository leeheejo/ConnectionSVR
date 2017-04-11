<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>


<script>
	function erchk() {
		if (document.getElementById("uId").value == ""
				|| document.getElementById("uPwd").value == "") {
			alert('enter login info');
		} else
			document.LoginForm.submit();
	}
</script>
<body>
	<center>
	<br>
	<h2>LOGIN</h2>
		<br>
		<div class="container">
			<form name="LoginForm" method="post" action="login"
				style="width: 300px">
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
						value="login"> <input type="button"
						class="btn btn-default" value="join"
						onClick="location.href='joinPage'">
				</div>
			</form>
		</div>
	</center>

</body>

</html>

