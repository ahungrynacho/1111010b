<%@ page import = "project2.*" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Full Page Sign In - Bootsnipp.com</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="http://netdna.bootstrapcdn.com/bootstrap/3.1.0/css/bootstrap.min.css" rel="stylesheet">
	<link rel="stylesheet" href="login.css">
	<script src="http://code.jquery.com/jquery-1.11.1.min.js"></script>
	<script src="http://netdna.bootstrapcdn.com/bootstrap/3.1.0/js/bootstrap.min.js"></script> 
	<script src="login.js"></script>
</head>

<body>

	<div class="container">
	    <div class="row">
	        <div class="col-md-12">
	            <div class="pr-wrap">
	                <div class="pass-reset">
	                    <label>Enter the email you signed up with</label>
	                    <input type="email" placeholder="Email" />
	                    <input type="submit" value="Submit" class="pass-reset-submit btn btn-success btn-sm" />
	                </div>
	            </div>
	            <div class="wrap">
	                <p class="form-title">cinephim</p>
	                <form class="login" action="FabflixControllerServlet" method="POST">		<!-- SERVLET -->
	                <input type="text" placeholder="Email" name="email"/>		<!-- EMAIL -->
	                <input type="password" placeholder="Password" name="password"/>		<!-- PASSWORD -->
	                <input type="submit" class="btn btn-success btn-sm" name="command" value="login">		<!-- LOGIN -->
	                
	                <div class="remember-forgot">
	                    <div class="row">
	                    
	                        <div class="col-md-6">
	                            <div class="checkbox">
	                                <label>
	                                    <input type="checkbox" />
	                                    Remember Me
	                                </label>
	                            </div>
	                        </div>
	                        
	                        <div class="col-md-6 forgot-pass-content">
	                            <a href="javascription:void(0)" class="forgot-pass">Forgot Password</a>
	                        </div>
	                        
	                        <div>
							  	<c:if test="${FAIL}">		<!-- FAILED LOGIN -->
							  		<p class="form-title">invalid password and/or email</p>
								</c:if>
	                        </div>
	                    </div>
	                </div>
	              
	                </form>
	                

	            </div>
	        </div>
	    </div>
					
	</div>


</body>
</html>
