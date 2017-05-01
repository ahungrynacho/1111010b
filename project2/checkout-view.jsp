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


		<div class="inner cover">
			<form class="form-horizontal" action="FabflixControllerServlet" method="POST">
				<input type="hidden" name="method" value="search">		<!-- METHOD -->
				<fieldset>
					
					<!-- Text input-->
					<div class="form-group">
						<label class="col-md-4 control-label" for="txtStreet">First Name</label>  
						<div class="col-md-4">
						<input name="firstName" type="text" placeholder="first name" class="form-control input-md">
						</div>
					</div>
					
					<!-- Text input-->
					<div class="form-group">
						<label class="col-md-4 control-label" for="txtCity">Last Name</label>  
						<div class="col-md-4">
						<input name="lastName" type="text" placeholder="last name" class="form-control input-md">    
						</div>
					</div>
					
					<!-- Text input-->
					<div class="form-group">
						<label class="col-md-4 control-label" for="txtFlatNumber">Credit Card Number</label>  
						<div class="col-md-4">
						<input name="ccid" type="text" placeholder="visa/mastercard" class="form-control input-md">    
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-4 control-label" for="txtFlatNumber">Expiration Date</label>  
						<div class="col-md-4">
						<input name="expDate" type="text" placeholder="YYYY/MM/DD" class="form-control input-md">    
						</div>
					</div>
		
				
					<button type="submit" class="btn btn-success btn-sm" name="command" value="creditCardInfo">Submit</button>
				</fieldset>
				
				<br>
				<a href="MovieListServlet">Continue Shopping</a>
			</form>
			
			<br>
			<c:if test="${EMPTY_CART}">
				<label>Empty shopping cart! Please add a movie!</label>
			</c:if>
			
			<c:if test="${DECLINED_CARD}">
				<label>Credit card declined! Please try again!</label>
			</c:if>		
		</div>

	      <div class="mastfoot">
	        <div class="inner">
	          <!-- Validation -->
	
	
	          <p>© 2017 CINEPHIM, LLC </p>
	        </div>
	      </div>
	    </div>
	</div>
	</div>

</body>
</html>