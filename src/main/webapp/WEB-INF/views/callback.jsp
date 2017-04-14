<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script type="text/javascript"
	src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script type="text/javascript">
	var doc = window.opener.document;
	var theForm = doc.getElementById("LoginForm");
	self.close();
	theForm.submit();
	
</script>