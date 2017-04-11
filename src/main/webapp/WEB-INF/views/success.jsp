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
<c:set var="oauthUrl" value="${oauthUrl}"></c:set>
<c:if test="${!empty oauthUrl and empty sessionScope.ACCESS_TOKEN}">
			window.open('${oauthUrl}',"artikLogin", "width=500, height=1000, scrollbars=yes, resizable=yes");
		</c:if>
<c:if test="${!empty oauthUrl and !empty sessionScope.ACCESS_TOKEN}">
			alert('already done');
		</c:if>

<title>Main Page</title>
</head>
<body>
<br>
<br>
	<center>
		<div class="container">
			<h2>
				<%=session.getAttribute("userLoginInfo")%>'s Devices
				<button type="button" class="btn btn-info "
					onClick="location.href='addDevice'">
					<span class="glyphicon glyphicon-plus"></span>
				</button>
			</h2>
			<div class="container">
				<button type="button" class="btn btn-info btn-sm"
					onClick="location.href='allOff'">외출</button>
				<button type="button" class="btn btn-info btn-sm "
					onClick="location.href='allOn'">귀가</button>
				<br> <br>
			</div>
			<table class="table" tyle="width: 300px">
				<tr>
				</tr>

				<thead>
					<tr>
						<th>name</th>
						<th>dId</th>
						<th>state
							<button type="button" class="btn btn-default btn-sm"
								onClick="location.href='refresh'">
								<span class="glyphicon glyphicon-refresh"></span>
							</button>
						</th>
						<th>sendAction</th>
					</tr>
				</thead>
				<c:choose>
					<c:when test="${fn:length(deviceList) > 0}">
						<c:forEach items="${deviceList}" var="row">
							<tr>
								<form action="sendActionTest" method="GET">
									<td>${row.name}</td>
									<td>${row.dId}</td>
									<c:set value="${row.state}" var="state" />
									<c:choose>
										<c:when test="${state == 0}">
											<td><span class="label label-default">OFF</span></td>

										</c:when>
										<c:when test="${state == 1}">
											<td><span class="label label-primary">ON</span></td>
										</c:when>
									</c:choose>
									<input type="hidden" name="state" value='${row.state}' /> <input
										type="hidden" name="dId" value='${row.dId}' />
									<td><input type="submit" class="btn btn-success"
										value="send"></td>

								</form>
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

	<button type="button" class="btn btn-warning"
		onClick="location.href='tcpIpTest'">
		<span class="glyphicon glyphicon-arrow-right"></span>
	</button>

</body>
</html>