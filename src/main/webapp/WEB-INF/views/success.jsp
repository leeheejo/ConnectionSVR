<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page session="true"%>
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
	src="https:////code.jquery.com/jquery-1.12.4.js"></script>

<script
	src="https://cdn.datatables.net/1.10.15/js/jquery.dataTables.min.js"></script>

<title>Main Page</title>
</head>
<style type="text/css">
@media screen and (max-width: 400px) {

	img {
		width: 80%;
		height: 42px;
	}
}

@media screen and (min-width: 401px) and (max-width: 800px) {

	img {
		width: 90%;
		height: 42px;
	}
}

@media screen and (min-width: 801px) {

	img {
		width: 300px;
		height: 42px;
	}
}
</style>
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

	function sendAction(dId, state, cmpCode) {

		var allData = {
			"dId" : dId,
			"state" : state,
			"cmpCode" : cmpCode
		};

		$.ajax({
			url : "sendActionTest",
			dataType : "json",
			data : allData,
			type : "get",
			success : function() {
				return false;
			},
			error : function(request, status, error) {
				return false;
			}

		});

	}
	
	function allOff() {

		$.ajax({
			url : "allOff",
			dataType : "json",
			type : "get",
			success : function() {
				return false;
			},
			error : function(request, status, error) {
				return false;
			}

		});

	}
	
	function allOn() {

		$.ajax({
			url : "allOn",
			dataType : "json",
			type : "get",
			success : function() {
				return false;
			},
			error : function(request, status, error) {
				return false;
			}

		});

	}
	
	$(document).ready(function() {
		$.ajax({
			url : "thread",
			dataType : "text",
			type : "get",
			success : function(data) {
				document.location.reload();
			},
			error : function(request, status, error) {
			}

		});

	});
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
			<div class="container">
				<button type="button" class="btn btn-info" onclick='allOff()'
					style="width: 49%">외출</button>
				<button type="button" class="btn btn-info" onClick='allOn()'
					style="width: 49%">귀가</button>
				<!--
				<button type="button" class="btn btn-info btn-sm "
					onClick="location.href='colorLoop'">colorLoop</button>
					 -->
				<br> <br>
			</div>
			<table class="table" id="example">
				<thead>
					<tr>
						<!-- <th style="width: 1%">code</th>  -->
						<th style="width: 80%">기기명</th>
						<!--  <th>dId</th>  -->
						<th style="width: 10%">상태</th>
						<th style="width: 10%">제어</th>
					</tr>
				</thead>

				<c:choose>
					<c:when test="${fn:length(deviceList) > 0}">
						<c:forEach items="${deviceList}" var="row">
							<tr
								onmouseover="javascript:changeTrColor(this, '#FFFFFF', '#F4FFFD')">

								<!-- <td>${row.cmpCode}</td> -->
								<c:set value="${row.cmpCode}" var="cmpCode" />
								<c:choose>
									<c:when test="${cmpCode == 1 || cmpCode == 4}">
										<td
											onclick="location.href='deviceDetail?dId=${row.dId}&state=${row.state}&name=${row.name}&cmpCode=${row.cmpCode}'">${row.name}</td>

									</c:when>
									<c:otherwise>
										<td>${row.name}</td>
									</c:otherwise>
								</c:choose>

								<c:set value="${row.state}" var="state" />
								<c:choose>
									<c:when test="${state == 0}">
										<td><span class="label label-default">OFF</span></td>
									</c:when>
									<c:when test="${state == 1}">
										<td><span class="label label-primary">ON</span></td>
									</c:when>
								</c:choose>

								<td>
									<button type="button" class="btn btn-success btn-sm" id="send"
										value="send"
										onclick='sendAction("${row.dId}",${row.state},${row.cmpCode})'>
										<span class="glyphicon glyphicon-send"></span>
									</button>
								</td>

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
				onClick="location.href='test'" id="subscription" name="subscription">
				<span class="glyphicon glyphicon-plus"></span>
			</button>
			<!--  
			<button type="button" class="btn btn-default btn-md"
				onClick="location.href='success'" id="refresh" name="refresh">
				<span class="glyphicon glyphicon-refresh"></span>
			</button>
			
			-->
			<button type="button" class="btn btn-default btn-md"
				onClick="location.href='createGroup'" id="createGroup"
				name="createGroup">
				<span class="glyphicon glyphicon-link"></span>
			</button>
			<button type="button" class="btn btn-info btn-md"
				onClick="location.href='addDevice'">
				<span class="glyphicon glyphicon-plus"></span>
			</button>
		</div>
	</center>

</body>
</html>