<%-- 
<%@page import="java.util.*, project2.*"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
	<title>Checkout</title>
	<head lang="en"><meta charset="UTF-8"></head>

<body>

	<table>
		<tr>
			<th>ID</th>
			<th>Title</th>
			<th>Year</th>
			<th>Director</th>
			<th>Banner URL</th>
			<th>Trailer URL</th>
		</tr>
											
		<tr>
			<td> ${MOVIE.id} </td>
			<td> ${MOVIE.title} </td>
			<td> ${MOVIE.year} </td>
			<td> ${MOVIE.director} </td>
			<td> ${MOVIE.bannerUrl} </td>
			<td> ${MOVIE.trailerUrl} <td>
		</tr>
	</table>

	<form action="FabflixControllerServlet" method="GET">
		Quantity <input type="text" name="quantity">
		<button type="submit" name="command" value="addToCart">Add to Cart</button>
	</form>
	
	<a href="search-view.jsp">Back</a>
</body>
</html>
--%>