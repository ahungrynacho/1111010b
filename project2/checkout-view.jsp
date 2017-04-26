<%@ page import="java.util.*, project2.*"%>
<!DOCTYPE html>
<html>
	<title>Checkout</title>
	<head lang="en"><meta charset="UTF-8"></head>

<body>
	<form action="FabflixControllerServlet" method="POST">

		<table>
			<tbody>
				<tr>
					<td><label>First Name </label></td>
					<td><input type="text" name="firstName"></td>
				</tr>
				
				<tr>
					<td><label>Last Name </label></td>
					<td><input type="text" name="lastName"></td>
				</tr>
				
				<tr>
					<td><label>Credit Card Number </label></td>
					<td><input type="text" name="ccid"></td>
				</tr>
				
				<tr>
					<td><label>Expiration Date (YYYY/MM/DD) </label></td>
					<td><input type="text" name="expDate"></td>
				</tr>
				
				<tr>
					<td><button type="submit" name="command" value="creditCardInfo">Submit</button></td>
				</tr>
			</tbody>
		</table>
	</form>
</body>	
</html>