<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page session="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
	src="https://code.jquery.com/jquery-1.9.1.js"></script>
<link rel="stylesheet" href="resources/main.css" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
</head>
<style type="text/css">
@media screen and (max-width: 400px) {
	img {
		width: 80%;
		height: 42px;
	}
	.Screen {
		width: 90%;
	}
}

@media screen and (min-width: 401px) and (max-width: 800px) {
	img {
		width: 100%;
		height: 42px;
	}
}

@media screen and (min-width: 801px) {
	img {
		width: 600px;
		height: 82px;
	}
	.Screen {
		width: 600px;
	}
}

div {
	padding: 3px;
	margin: 3px;
}
</style>
<body onbeforeunload="success">
	<center>
		<div class="container" align="center">
			<img src="<c:url value="/resources/logo.png"/>" height="32"
				width="78%" alt="" onClick="location.href='success'" />
			<button type="button" class="btn btn-default btn-sm "
				onClick="location.href='logout'">
				<span class="glyphicon glyphicon-off"></span>
			</button>
		</div>
		<br>
		<div class="container Screen panel panel-default ">
			<div class="form-group panel-heading">서버 목록</div>
			<table style="width: 100%" align="center">
				<c:choose>
					<c:when test="${fn:length(connectedCompanyList) > 0}">
						<c:forEach items="${connectedCompanyList}" var="row">
							<form action="selectCompany" method="GET">
								<input type="hidden" name="cmpCode" value='${row.cmpCode}' />
								<tr align="center">
									<td align="center">
										<button class="btn btn-default btn-lg btn-block" type="submit"
											style="width: 100%">${row.cmpName}</button>
									</td>
								</tr>
							</form>
						</c:forEach>
						<!-- 
						<tr align="center">
							<td align="center">
								<button class="btn btn-default btn-lg btn-block"
									onClick="location.href='philipsHueLogin'" style="width: 100%">Phillips Hue</button>
							</td>
						</tr>
						 -->
					</c:when>
					<c:otherwise>
						<tr>
							<td colspan="4">조회된 결과가 없습니다.</td>
						</tr>
					</c:otherwise>
				</c:choose>
			</table>

		</div>
	</center>
</body>
</html>