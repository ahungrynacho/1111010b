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
			<form class="form-horizontal" action="FabflixControllerServlet" method="GET">
				<input type="hidden" name="method" value="search">		<!-- METHOD -->
				<input type="hidden" name="newSearchQuery" value="true">
				<fieldset>
					
					<!-- Text input-->
					<div class="form-group">
						<label class="col-md-4 control-label" for="txtStreet">Title</label>  
						<div class="col-md-4">
						<input id="title" name="title" type="text" placeholder="movie title" class="form-control input-md">
						</div>
					</div>
					
					<!-- Text input-->
					<div class="form-group">
						<label class="col-md-4 control-label" for="txtCity">Year</label>  
						<div class="col-md-4">
						<input id="year" name="year" type="text" placeholder="release date" class="form-control input-md">    
						</div>
					</div>
					
					<!-- Text input-->
					<div class="form-group">
						<label class="col-md-4 control-label" for="txtFlatNumber">Director</label>  
						<div class="col-md-4">
						<input id="director" name="director" type="text" placeholder="movie director" class="form-control input-md">    
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-4 control-label" for="txtFlatNumber">First Name</label>  
						<div class="col-md-4">
						<input id="firstName" name="firstName" type="text" placeholder="actor's first name" class="form-control input-md">    
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-4 control-label" for="txtFlatNumber">Last Name</label>  
						<div class="col-md-4">
						<input id="lastName" name="lastName" type="text" placeholder="actor's last name" class="form-control input-md">    
						</div>
					</div>
				
					<button type="submit" class="btn btn-success btn-sm" name="command" value="searchByFields">Search</button>
				</fieldset>
				
				
			</form>		
		</div>
<%-- 
	      <div class="inner cover">
	        <h1 class="cover-heading">Welcome Back</h1>
	
	        <p class="lead"> ${CUSTOMER.firstName} </p>
	
	        <p class="lead"><a class="btn btn-lg btn-info" href="search-view.jsp">Search Now</a></p>
	      </div>
--%>		
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