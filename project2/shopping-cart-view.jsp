<%@page import="java.util.*, project2.*"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
	<title>Shopping Cart</title>
	<head lang="en"><meta charset="UTF-8"></head>
<body>
	
	<form action="FabflixControllerServlet" method="GET">
		<table>
			<tr>
				<th>Movie</th>
				<th>Quantity</th>
			</tr>
		
			<c:forEach var="m" items="${SHOPPING_CART}">	
				<tr>	
					<td> ${m.title} </td>
					<td>						
						<input type="text" name="${m.id}" value="${m.quantity}">
					</td>		
				</tr>
				
			</c:forEach>
		
		</table>
		
		<button type="submit" name="command" value="update">Update</button>
	</form>
	
	<form action="checkout-view.jsp" method="GET">
		<button type="submit" name="command" value="linkToJsp">Proceed to Checkout</button>
	</form>
	
	<a href="search-view.jsp">Continue Shopping</a>


</body>
</html>