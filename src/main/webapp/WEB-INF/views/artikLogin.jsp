<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>

<c:set var="oauthUrl" value="${oauthUrl}"></c:set>

<script type="text/javascript"
	src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script type="text/javascript">
<c:if test="${!empty oauthUrl and empty sessionScope.ACCESS_TOKEN}">
	window.open('${oauthUrl}',"artikLogin", "width=500, height=1000, scrollbars=yes, resizable=yes");
</c:if>
</script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
</head>
<body>
	<form name="LoginForm1" action="${pageContext.request.contextPath}/deviceList" method="post"></form>
</body>
</html>