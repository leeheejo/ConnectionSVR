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
<script type="text/javascript"
	src="https:////code.jquery.com/jquery-1.12.4.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-slider/9.8.0/bootstrap-slider.min.js"
	integrity="sha256-Gdq5BxoczjhbEJLjrYKQ4fvBGx/EQrTWjDM2UrdTot0="
	crossorigin="anonymous"></script>

<link rel="stylesheet" href="resources/main.css" />
<script
	src="https://cdn.datatables.net/1.10.15/js/jquery.dataTables.min.js"></script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
</head>
<style type="text/css">
@media screen and (max-width: 400px) {
	img {
		width: 80%;
		height: 42px;
	}
	.Screen {
		width: 90%
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
<script type="text/javascript">

	$(document).ready(function() {
			
					
						var dId = '${dId}';
						var allData = {
							"dId" : dId
						};
					

						if(${cmpCode} == 4) {
							divchange(2);
							$
							.ajax({
								url : "getDidsByGId",
								dataType : "json",
								type : "get",
								data : allData,
								success : function(data) {
									setData(data);
								},
								error : function(request, status, error) {
								}

							});



					
						} else{
							var dId = '${dId}';
							var allData = {
								"dId" : dId
							};
					
						$.ajax({
									url : "getDeviceState",
									dataType : "text",
									type : "get",
									data : allData,
									success : function(data) {

										var obj = jQuery.parseJSON(data);
										var device = obj.data[0];
										var deviceData = device.data;

										if (device.sdtid == "dt6f79b9b4aa3b4a80b7b76c2190016c61") {

											var brightness = deviceData.brightness;
											var colorR = deviceData.colorRGB.r;
											var colorG = deviceData.colorRGB.g;
											var colorB = deviceData.colorRGB.b;

											divchange(0);
											hue(brightness, colorR, colorG,
													colorB);

										} else if (device.sdtid == "dt9ceaf54e65b241ddade6064a5cf7f71e") {
											var sensors = deviceData.sensors;
											var Dust = sensors.Dust[0];
											var FineDust = sensors.FineDust[0];
											var Odor = sensors.Odor[0];
											console.log(deviceData);
											divchange(1);
											airPurifier(Dust, FineDust, Odor);
										}

									},
									error : function(request, status, error) {
									}

								});
						}
						var red = document.getElementById("red");
						var ctx = red.getContext("2d");
						ctx.beginPath();
						ctx.arc(20,10,8,0,2*Math.PI);
						ctx.fillStyle = "red";
						ctx.fill();
						ctx.stroke();
						
						var blue = document.getElementById("blue");
						var ctx = blue.getContext("2d");
						ctx.beginPath();
						ctx.arc(20,10,8,0,2*Math.PI);
						ctx.fillStyle = "blue";
						ctx.fill();
						ctx.stroke();
						
						var green = document.getElementById("green");
						var ctx = green.getContext("2d");
						ctx.beginPath();
						ctx.arc(20,10,8,0,2*Math.PI);
						ctx.fillStyle = "green";
						ctx.fill();
						ctx.stroke();
						
					});


	
	function divchange(flag) {

		if (flag == 0) {
			document.getElementById("hue").style.display = "block";
			document.getElementById("airPurifier").style.display = "none";
			document.getElementById("group").style.display = "none";
		} else if(flag == 1) {
			document.getElementById("airPurifier").style.display = "block";
			document.getElementById("hue").style.display = "none";
			document.getElementById("group").style.display = "none";
		} else if(flag == 2) {
			document.getElementById("group").style.display = "block";
			document.getElementById("hue").style.display = "none";
			document.getElementById("airPurifier").style.display = "none";
		}

	}
	function airPurifier(Dust, FineDust, Odor) {
		$("#Dust").val(Dust);
		$("#FineDust").val(FineDust);
		$("#Odor").val(Odor);
	}
	function hue(Brightness, R, G, B) {
		$("#Brightness").val(Brightness);
		$("#R").val(R);
		$("#G").val(G);
		$("#B").val(B);
	}
	
	function setData(data) {
		console.log(data);
		var devices = '';
		var rows = 0;
		$.each(data, function( index, value ){
			console.log(value.name);
			devices = devices.concat(value.name);
			devices = devices.concat("\n");
			rows ++;
			
		});
		document.getElementById("device").rows = rows;
		$('#device').val(devices);
	}
	
	function colorChange(dId, name, state){
		var color = $("input[type='radio'][name='optradio']:checked").val();
		var R = 0;
		var G = 0;
		var B = 0;
		if(color == 1) {
			R = 50;
		} else if(color == 2) {
			B = 50;
		}else if(color==3){
			G = 50;
		}
		
		$
		.ajax({
			url : "sendActionRGB",
			dataType : "text",
			type : "get",
			data : {"dId" : dId, "name" : name, "state" : state, "actionR" : R, "actionG" :G, "actionB":B},
			success : function(data) {

				location.href ="success";
			},
			error : function(request, status, error) {
			}

		});

	}

</script>
<body>
	<center>
		<div class="container" align="center">
			<img src="<c:url value="/resources/logo.png"/>" height="32"
				width="78%" alt="" onClick="location.href='success'" />
			<button type="button" class="btn btn-default btn-sm "
				onClick="location.href='logout'">
				<span class="glyphicon glyphicon-off"></span>
			</button>
		</div>
		<div class="panel panel-default Screen">
			<div class="panel-heading">
				<br>
				<c:set value="${state}" var="state" />
				<c:choose>
					<c:when test="${state == 0}">
						<span class="label label-default">OFF</span>
					</c:when>
					<c:when test="${state == 1}">
						<span class="label label-primary">ON</span>
					</c:when>
				</c:choose>
				<h3>${name}</h3>
			</div>
			<div id="hue" style="display: none;" class="panel-body">
				<br> <br>
				<h4>Current State</h4>
				<table>
					<tr>
						<td>
							<div class="form-group">
								<label for="Brightness">Brightness</label> <input type=text
									id="Brightness" class="form-control" readonly />
							</div>
						</td>
						<td>
							<div class="form-group ">
								<label for="R">Color R</label> <input type=text id="R"
									class="form-control" readonly />
							</div>
						</td>
						<td>
							<div class="form-group ">
								<label for="G">Color G</label> <input type=text id="G"
									class="form-control" readonly />
							</div>
						</td>
						<td>
							<div class="form-group ">
								<label for="B">Color B</label> <input type=text id="B"
									class="form-control" readonly />
							</div>
						</td>
					</tr>
				</table>

				<br> <br>
				<h4>Change Color</h4>
				<table>
					<tr>
						<td align="center">
							<div class="radio">
								<label><input type="radio" name="optradio" value=1>
									<canvas id="red" width="45" height="45"> </canvas></label> <label><input
									type="radio" name="optradio" value=2> <canvas id="blue"
										width="45" height="45"> </canvas></label> <label><input
									type="radio" name="optradio" value=3> <canvas
										id="green" width="45" height="45" value=3> </canvas></label>
							</div>
						</td>
					<tr>
				</table>
				<button type="button" class="btn btn-primary btn-block"
					onclick="colorChange('${dId}','${name}','${state}')">submit</button>

				<br>

				<form action="deleteDevice" method="GET">

					<input type="hidden" name="dId" value='${dId}' /> <input
						type="hidden" name="cmpCode" value=1 />
					<button type="submit" class="btn btn-default btn-block"
						onClick="location.href='deleteDevice'">delete</button>

				</form>
			</div>

			<div id="airPurifier" style="display: none" class="panel-body">
				<br> <br>
				<h4>Current State</h4>
				<table>
					<tr>
						<td><div class="form-group ">
								<label for="Dust">Dust</label> <input type=text id="Dust"
									class="form-control" readonly />
							</div></td>
						<td><div class="form-group ">
								<label for="FineDust">FineDust</label> <input type=text
									id="FineDust" class="form-control" readonly />
							</div></td>
						<td><div class="form-group ">
								<label for="Odor">Odor</label> <input type=text id="Odor"
									class="form-control" readonly />
							</div></td>
					</tr>
				</table>
				<br>
				<form action="deleteDevice" method="GET">
					<input type="hidden" name="dId" value='${dId}' /> <input
						type="hidden" name="cmpCode" value=1 />
					<button type="submit" class="btn btn-default btn-block"
						onClick="location.href='deleteDevice'">delete</button>
				</form>

			</div>

			<div id="group" style="display: none" class="panel-body">
				<br> <br>
				<h4>Group Component</h4>
				<textarea id="device" class="form-control Screen"
					style="resize: none; overflow: hidden;" readonly></textarea>
				<br>
				<form action="deleteDevice" method="GET">
					<input type="hidden" name="dId" value='${dId}' /> <input
						type="hidden" name="cmpCode" value=4 />
					<button type="submit" class="btn btn-default btn-block Screen"
						onClick="location.href='deleteDevice'">delete</button>
				</form>

			</div>
		</div>
	</center>
</body>
</html>