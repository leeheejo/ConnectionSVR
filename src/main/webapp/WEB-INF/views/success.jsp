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

	$(document)
			.ready(
					function() {
						$('#example')
								.dataTable(
										{
											"ajax" : "getDeviceListAjax",
											"paging" : false,
											"searching" : false,
											"ordering" : false,
											"info" : false,
											"columnDefs" : [
													{
														"render" : function(
																data, type, row) {
															return row.name;
														},
														"targets" : 0
													},
													{
														"render" : function(
																data, type, row) {
															if (row.state == 0)
																return '<span class="label label-default">OFF</span>';
															else
																return '<span class="label label-primary">ON</span>';
														},
														"targets" : 1
													},
													{
														"render" : function(
																data, type, row) {
															return '<form action="sendActionTest" method="GET">'
																	+ '<input type="hidden" name="dId" value="'+row.dId+'"/>'
																	+ '<input type="hidden" name="cmpCode" value="'+row.cmpCode+'"/>'
																	+ '<input type="hidden" name="state" value="'+row.state+'"/>'
																	+ '<button type="submit" class="btn btn-success btn-sm" id="send" value="send">'
																	+ '<span class="glyphicon glyphicon-send">'
																	+ '</span>'
																	+ '</button>'
																	+ '</form>';
														},
														"targets" : 2
													},
													{
														"render" : function(
																data, type, row) {

															return '<form action="deleteDevice" method="GET">'
																	+ '<input type="hidden" name="dId" value="'+row.dId+'"/>'
																	+ '<input type="hidden" name="cmpCode" value="'+row.cmpCode+'"/>'
																	+ '<button type= "submit" class="btn btn-danger btn-sm">'
																	+ '<span class="glyphicon glyphicon-trash">'
																	+ '</span>'
																	+ '</button>'
																	+ '</form>';
														},
														"targets" : 3
													} ]

										});

						setTimeout(function(){
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
						}, 2000);
						

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