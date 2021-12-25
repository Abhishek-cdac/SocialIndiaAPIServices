<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%
String txnresponse = request.getParameter("txn_response");
String pgdetail = request.getParameter("pg_details");
String frauddetail = request.getParameter("fraud_details");
String otherdetail = request.getParameter("other_details");

%>
<form method="post" name="finalform" id="finalform" action='http://139.59.25.167:8080/paymenttest/getresponse.jsp'>
<input type="hidden" name="txn_response" id="txn_response" value='<%=txnresponse%>'/>
<input type="hidden" name="pg_details" id="pg_details" value='<%=pgdetail%>'/>
<input type="hidden" name="fraud_details" id="fraud_details" value='<%=frauddetail%>'/>
<input type="hidden" name="other_details" id="other_details" value='<%=otherdetail%>'/>
<input type="submit" name="subn" id="subn" value="submit" style="display: none;"/>
</form>
<script type="text/javascript">
//alert(document.getElementById('finaldata').value);
document.getElementById('finalform').submit();
//document.getElementById('fFrm').submit();
</script>
</body>
</html>