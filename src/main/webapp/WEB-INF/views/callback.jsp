<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script type="text/javascript"
	src="https://code.jquery.com/jquery-1.9.1.js"></script>
<script type="text/javascript">
	var doc = window.opener.document;
	var theForm = doc.getElementById("LoginForm");
	opener.location.reload();
	self.close();
</script>