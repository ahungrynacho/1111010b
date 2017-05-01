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


		<div class="inner cover form-group">
			  <form action="MovieListServlet" method="GET">
			  		<input type="hidden" name="method" value="browse">
			  		<label>Browse by Title</label>
					  <select class="text-color" name="titleSort">
						<option value="A">A</option>
						<option value="B">B</option>
						<option value="C">C</option>
						<option value="D">D</option>
						<option value="E">E</option>
						<option value="F">F</option>
						<option value="G">G</option>
						<option value="H">H</option>
						<option value="I">I</option>
						<option value="J">J</option>
						<option value="K">K</option>
						<option value="L">L</option>
						<option value="M">M</option>
						<option value="N">N</option>
						<option value="O">O</option>
						<option value="P">P</option>
						<option value="Q">Q</option>
						<option value="R">R</option>
						<option value="S">S</option>
						<option value="T">T</option>
						<option value="U">U</option>
						<option value="V">V</option>
						<option value="W">W</option>
						<option value="X">X</option>
						<option value="Y">Y</option>
						<option value="Z">Z</option>
						<option value="0">0</option>
						<option value="1">1</option>
						<option value="2">2</option>
						<option value="3">3</option>
						<option value="4">4</option>
						<option value="5">5</option>
						<option value="6">6</option>
						<option value="7">7</option>
						<option value="8">8</option>
						<option value="9">9</option>
					  </select>
				  <input type="submit" class="btn btn-success btn-sm" value="Go" />
				</form>
			 <br/>
			 <br/>
			 <form action="MovieListServlet" method="GET">
			 		<input type="hidden" name="method" value="browse">
			  		<label>Browse by Genre</label>
					  <select class="text-color" name="genreName">
					 	<option value="Action">Action</option>
						<option value="Adventure">Adventure</option>
						<option value="Animation">Animation</option>
						<option value="Arts">Arts</option>
						<option value="Classic">Classic</option>
						<option value="Comedy">Comedy</option>
						<option value="Coming-Of-Age-Drama">Coming-of-Age-Drama</option>
						<option value="Crime">Crime</option>
						<option value="CrimeGangster">Crime/Gangster</option>
						<option value="Detective">Detective</option>
						<option value="Documentary">Documentary</option>
						<option value="Drama">Drama</option>
						<option value="epics">epics</option>
						<option value="Epics/Historical">Epics/Historical</option>
						<option value="Family">Family</option>
						<option value="Fasntasy">Fantasy</option>
						<option value="Film-Noir">Film-Noir</option>
						<option value="Foreign">Foreign</option>
						<option value="Fangster">Gangster</option>
						<option value="Horror">Horror</option>
						<option value="Indie">Indie</option>
						<option value="James Bond">James Bond</option>
						<option value="kid">Kid</option>
						<option value="Love">Love</option>
						<option value="Music">Music</option>
						<option value="Musical">Musical</option>
						<option value="Musical/Performing Arts">Musical/Performing Arts</option>
						<option value="Mystery">Mystery</option>
						<option value="Roman">Roman</option>
						<option value="Romance">Romance</option>
						<option value="Science Fiction">Science Fiction</option>
						<option value="Short">Short</option>
						<option value="Spy">Spy</option>
						<option value="Suspense">Suspense</option>
						<option value="Thriller">Thriller</option>
						<option value="Tragedy">Tragedy</option>
						<option value="War">War</option>
						<option value="Western">Western</option>
					  </select>
				  <input type="submit" class="btn btn-success btn-sm" value="Go" />
				</form>
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