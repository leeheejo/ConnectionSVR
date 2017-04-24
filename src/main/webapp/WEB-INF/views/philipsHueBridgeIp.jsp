<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
<script	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
	<br>
	<center>
		<div class="container" style="width: 100%">
			<form name="bridgeIp" method="post" action="bridgeIp"
				style="width: 100%">
				<!-- <form method="get"> -->
				<div class="form-group">
					<input type="text" name="bridgeIp" class="form-control"
						placeholder="bridgeIp" value ="192.168.101.20">
				</div>
				<div>
					<button type="submit" class="btn btn-primary">submit</button>
				</div>
			</form>
		</div>
	</center>
</body>
</html>