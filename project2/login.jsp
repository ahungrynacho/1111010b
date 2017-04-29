<%@ page import = "project2.*" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
	<body>
		<h3 align = "center"> Login Page </h3>
			<c:if test="${FAIL}">
				<b>Invalid email or password try again!</b>
			</c:if>
			<form action = "FabflixControllerServlet" method = "post">
				<br>
				Email: <input type = "text" name = "email" />
	
				<br>
	
				Password:<input type = "password" name = "password" />
				
				<input type="submit" name="command" value="login"/>
			</form>

			

	</body>



</html>