<%@page import="java.util.*, project2.*"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
	<title>Checkout</title>
	<head lang="en"><meta charset="UTF-8"></head>


<body>

	<a href="main-page.jsp">Home</a>
	<a href="shopping-cart-view.jsp">Shopping Cart</a>
	<a href="checkout-view.jsp">Checkout</a>

	<h1>Credit card approved! Transaction complete!</h1>
	<br></br>
	<h3>Order History</h3>
	
	<c:forEach var="trans" items="${BOUGHT_BY_CUSTOMER}">
		<table>
			<tr>
				<th>Transaction ID</th>
				<th>Customer ID</th>
				<th>Movie ID</th>
				<th>Sale Date</th>
			</tr>
																	
			<tr>
				<td> ${trans.id} </td>
				<td> ${trans.customerId} </td>
				<td> ${trans.movieId} </td>
				<td> ${trans.saleDate} </td>			
			</tr>
		</table>		
	</c:forEach>
	
	<c:forEach var="movie" items="${SHOPPING_CART}">
		<table>
			<tr>
				<th>Movie ID</th>
				<th>Title</th>
				<th>Quantity</th>
			</tr>
																	
			<tr>
				<td> ${movie.id} </td>
				<td> ${movie.title} </td>
				<td> ${movie.quantity} </td>	
			</tr>
		</table>	
	</c:forEach>
	
</body>
</html>