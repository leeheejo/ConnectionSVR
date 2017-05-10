<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page session="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<meta name="viewport" content="width=device-width, initial-scale=1">
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

<title>Main Page</title>
</head>
<script type="text/javascript">
	
</script>

<body>
	<center>
		<div class="container">

			<form action="create">
				<input type="text" id="ddid" name="ddid" placeholder="ddid">
				<button type="submit" class="btn btn-info btn-md"
					onClick="location.href='create'">create</button>
			</form>
			<br>

			<form action="delete">
				<input type="text" id="subscriptionID" name="subscriptionID"
					placeholder="subscriptionID">
				<button type="submit" class="btn btn-info btn-md"
					onClick="location.href='delete'">delete</button>
			</form>
			<br>

			<button type="button" class="btn btn-info btn-md"
				onClick="location.href='getlist'">get list</button>
			<br>

			<form action="validate">
				<input type="text" id="subscriptionID" name="subscriptionID"
					placeholder="subscriptionID"> <input type="text" id="aId"
					name="aId" placeholder="aId"> <input type="text" id="nonce"
					name="nonce" placeholder="nonce">
				<button type="submit" class="btn btn-info btn-md"
					onClick="location.href='validate'">validate</button>
			</form>

			<form action="noti">
				<input type="text" id="notificationID" name="notificationID"
					placeholder="notificationID">
				<button type="submit" class="btn btn-info btn-md"
					onClick="location.href='noti'">noti</button>
			</form>
		</div>
	</center>

</body>
</html>