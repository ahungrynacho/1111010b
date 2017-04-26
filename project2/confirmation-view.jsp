<%@page import="java.util.*, project2.*"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
	<title>Checkout</title>
	<head lang="en"><meta charset="UTF-8"></head>


<body>

	<c:choose>
		<c:when test="${SUCCESS}">
			Credit card approved!
		</c:when>
		
		<c:otherwise>
			Credit card declined!
		</c:otherwise>
	</c:choose>
	
</body>
</html>