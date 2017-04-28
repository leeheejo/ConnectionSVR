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
	src="http:////code.jquery.com/jquery-1.12.4.js"></script>

<script
	src="https://cdn.datatables.net/1.10.15/js/jquery.dataTables.min.js"></script>

<title>Main Page</title>
</head>
<script type="text/javascript">
	function numkeyCheck(e) {
		var keyValue = event.keyCode;
		if (((keyValue >= 48) && (keyValue <= 57)))
			return true;
		else
			return false;
	}

	function changeTrColor(trObj, oldColor, newColor) {
		trObj.style.backgroundColor = newColor;
		trObj.onmouseout = function() {
			trObj.style.backgroundColor = oldColor;
		}
	}
</script>
<script>
	
</script>
<body>
	<center>
		<br>
		<div class="container" align="center">
			<img src="<c:url value="/resources/logo.png"/>" height="32"
				width="78%" alt="" onClick="location.href='success'" />
			<button type="button" class="btn btn-default btn-sm "
				onClick="location.href='logout'">
				<span class="glyphicon glyphicon-off"></span>
			</button>
		</div>
		<br>
		<div class="container">
			<div class="container" style="width: 100%">
				<button type="button" class="btn btn-info"
					onClick="location.href='allOff'" style="width: 49%">외출</button>
				<button type="button" class="btn btn-info"
					onClick="location.href='allOn'" style="width: 49%">귀가</button>
				<!--
				<button type="button" class="btn btn-info btn-sm "
					onClick="location.href='colorLoop'">colorLoop</button>
					 -->
				<br> <br>
			</div>
			<table class="table" id="example" style="width: 100%; margin: auto;"
				data-toggle="modal">
				<thead>
					<tr>
						<!-- <th style="width: 1%">code</th>  -->
						<th style="width: 50%">기기명</th>
						<!--  <th>dId</th>  -->
						<th>상태</th>
						<th>제어</th>
						<th>삭제</th>
					</tr>
				</thead>

				<c:choose>
					<c:when test="${fn:length(deviceList) > 0}">
						<c:forEach items="${deviceList}" var="row">
							<tr
								onmouseover="javascript:changeTrColor(this, '#FFFFFF', '#F4FFFD')">
								<form action="sendActionTest" method="GET">
									<!-- <td>${row.cmpCode}</td> -->
									<td name="name">${row.name}</td>

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
										type="hidden" name="dId" value='${row.dId}' /> <input
										type="hidden" name="cmpCode" value='${row.cmpCode}' />
									<td>
										<button type="submit" class="btn btn-success btn-sm"
											value="send">
											<span class="glyphicon glyphicon-send"></span>
										</button> <!-- 										 <c:set
											value="${row.dtId}" var="dtId" /> 
											
										<c:choose>
											<c:when test="${dtId eq 'dt6f79b9b4aa3b4a80b7b76c2190016c61'}">
												<table>
													<tr>
														<td><input type="text" id="R" name="R"
															placeHolder="R" style="width: 30px;"
															onKeyPress="return numkeyCheck(event)" /></td>
														<td><input type="text" id="G" name="G"
															placeHolder="G" style="width: 30px;"
															onKeyPress="return numkeyCheck(event)" /></td>
														<td><input type="text" id="B" name="B"
															placeHolder="B" style="width: 30px;"
															onKeyPress="return numkeyCheck(event)" /></td>
													</tr>
												</table>
											</c:when>
										</c:choose>

 -->




									</td>
								</form>
								<c:set value="${row.cmpCode}" var="cmpCode" />

								<c:choose>
									<c:when test="${cmpCode eq '0'}">
										<td>&nbsp&nbsp</td>
									</c:when>
									<c:otherwise>
										<td>
											<form action="deleteDevice" method="GET">
												<input type="hidden" name="dId" value='${row.dId}' />
												<button type="submit" class="btn btn-danger btn-sm"
													onClick="location.href='deleteDevice'">
													<span class="glyphicon glyphicon-trash"></span>
												</button>
											</form>
										</td>
									</c:otherwise>
								</c:choose>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr align="center">
							<td colspan="4">조회된 결과가 없습니다.</td>
						</tr>
					</c:otherwise>
				</c:choose>

			</table>
		</div>

		<div class="container" align="right">
			<button type="button" class="btn btn-default btn-md"
				onClick="location.href='success'" id="refresh" name="refresh">
				<span class="glyphicon glyphicon-refresh"></span>
			</button>
			<button type="button" class="btn btn-info btn-md"
				onClick="location.href='addDevice'">
				<span class="glyphicon glyphicon-plus"></span>
			</button>
		</div>
	</center>

</body>
</html>