package jdbc;
import java.sql.*;
import java.util.Scanner;

// http://theopentutorials.com/tutorials/java/jdbc/jdbc-mysql-CONNECTION-tutorial/

public class JDBC {
	private static Scanner INPUT = new Scanner(System.in);
	private static String URL = "jdbc:mysql:///moviedb?autoReconnect=true&useSSL=false";
	private static String USER = null;		// bbhuynh
	private static String PASSWORD = null;		// spring2017
	private static String[] TABLES = {
			"movies", "stars", "stars_in_movies",
			"genres", "genres_in_movies", "customers",
			"sales", "creditcards"
			};
	
	private Connection CONNECTION = null;
	private Statement STATEMENT = null;
	
	public JDBC() throws Exception {
		/* Constructor */
		
		Class.forName("com.mysql.jdbc.Driver").newInstance();
	}
	
	public void login() {
		/* Repeatedly ask the user for correct login information. */
		
		while (true) {
			try {
				System.out.print("username: ");
				JDBC.USER = JDBC.INPUT.nextLine();
				System.out.print("password: ");
				JDBC.PASSWORD = JDBC.INPUT.nextLine();
				this.CONNECTION = DriverManager.getConnection(JDBC.URL, 
															JDBC.USER, 
															JDBC.PASSWORD);
				this.STATEMENT = this.CONNECTION.createStatement();
				return;
			} catch (Exception e) {
				System.out.println("Connection error: timeout after 3 attempts, "
						+ "invalid username, or invalid password.");		
			}
		}

	}
	
	private String getFeaturedMovies() {
		/* Print out (to the screen) the movies featuring a given star. 
		 * All movie attributes should appear, labeled and neatly arranged; 
		 * the star can be queried via first name and/or last name or by ID. 
		 * First name and/or last name means that a star should be queried 
		 * by both a) first name AND last name b) first name or last name. 
		 * Returns a mySQL query as a String. */
		
		System.out.print("Search by ID (y/n): ");
		if (JDBC.INPUT.nextLine().equalsIgnoreCase("y")) {
			System.out.print("id: ");
			String id = JDBC.INPUT.nextLine();
			return String.format("SELECT * FROM movies m, stars_in_movies sim "
								+ "WHERE sim.star_id = %s AND "
								+ "m.id = sim.movie_id", id);
		}

		System.out.print("Search by full name (y/n): ");
		if (JDBC.INPUT.nextLine().equalsIgnoreCase("y")) {
			System.out.print("first_name: ");
			String firstName = JDBC.INPUT.nextLine();
			
			System.out.print("last_name: ");
			String lastName = JDBC.INPUT.nextLine();
			
			return String.format("SELECT * FROM movies m, "
								+ "stars_in_movies sim, stars s "
								+ "WHERE s.first_name = '%s' AND s.last_name = '%s' "
								+ "AND s.id = sim.star_id AND sim.movie_id = m.id", 
								firstName, lastName);
		}
		
		System.out.print("Search by first name (y/n): ");
		if (JDBC.INPUT.nextLine().equalsIgnoreCase("y")) {
			System.out.print("first_name: ");
			String firstName = JDBC.INPUT.nextLine();
			
			return String.format("SELECT * FROM movies m, "
								+ "stars_in_movies sim, stars s "
								+ "WHERE s.first_name = '%s' "
								+ "AND s.id = sim.star_id AND sim.movie_id = m.id", 
								firstName);
		}
		
		System.out.print("Search by last name (y/n): ");
		if (JDBC.INPUT.nextLine().equalsIgnoreCase("y")) {
			System.out.print("last_name: ");
			String lastName = JDBC.INPUT.nextLine();
			
			return String.format("SELECT * FROM movies m, "
								+ "stars_in_movies sim, stars s "
								+ "WHERE s.last_name = '%s' "
								+ "AND s.id = sim.star_id AND sim.movie_id = m.id", 
								lastName);	
		}
		
		return null;
	}
	
	private String addStar() {
		/* Insert a new star into the database. 
		 * If the star has a single name, add it as his last_name 
		 * and assign an empty string ("") to first_name. 
		 * Returns a mySQL query as a String. */
		
		String dob = null;
		String photoUrl = null;
		String firstName = null;
		String lastName = null;
		
		System.out.print("id: ");
		String id = JDBC.INPUT.nextLine();
		
		System.out.print("Full Name (y/n): ");
		if (JDBC.INPUT.nextLine().equalsIgnoreCase("y")) {
			System.out.print("first_name: ");
			firstName = JDBC.INPUT.nextLine();
			
			System.out.print("last_name: ");
			lastName = JDBC.INPUT.nextLine();		
		}
		else {
			firstName = "";
			System.out.print("last_name: ");
			lastName = JDBC.INPUT.nextLine();							
		}
		
		System.out.print("Date of Birth YYYY/MM/DD (y/n): ");
		if (JDBC.INPUT.nextLine().equalsIgnoreCase("y")) {
			System.out.print("dob: ");
			dob = JDBC.INPUT.nextLine();
		}
		
		System.out.print("Photo URL (y/n): ");
		if (JDBC.INPUT.nextLine().equalsIgnoreCase("y")) {
			System.out.print("photo_url: ");
			photoUrl = JDBC.INPUT.nextLine();
		}

		if (dob == null && photoUrl == null) {
			return String.format("INSERT INTO stars VALUES(%s, '%s', '%s', null, '')",
									id, firstName, lastName);
		}
		else if (dob != null && photoUrl == null) {
			return String.format("INSERT INTO stars VALUES(%s, '%s', '%s', '%s', '')", 
									id, firstName, lastName, dob);
		}
		else if (dob != null && photoUrl != null) {
			return String.format("INSERT INTO stars VALUES(%s, '%s', '%s', '%s', '%s')", 
									id, firstName, lastName, dob, photoUrl);
		}
		else
			return null;
	}
	
	private String addCustomer() throws Exception {
		/* Insert a customer into the database. Do not allow insertion 
		 * of a customer if his credit card does not exist in the credit 
		 * card table. The credit card table simulates the bank records. 
		 * If the customer has a single name, add it as his last_name and 
		 * assign an empty string ("") to first_name. 
		 * Returns a mySQL query as a String. */
		
		String firstName = null;
		String lastName = null;
		String ccid = null;
		
		System.out.print("id: ");
		String id = JDBC.INPUT.nextLine();
	
		System.out.print("Full Name (y/n): ");
		if (JDBC.INPUT.nextLine().equalsIgnoreCase("y")) {
			System.out.print("first_name: ");
			firstName = JDBC.INPUT.nextLine();
			
			System.out.print("last_name: ");
			lastName = JDBC.INPUT.nextLine();		
		}
		else {
			firstName = "";
			System.out.print("last_name: ");
			lastName = JDBC.INPUT.nextLine();							
		}	
		
		while (true) {		// Repeatedly ask the user for a valid credit card number
			System.out.print("cc_id: ");
			ccid = JDBC.INPUT.nextLine();
			
			ResultSet result = this.STATEMENT.executeQuery(String.format("SELECT * FROM creditcards cc "
																		+ "WHERE cc.id = '%s'", ccid));
			
			if (result.next())		// If a valid credit card number appears in the query
				break;
			else
				System.out.println("ERROR: Must input credit card number.");
		}
		
		System.out.print("address: ");
		String address = JDBC.INPUT.nextLine();
		
		System.out.print("email: ");
		String email = JDBC.INPUT.nextLine();
		
		System.out.print("password: ");
		String password = JDBC.INPUT.nextLine();
		
		return String.format("INSERT INTO customers "
							+ "VALUES(%s, '%s', '%s', '%s', '%s', '%s', '%s')", 
								id, firstName, lastName, ccid, 
								address, email, password);
	}
	
	private String removeCustomer() {
		/* Delete a record in "customer" which cascade deletes
		 * a record in "sales". Returns a mySQL query as a String. */

		System.out.print("id: ");
		String id = JDBC.INPUT.nextLine();
		return String.format("DELETE FROM customers WHERE id = %s", id);
	}
	
	private void getMetadata() throws Exception {		
		/* Provide the metadata of the database; 
		 * in particular, print out the name of each table and, 
		 * for each table, each attribute and its type. */
		
		DatabaseMetaData metadata = this.CONNECTION.getMetaData();
		
		for (int i = 0; i < JDBC.TABLES.length; ++i) {
			ResultSet columns = metadata.getColumns(null, null, JDBC.TABLES[i], "%");
			System.out.println(JDBC.TABLES[i]);
			
			while (columns.next()) {
				System.out.println(columns.getString(4) + " : " 
									+ columns.getString(6));		// column name : mySQL data type
			}
			System.out.println();
		}

	}
	
	private void printQuery(String command) {
		/* Executes "command" which is a mySQL query as a String. 
		 * "command" must contain a SELECT statement. */
		
		try {
			ResultSet result = this.STATEMENT.executeQuery(command);
			ResultSetMetaData metadata = result.getMetaData();
			
			while (result.next()) {
				for (int i = 1; i <= metadata.getColumnCount(); ++i) {
					System.out.println(metadata.getColumnName(i) + " : " 
										+ result.getString(i));		// column name : column entry
				}
				System.out.println();
			}
		} catch (Exception e) {
			System.out.println("Not a valid query.");
		}
	}
	
	private void update(String command) {
		/* Executes "command" which is a mySQL query as a String. 
		 * "command" must contain an INSERT, UPDATE, or DELETE statement. */
		
		try {
			int status = this.STATEMENT.executeUpdate(command);
			if (status > 0)
				System.out.println(status + " record(s) successfully updated.");
			else
				System.out.println("No records updated.");
			
		} catch (Exception e) {
			System.out.println("Not a valid query.");
		}
	}
	
	public void run() {		
		/* Acts as the controller and user interface by 
		 * calling JDBC's privately defined methods. */
		
		this.login();
		while (true) {
			System.out.println("0. Execute direct query");
			System.out.println("1. Print movies");
			System.out.println("2. Insert a new star");
			System.out.println("3. Insert a new customer");
			System.out.println("4. Delete a customer");
			System.out.println("5. Print database metadata");
			System.out.println("6. Logout");
			System.out.println("7. Exit");
			
			System.out.print("$ ");
			String command = JDBC.INPUT.nextLine();

			try {
				switch(command) {
					case "0":
						System.out.print("$ ");
						this.printQuery(JDBC.INPUT.nextLine());
						break;
						
					case "1":
						this.printQuery(this.getFeaturedMovies());
						break;
						
					case "2":
						this.update(this.addStar());
						break;
						
					case "3":
						this.update(this.addCustomer());
						break;
						
					case "4":
						this.update(this.removeCustomer());
						break;
						
					case "5":
						this.getMetadata();
						break;
					
					case "6":
						this.login();
						break;
						
					case "7":
						System.exit(0);
						break;
						
					default:
						System.out.println("No option selected.");
				}
			} catch (Exception e) {
				System.out.println("ERROR");
			} 
		}
	}
	
}
