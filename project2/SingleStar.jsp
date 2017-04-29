<%@page import="java.util.*, project2.*"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<body>

	<a href="main-page.jsp">Home</a>
	<a href="shopping-cart-view.jsp">Shopping Cart</a>
	<a href="checkout-view.jsp">Checkout</a>
	
	<head>
		<title>Single Star </title>
		
		
		<link type="text/css" rel="stylesheet" href="css/style.css">
	</head>

	<div id="wrapper">
		<div id="header">
			<h2>Star</h2>
		</div>
	</div>

	<div id="container">
		<div id="content">
		
			<table>
				<tr>
					<th> Poster</th>
					<th> Id </th>
					<th> Name </th>
					<th> DOB </th>
					<th> Movies </th>
					
				</tr>
				
				<tr> 

				<td><img src="${STAR.photoUrl}" width="200" height="200" alt="Image Unavailable" id="moviePoster"/></td>
				<td> ${STAR.id} </td>
				<td> ${STAR.firstName} ${stars.lastName} </td>
				<td> ${STAR.dob} </td>
				<td> <c:forEach var = "movie" items = "${STAR.movies}"> 
					 <a href = "SingleMovieServlet?movieId=${movie.id}">	${movie.title} <br><br/> </a>
					</c:forEach>
				 </td>
				</tr>
				
			</table>
		</div>
	</div>
	
	<a href="MovieListServlet">Back</a>
	
</body>
</html>
