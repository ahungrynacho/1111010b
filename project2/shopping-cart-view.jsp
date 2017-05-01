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
							<form action="FabflixControllerServlet" method="GET">
								<table class="table table-responsive table-background">
									<tr>
										<th><p align="center">Movie</p></th>
										<th><p align="center">Quantity</p></th>
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
								
								<button class="btn button btn-success btn-sm" type="submit" name="command" value="update">Update</button>
							</form>
							
							<br></br>
							
							<form action="checkout-view.jsp" method="GET">
								<button class="btn button btn-success btn-sm" type="submit">Proceed to Checkout</button>
							</form>	
							
							<br>
							<a class="text-color" href="MovieListServlet">Continue Shopping</a>				
					
							</div>
					</div>
					
			</div>
	
	    </div>
	    
	    


</body>
</html>