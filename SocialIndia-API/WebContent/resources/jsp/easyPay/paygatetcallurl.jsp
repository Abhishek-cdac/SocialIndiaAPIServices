<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
System.out.println("httpurl------------>"+request.getParameter("payHttpurl"));
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Paygate Payment</title>
</head>
<body>
<form method="post" name="payGateformName" id="payGateformName" action="<s:property value="payHttpurl"/>" style="display:block;">

<input type="submit" name="subnbut" id="subnbut" value="submit" style="display: none"/>
</form>
<script type="text/javascript">
document.getElementById('payGateformName').submit(); 
</script>
</body>
</html>