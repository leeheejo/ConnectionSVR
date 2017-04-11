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
	src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script type="text/javascript">
	
</script>
<script type="text/javascript">

	<c:set var="oauthUrl" value="${oauthUrl}"></c:set>
		<c:if test="${!empty oauthUrl and empty sessionScope.ACCESS_TOKEN}">
			window.open('${oauthUrl}',"artikLogin", "width=500, height=1000, scrollbars=yes, resizable=yes");
		</c:if>
		<c:if test="${!empty oauthUrl and !empty sessionScope.ACCESS_TOKEN}">
			alert('already done');
		</c:if>
</script>

<body>
	<br>
	<br>
	<center>
		<div class="container">
			<h2>
				Device List
				<button type="button" class="btn btn-info"
					onClick="location.href='getArtikDeviceList'">
					<span class="glyphicon glyphicon-download-alt"></span>
				</button>
			</h2>
			<table class="table" tyle="width: 300px" align="center">
				<thead>
					<tr>
						<th>name</th>
						<th>dId</th>
						<th>select</th>
					</tr>
				</thead>
				<c:choose>
					<c:when test="${fn:length(artikDeviceList) > 0}">
						<c:forEach items="${artikDeviceList}" var="row">
							<tr>
								<td>${row.name}</td>
								<td>${row.id}</td>
								<td><form action="insertDevice" method="GET">
										<input type="hidden" name="dtId" value='${row.dtid}' /> <input
											type="hidden" name="name" value='${row.name}' /> <input
											type="hidden" name="dId" value='${row.id}' /> <input
											type="submit" class="btn btn-success" value="select">
									</form></td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr align="center">
							<td colspan="2">조회된 결과가 없습니다.</td>
						</tr>
					</c:otherwise>
				</c:choose>

			</table>
		</div>
	</center>
</body>
</html>