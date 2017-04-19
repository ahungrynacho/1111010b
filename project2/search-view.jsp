<%@ page import="java.util.*, project2.*"%>
<!DOCTYPE html>
<html>
<body>

<head>
	<title>Search Movies</title>

</head>

<form action="SearchControllerServlet" method="GET">
	Title: <input type="text" name="title"/>
	<br></br>
	Year: <input type="text" name="year"/>
	<br></br>
	Director: <input type="text" name="director"/>
	<br></br>
	Name: <input type="text" name="name"/>
	<br></br>
	
	<input type="submit" value="Submit"/>

</form>

<% 
	// list movies from the request object sent by SearchControllerServlet
	ArrayList<Movie> movies = (ArrayList<Movie>) request.getAttribute("MOVIES");
%>	
	<% for (Movie m : movies) { %>
		id: <% %>
		title: <% %>
		year: <% %>
		director: <% %>
		list of genres: <% %>
		list of stars: <% %>
	<% } %>

</body>
</html>

