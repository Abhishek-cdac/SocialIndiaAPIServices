<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<form role="form" id="finalform" name="finalform" method="post" action="<s:property value="actionurl"/>">
<s:hidden name="toStringval" id="toStringval"></s:hidden>
</form>
</body>
<script type="text/javascript">
document.getElementById('finalform').submit();
</script>
</html>