<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>

	<center>
		<br>
		<h2>JOIN</h2>
		<br>

		<div class="container">
			<form name="JoinForm" method="post" action="join"
				style="width: 300px">
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
					<button type="submit" class="btn btn-primary">submit</button>
				</div>
			</form>
		</div>
	</center>
</body>
</html>