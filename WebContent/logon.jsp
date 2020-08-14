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
		String username=(String)session.getAttribute("username");
		String password=(String)session.getAttribute("password");
		String actionUrl=(String)session.getAttribute("actionurl");
		String err=(String)session.getAttribute("err");
		if(err==null)err="0";			
		
	%>
	
	<form action="<%= actionUrl %>" method="POST" name="logonForm" ENCTYPE="application/x-www-form-urlencoded">
      	<div>
        <input id="username" name="username" type="hidden" value="<%= username %>"/>
        <input id="password" name="password" type="hidden" value="<%= password %>"/>
        <input id="err" name="err" type="hidden" value="<%=err %>"/>
        <input id="longitude" name="longitude" type="hidden"  />
        <input id="latitude" name="latitude" type="hidden"/>
        
    	</div>
    </form>
    <script>
	    if (navigator.geolocation) {
	        navigator.geolocation.getCurrentPosition(showPosition,showError);
	    } else { 
	    	document.getElementById("longitude").value="0";
	    	document.getElementById("latitude").value="0"
	    	logonForm.submit();
	    }
	    function showPosition(position){
			document.getElementById("longitude").value=position.coords.longitude;
	    	document.getElementById("latitude").value=position.coords.latitude;
	    	logonForm.submit();
	    		    	
		}
	    function showError(error) {
	    	document.getElementById("longitude").value="0";
	    	document.getElementById("latitude").value="0"
	    	logonForm.submit();
	    }
	    //document.onreadystatechange = function(){
	    //    if(document.readyState === 'complete'){
	    //    	setTimeout(function(){
	        		 //your code here
	    //    		logonForm.submit();
	    //   		}, 3000);
	        	//
	    //    }
	    //}
    </script>
</body>

</html>