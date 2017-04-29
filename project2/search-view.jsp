<%@page import="java.util.*, project2.*"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
	<title>Search Movies</title>
	<head lang="en">
	<meta charset="UTF-8">
	</head>

<body>

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
	
	<br></br>
	<form action="FabflixControllerServlet" method="GET">
		<input type="hidden" name="method" value="search">
		General Search <input type="text" name="keywords"/>
		<button type="submit" name="command" value="searchByKeywords">Search</button>
	</form>
	
	<br></br>
	<a href="checkout-view.jsp">Proceed to Checkout</a>
	<a href="shopping-cart-view.jsp">Shopping Cart</a>

<%-- 
	<form action="FabflixControllerServlet" method="GET">
		<table>
			<tr>
				<th>ID</th>
				<th>Title</th>
				<th>Year</th>
				<th>Director</th>
				<th>Genre</th>
				<th>Movie Stars</th>
			</tr>
			
			<c:forEach var="m" items="${MOVIE_LIST}">
				<!-- set up a link for each movie -->
				<c:url var="movieLink" value="FabflixControllerServlet">
					<c:param name="command" value="linkToMovie" />
					<c:param name="movieId" value="${m.id}" />
				</c:url>
																	
				<tr>
					<td> ${m.id} </td>
					<td> <a href="${movieLink}">${m.title}</a></td>
					<td> ${m.year} </td>
					<td> ${m.director} </td>
					<td>
						<table>
							<c:forEach var="g" items="${m.genres}">
								<tr>
									<td> ${g.name} </td>
								</tr>
							</c:forEach>
						</table>
					</td>
					<td> 
						<table>
							<c:forEach var="s" items="${m.stars}">
								<tr>
									<td> ${s.firstName} ${s.lastName} </td>
								</tr>
							</c:forEach>
						</table>
					</td>
					
					
				</tr>
			</c:forEach>

 		
		</table>
	</form>
--%>
</body>
</html>


