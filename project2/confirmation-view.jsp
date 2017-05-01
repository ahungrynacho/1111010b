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


		<div class="row cover-container options-padding">


			<h3>Review Order</h3>
			<table class="table table-responsive table-background text-color">
				<c:forEach var="movie" items="${SHOPPING_CART}">
					
						<tr>
							<th><p align="center">Movie ID</p></th>
							<th><p align="center">Title</p></th>
							<th><p align="center">Quantity</p></th>
						</tr>
																				
						<tr>
							<td> ${movie.id} </td>
							<td> ${movie.title} </td>
							<td> ${movie.quantity} </td>	
						</tr>
						
				</c:forEach>
			</table>
			
			
			<h3>Order History</h3>
			<table class="table table-responsive table-background text-color">
				<c:forEach var="trans" items="${BOUGHT_BY_CUSTOMER}">
					
						<tr>
							<th><p align="center">Transaction ID</p></th>
							<th><p align="center">Customer ID</p></th>
							<th><p align="center">Movie ID</p></th>
							<th><p align="center">Sale Date</p></th>
						</tr>
																				
						<tr>
							<td> ${trans.id} </td>
							<td> ${trans.customerId} </td>
							<td> ${trans.movieId} </td>
							<td> ${trans.saleDate} </td>			
						</tr>
							
				</c:forEach>
			</table>
			
		</div>

	    </div>
	</div>
	</div>

</body>
</html>