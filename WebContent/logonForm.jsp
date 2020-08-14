<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="ngn.otp_admin.utils.OTPProps"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<%
		String username=(String)session.getAttribute("username");
		String password=(String)session.getAttribute("password");
		String destinationUrl=(String)session.getAttribute("destinationUrl");
		String destinationUserNameParameterName=(String)session.getAttribute("destinationUserNameParameterName");
		String destinationPasswordParameterName=(String)session.getAttribute("destinationPasswordParameterName");
		//OTPProps props=new OTPProps();
		//String destination="";
		//String url="";
		//destination=props.getProperty("owa.destination.url");
		//url=props.getProperty("owa.dll.url");
		//System.out.println(url);
		//System.out.println(destination);
	%>
	<form action="<%=destinationUrl %>" method="POST" name="logonForm" ENCTYPE="application/x-www-form-urlencoded">
      	<div> 
      		<input type="hidden" name="loginOp" value="login"/>
      		<input type="hidden" name="flags" value="0"/>
	        <input type="hidden" name="forcedownlevel" value="0"/>
			<input type="hidden" name="fromOTP" value="true"/>
	        <input id="<%=destinationUserNameParameterName %>" name="<%=destinationUserNameParameterName %>" type="hidden" value="<%= username %>"/>
	        <input id="<%=destinationPasswordParameterName %>" name="<%=destinationPasswordParameterName %>" type="hidden" value="<%= password %>"/>
    	</div>
    </form>
    <script>
    	logonForm.submit();
    </script>
</body>
</html>