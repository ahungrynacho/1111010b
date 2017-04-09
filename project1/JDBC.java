package jdbc;
import java.sql.*;
import java.util.Scanner;
import java.util.ArrayList;

// http://theopentutorials.com/tutorials/java/jdbc/jdbc-mysql-CONNECTION-tutorial/
public class JDBC {
	private static Scanner INPUT = new Scanner(System.in);
	private static String URL = "jdbc:mysql:///moviedb?autoReconnect=true&useSSL=false";
	private static String USER = null;
	private static String PASSWORD = null;
	private static String[] TABLES = {
			"movies", "stars", "stars_in_movies",
			"genres", "genres_in_movies", "customers",
			"sales", "creditcards"
			};
	
	private Connection CONNECTION = null;
	private Statement STATEMENT = null;
	private boolean RUNNING = true;
	
	public JDBC() throws Exception {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
	}
	
	public void login() throws Exception {
		
		System.out.print("username: ");
		JDBC.USER = JDBC.INPUT.nextLine();
		System.out.print("password: ");
		JDBC.PASSWORD = JDBC.INPUT.nextLine();
		this.CONNECTION = DriverManager.getConnection(JDBC.URL, JDBC.USER, JDBC.PASSWORD);
		this.STATEMENT = this.CONNECTION.createStatement();
	}
	
	public String getFeaturedMovies() {
		String query = null;
		
		System.out.print("Search by ID (y/n): ");
		if (JDBC.INPUT.nextLine().equalsIgnoreCase("y")) {
			System.out.print("id: ");
			String id = JDBC.INPUT.nextLine();
			query = String.format("SELECT * FROM movies m, stars_in_movies sim "
								+ "WHERE sim.star_id = %s AND m.id = sim.movie_id", id);
			return query;
		}

		System.out.print("Search by full name (y/n): ");
		if (JDBC.INPUT.nextLine().equalsIgnoreCase("y")) {
			System.out.print("first_name: ");
			String firstName = JDBC.INPUT.nextLine();
			
			System.out.print("last_name: ");
			String lastName = JDBC.INPUT.nextLine();
			
			query = String.format("SELECT * FROM movies m, stars_in_movies sim, stars s "
								+ "WHERE s.first_name = '%s' AND s.last_name = '%s' "
								+ "AND s.id = sim.star_id AND sim.movie_id = m.id", 
								firstName, lastName);
			
			return query;
		}
		
		System.out.print("Search by first name (y/n): ");
		if (JDBC.INPUT.nextLine().equalsIgnoreCase("y")) {
			System.out.print("first_name: ");
			String firstName = JDBC.INPUT.nextLine();
			
			query = String.format("SELECT * FROM movies m, stars_in_movies sim, stars s "
								+ "WHERE s.first_name = '%s' "
								+ "AND s.id = sim.star_id AND sim.movie_id = m.id", 
								firstName);
			return query;
		}
		
		System.out.print("Search by last name (y/n): ");
		if (JDBC.INPUT.nextLine().equalsIgnoreCase("y")) {
			System.out.print("last_name: ");
			String lastName = JDBC.INPUT.nextLine();
			
			query = String.format("SELECT * FROM movies m, stars_in_movies sim, stars s "
								+ "WHERE s.last_name = '%s' "
								+ "AND s.id = sim.star_id AND sim.movie_id = m.id", 
								lastName);
			return query;	
		}

		return query;
	}
	
	public String addStar() {
		String dob = null;
		String photoUrl = null;
		String query = null;
		
		System.out.print("id: ");
		String id = JDBC.INPUT.nextLine();
		
		System.out.print("first_name: ");
		String firstName = JDBC.INPUT.nextLine();
		
		System.out.print("last_name: ");
		String lastName = JDBC.INPUT.nextLine();
		
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
			query = String.format("INSERT INTO stars VALUES(%s, '%s', '%s', null, '')",
									id, firstName, lastName);
		}
		else if (dob != null && photoUrl == null) {
			query = String.format("INSERT INTO stars VALUES(%s, '%s', '%s', '%s', '')", 
									id, firstName, lastName, dob);
		}
		else if (dob != null && photoUrl != null) {
			query = String.format("INSERT INTO stars VALUES(%s, '%s', '%s', '%s', '%s')", 
									id, firstName, lastName, dob, photoUrl);
		}
		return query;
	}
	
	public String addCustomer() throws Exception {
		String query = null;
		
		System.out.print("id: ");
		String id = JDBC.INPUT.nextLine();
		
		System.out.print("first_name: ");
		String firstName = JDBC.INPUT.nextLine();
		
		System.out.print("last_name: ");
		String lastName = JDBC.INPUT.nextLine();
		
		System.out.print("cc_id: ");
		String ccid = JDBC.INPUT.nextLine();
		
		ResultSet result = this.STATEMENT.executeQuery(String.format("SELECT * FROM creditcards cc "
																	+ "WHERE cc.id = '%s'", ccid));
		
		if (!result.next()) {
			System.out.println("ERROR: Must input credit card number.");
			return query;
		}
		
		System.out.print("address: ");
		String address = JDBC.INPUT.nextLine();
		
		System.out.print("email: ");
		String email = JDBC.INPUT.nextLine();
		
		System.out.print("password: ");
		String password = JDBC.INPUT.nextLine();
		
		query = String.format("INSERT INTO customers VALUES(%s, '%s', '%s', '%s', '%s', '%s', '%s')", 
								id, firstName, lastName, ccid, address, email, password);
		return query;
	}
	
	public String removeCustomer() {
		String query = null;
		System.out.print("id: ");
		String id = JDBC.INPUT.nextLine();
		query = String.format("DELETE FROM customers WHERE id = %s", id);
		return query;
	}
	
	public String getMetadata() throws Exception {		
		
		// non-query approach may be faster
		DatabaseMetaData metadata = this.CONNECTION.getMetaData();
		
		for (int i = 0; i < JDBC.TABLES.length; ++i) {
			ResultSet columns = metadata.getColumns(null, null, JDBC.TABLES[i], "%");
			System.out.println(JDBC.TABLES[i]);
			while (columns.next()) {
				System.out.println(columns.getString(4) + " : " 
									+ columns.getString(6));
			}
			System.out.println();
		}
	
		return null;
	}
	
	public ResultSet query(String command) throws Exception {
		ResultSet result = this.STATEMENT.executeQuery(command);
		if (result.next()) {
			
		}
		
		return result;
	}
	
	public void printQuery(String command) throws Exception {
		ResultSet result = this.STATEMENT.executeQuery(command);
		ResultSetMetaData metadata = result.getMetaData();
		
		while (result.next()) {
			for (int i = 1; i <= metadata.getColumnCount(); ++i) {
				System.out.println(metadata.getColumnName(i) + " : " 
									+ result.getString(i));
			}
			System.out.println();
		}		
	}
	
	public void update(String command) throws Exception {
		if (command != null)
			this.STATEMENT.executeUpdate(command);
	}
	
	public void run() throws Exception {
		
		while (this.RUNNING) {
			System.out.println("1. Print movies");
			System.out.println("2. Insert a new star");
			System.out.println("3. Insert a new customer");
			System.out.println("4. Delete a customer");
			System.out.println("5. Print database metadata");
			System.out.println("6. Logout");
			System.out.println("7. Exit");
			
			System.out.print("$ ");
			String command = JDBC.INPUT.nextLine();

			switch(command) {
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
			
		}
	}
	
}
