 
<%@page import="java.util.*, project2.*"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
	<title>Search Movies</title>
	<head lang="en">
	<meta charset="UTF-8">
	</head>

<body>

	<a href="main-page.jsp">Home</a>
	<a href="shopping-cart-view.jsp">Shopping Cart</a>
	<a href="checkout-view.jsp">Checkout</a>

	<form action="FabflixControllerServlet" method="GET">
		<input type="hidden" name="method" value="search">		
		<table>
			<tbody>
				
				<tr>
					<td><label>Title </label></td>
					<td><input type="text" name="title" /></td>
				</tr>
				
				<tr>
					<td><label>Year </label></td>
					<td><input type="text" name="year" /></td>
				</tr>
				
				<tr>
					<td><label>Director </label></td>
					<td><input type="text" name="director" /></td>
				</tr>
				
				<tr>
					<td><label>First Name </label></td>
					<td><input type="text" name="firstName" /></td>
				</tr>
				
				<tr>
					<td><label>Last Name </label></td>
					<td><input type="text" name="lastName" /></td>
				</tr>
				
				<tr>
					<td><button type="submit" name="command" value="searchByFields">Search</button></td>
				</tr>
		
			</tbody>
		</table>
	</form>

<%-- 	
	<br></br>
	<form action="FabflixControllerServlet" method="GET">
		<input type="hidden" name="method" value="search">
		General Search <input type="text" name="keywords"/>
		<button type="submit" name="command" value="searchByKeywords">Search</button>
	</form>
--%>

</body>
</html>
