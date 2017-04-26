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
		<c:url var="checkout" value="FabflixControllerServlet">
			<c:param name="command" value="addToCart" />
			<c:param name="movieId" value="${MOVIE.id}" />
		</c:url>
		Quantity <input type="text" name="quantity">
		<button type="submit" value="${checkout}">Add to Cart</button>
	</form>

</body>
</html>