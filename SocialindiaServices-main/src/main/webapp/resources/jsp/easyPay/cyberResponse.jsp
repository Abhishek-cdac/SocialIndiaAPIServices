<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Cyberplate Response</title>
</head>
<body>
<form method="post" name="cypermRespName" id="cypermRespName" action="<s:property value="redirectUrl"/><s:property value="responseData"/>" style="display:block;">

<input type="submit" name="subnbut" id="subnbut" value="submit" style="display: none"/>
</form>
<script type="text/javascript">
document.getElementById('cypermRespName').submit(); 
</script>
</body>
</html>