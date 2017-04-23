<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page session="true"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

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
<style type="text/css">
#center {
	position: absolute;
	top: 30%;
	width: 100%;
}
</style>

<body>
	<center>
		<div class="container" id="center">
			<h2>ERROR</h2>

			<c:set value="${userLoginInfo}" var="isLogin" />
			<c:choose>
				<c:when test="${isLogin == null}">
					<p>
						<h8>단지 서버에 등록 정보가 없습니다.<br></h8>
						<h8>로그인 페이지로 돌아가기</h8>
					</p>
					<button type="button" class="btn btn-default btn-md "
						onClick="location.href='home'">
						<span class="glyphicon glyphicon-repeat"></span>
					</button>

				</c:when>
				<c:otherwise>
					<p>
						<h8>메인 페이지로 돌아가기</h8>
					</p>
					<button type="button" class="btn btn-default btn-md "
						onClick="location.href='success'">
						<span class="glyphicon glyphicon-repeat"></span>
					</button>

				</c:otherwise>
			</c:choose>

		</div>
	</center>
</body>
</html>