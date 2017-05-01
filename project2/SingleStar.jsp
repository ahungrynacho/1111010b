<%@ page import = "project2.*" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <!-- This file has been downloaded from Bootsnipp.com. Enjoy! -->
    <title>Full screen background cover page. - Bootsnipp.com</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="http://netdna.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="main-page.css">
    <script src="http://code.jquery.com/jquery-1.11.1.min.js"></script>
    <script src="http://netdna.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>

</head>

<body>
	<div class="site-wrapper">
	  <div class="site-wrapper-inner">
	    <div class="cover-container">
	      <div class="masthead clearfix">
	        <div class="inner">
	          <h3 class="masthead-brand">C I N E P H I M</h3>
	
	          <ul class="nav masthead-nav">	
		        <li>
	              <a href="main-page.jsp">Home</a>
	            </li>
	                    
	            <li>
	              <a href="search-view.jsp">Search</a>
	            </li>
	            
	         	<li>
	              <a href="Browse.jsp">Browse</a>
	            </li>
	
	            <li>
	              <a href="shopping-cart-view.jsp">Shopping Cart</a>
	            </li>
	            
	           	<li>
	              <a href="checkout-view.jsp">Checkout</a>
	            </li>
	            

	          </ul>
	        </div>
	      </div>
	
	    	      <div class="inner single-page text-color" >
	    	      	<h1> ${STAR.firstName} ${STAR.lastName} </h1>
	    	      	<img src="${STAR.photoUrl}" width="200" height="200" alt="Image Unavailable" id="moviePoster"/>
					<table class="table table-responsive table-background">
						<tr>
							<th><p align="center"> Id </p></th>
							<th><p align="center"> DOB </p></th>
							<th><p align="center"> Movies </p></th>	
						</tr>
						
						<tr> 
							<td> ${STAR.id} </td>
							<td> ${STAR.dob} </td>
							<td> <c:forEach var = "movie" items = "${STAR.movies}"> 
								 	<a class="text-color" href = "SingleMovieServlet?movieId=${movie.id}">	${movie.title} <br><br/> </a>
								 </c:forEach>
							 </td>
						</tr>
						
					</table>
			
					<a class="text-color" href="MovieListServlet">Continue Shopping</a>					
						
						
					</div>
				</div>

	      </div>
	
	    </div>
	    

</body>
</html>