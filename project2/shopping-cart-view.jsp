<%@page import="java.util.*, project2.*"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
	<title>Shopping Cart</title>
	<head lang="en"><meta charset="UTF-8"></head>
<body>
	
	<table>
		<tr>
			<th>Movie</th>
			<th>Quantity</th>
		</tr>
		
	<c:forEach var="m" items="${SHOPPING_CART}">
		<tr>
			<td> ${m.title} </td>
			<td> ${m.quantity} </td>
		</tr>
	</c:forEach>
	
	</table>



</body>
</html>