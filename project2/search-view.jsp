<%@ page import="java.util.*, project2.*"%>
<!DOCTYPE html>
<html>
	<title>Search Movies</title>
	<head lang="en">
	<meta charset="UTF-8">
	</head>


<% 
	// list movies from the request object sent by SearchControllerServlet
	ArrayList<Movie> movies = (ArrayList<Movie>) request.getAttribute("movies");
%>


<body>

	<form action="SearchControllerServlet" method="GET">		
		<table>
			<tbody>
			
				<tr>
					<td><label>Title </label></td>
					<td><input type="text" name="title" /></td>
				</tr>
				
				<tr>
					<td><label>Year </label></td>
					<td><input type="text" name="year" /></td>
				</tr>
				
				<tr>
					<td><label>Director </label></td>
					<td><input type="text" name="director" /></td>
				</tr>
				
				<tr>
					<td><label>First Name </label></td>
					<td><input type="text" name="firstName" /></td>
				</tr>
				
				<tr>
					<td><label>Last Name </label></td>
					<td><input type="text" name="lastName" /></td>
				</tr>
				
				<tr>
					<td><button type="submit" name="button" value="searchByFields">Search</button></td>
				</tr>
		
			</tbody>
		</table>
	</form>
	
	<br></br>
	<form action="SearchControllerServlet" method="GET">
		General Search <input type="text" name="keywords"/>
		<button type="submit" name="button" value="searchByKeywords">Search</button>
	</form>

	<table>
		<tr>
			<th>ID</th>
			<th>Title</th>
			<th>Year</th>
			<th>Director</th>
			<th>Banner URL</th>
			<th>Trailer URL</th>
		</tr>
 
		<%  for (Movie m : movies) { %> 
			<tr>
				<td> <%=  m.getId() %> </td>
				<td> <%=  m.getTitle() %> </td>
				<td> <%=  m.getYear() %> </td>
				<td> <%=  m.getDirector() %> </td>
				<td> <%=  m.getBanner_url() %> </td>
				<td> <%=  m.getTrailer_url() %> </td>
			</tr>
		
		<% } %>	
	
	</table>

</body>
</html>


