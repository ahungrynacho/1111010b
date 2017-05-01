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
					<h1>${MOVIE.title}</h1>               
						<img src="${MOVIE.bannerUrl}" width="200" height="200" alt="Image Unavailable" id="moviePoster"/>
						
						<table class="table table-responsive table-background">
							<tr>
								<th><p align="center"> Id </p></th>
								<th><p align="center"> Year </p></th>
								<th><p align="center"> Director </p></th>
								<th><p align="center"> Stars </p></th>
								<th><p align="center"> Genres </p></th>
								<th><p align="center"> Trailer </p></th>
								
							</tr>
							
							<tr> 
							<!--  -->
							
								<td> ${MOVIE.id} </td>
								<td> ${MOVIE.year} </td>
								<td> ${MOVIE.director} </td>
								
								<td> <c:forEach var = "stars" items = "${MOVIE.stars}"> 
											<a class="text-color" href = "SingleStarServlet?starId=${stars.id}">	${stars.firstName} ${stars.lastName} <br/> </a>
											</c:forEach>
								 </td>
											
								<td> <c:forEach var = "genres" items = "${MOVIE.genres}">
									 	<a class="text-color" href = "MovieListServlet?genreName=${genres.name}"> ${genres.name} <br/> </a>
									 	</c:forEach>
							 	 </td>
							 	 
							 	 <td> <a class="text-color" href="${MOVIE.trailerUrl}">URL</a> </td>
								
								
							</tr>
							
						</table>
						
						
						<form action="FabflixControllerServlet" method="GET">
							Quantity <input type="text" name="quantity" value="1">
							<input type="hidden" name="movieId" value="${MOVIE.id}">
							<button class="btn btn-success" type="submit" name="command" value="addToCart"><span style="margin-right:20px" class="glyphicon glyphicon-shopping-cart" aria-hidden="true"></span>Add to Cart</button>                                      
						</form>
					
						<c:if test="${ERROR_MSG}">
							<b>Invalid Quantity!</b>
						</c:if>	
						
						<br></br>
						
						<a class="text-color" href="MovieListServlet">Continue Shopping</a>  						
						
						
					</div>
				</div>
			
                 
                    
                    
                    
                    
                    
                    
                    
                    
                                              
			


	      </div>
	
	    </div>
	    

</body>
</html>