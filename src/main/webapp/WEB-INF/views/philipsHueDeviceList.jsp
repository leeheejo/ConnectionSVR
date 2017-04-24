<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page session="true"%>

<html>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR"
	charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="http://code.jquery.com/jquery-1.9.1.js"></script>

<body>
	<center>
		<br>
		<div class="container" align="center">
			<img src="<c:url value="/resources/logo.png"/>" height="32" width="78%" alt="" onClick="location.href='success'" />
			<button type="button" class="btn btn-default btn-sm " onClick="location.href='logout'">
				<span class="glyphicon glyphicon-off"></span>
			</button>
		</div>
		<br>
	<div class="container">
		<table class="table" style="width: 100%" align="center">
			<thead>
				<tr>
					<th>기기명</th>
					<th style="width: 10%"></th>
				</tr>
			</thead>
			<c:choose>
				<c:when test="${fn:length(philipsHueDeviceList) > 0}">
					<c:forEach items="${philipsHueDeviceList}" var="row">
						<tr>
							<td>${row.name}</td>
							<td><form action="insertDevice" method="GET">
									<input type="hidden" name="cmpCode" value='${row.cmpCode}' />
									<input type="hidden" name="dtId" value='${row.dtId}' /> <input
										type="hidden" name="name" value='${row.name}' /> <input
										type="hidden" name="dId" value='${row.dId}' /> <input
										type="submit" class="btn btn-success" value="추가" />
								</form></td>
						</tr>
					</c:forEach>
				</c:when>

			</c:choose>
			<!-- 
				<tr align="right">
					<td colspan="3">
						<button type="button" class="btn btn-info " data-toggle="modal"
							data-target="#myModal">
							<span class="glyphicon glyphicon-plus"></span>
						</button>
					</td>
				</tr>
				-->
		</table>
	</div>
	</center>

</body>
</html>