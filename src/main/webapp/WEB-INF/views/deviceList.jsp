<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page session="true"%>

<html>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR" charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script type="text/javascript" src="http://code.jquery.com/jquery-1.9.1.js"></script>

<script type="text/javascript">
	<c:set var="oauthUrl" value="${oauthUrl}"></c:set>
		<c:if test="${!empty oauthUrl and empty sessionScope.ACCESS_TOKEN}">
			window.open('${oauthUrl}',"artikLogin", "width=500, height=1000, scrollbars=yes, resizable=yes");
		</c:if>
		<c:if test="${!empty oauthUrl and !empty sessionScope.ACCESS_TOKEN}">
			alert('이미 로그인이 되어 있습니다XD');
		</c:if>
</script>
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
					<c:when test="${fn:length(artikDeviceList) > 0}">
						<c:forEach items="${artikDeviceList}" var="row">
							<tr>
								<td>${row.name}</td>
								<td><form action="insertDevice" method="GET">
										<input type="hidden" name="dtId" value='${row.dtid}' />
										<input type="hidden" name="name" value='${row.name}' /> 
										<input type="hidden" name="dId" value='${row.id}' /> 
										<input type="submit" class="btn btn-success" value="추가"/>
									</form>
								</td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr align="center">
							<td colspan="3">
								<div class="container" align="center">
									<small>기기 목록 가져오기</small>
									<button type="button" class="btn btn-info" onClick="location.href='getArtikDeviceList'">
										<span class="glyphicon glyphicon-download-alt"></span>
									</button>
								</div>
							</td>
						</tr>
					</c:otherwise>
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
			
			<!-- Modal -->
			<div class="modal fade" id="myModal" role="dialog">
				<div class="modal-dialog modal-sm">

					<!-- Modal content-->
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal">&times;</button>
							<h4 class="modal-title">새 기기 추가</h4>
						</div>

						<form name="addNewDevice" method="post" action="addNewDevice">
							<div class="modal-body">
								<!-- <form method="get"> -->
								<div class="form-group">
									<input type="text" name="dtId" class="form-control" placeholder="dtId" value="dt82c0c882f8d547f9988065b2a850062a">
								</div>
								<br>
								<div class="form-group">
									<input type="text" name="name" class="form-control" placeholder="name">
								</div>
								<br>
							</div>
							<div class="modal-footer">
								<button type="submit" class="btn btn-primary">submit</button>
								<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</center>
</body>
</html>