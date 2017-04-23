<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page session="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script type="text/javascript" src="http://code.jquery.com/jquery-1.9.1.js"></script>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
</head>
<body>
	<br>
	<center>
		<div class="container" align="center">
			<img src="<c:url value="/resources/logo.png"/>" height="32" width="78%" alt="" onClick="location.href='success'"  />
			<button type="button" class="btn btn-default btn-sm "onClick="location.href='logout'">
				<span class="glyphicon glyphicon-off"></span>
			</button>
		</div>
		<br>
		<div class="container">
			<div class="input-group">
				<span class="input-group-addon">
					<i class="glyphicon glyphicon-search"></i>
				</span>
				<input id="search" type="text" class="form-control" name="search" placeholder="Search">
			</div>
			<br>
			<table style="width: 100%" align="center">
				<c:choose>
					<c:when test="${fn:length(connectedCompanyList) > 0}">
						<c:forEach items="${connectedCompanyList}" var="row">
							<tr align="center">
								<td align="center">
									<button class="btn btn-default btn-lg btn-block" onClick="location.href='artikLogin'" style="width: 100%">${row.cmpName}</button>
								</td>
							</tr>
						</c:forEach>
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