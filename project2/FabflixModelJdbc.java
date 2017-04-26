package project2;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

public class FabflixModelJdbc {
	
	private DataSource dataSource;
	
	public FabflixModelJdbc(DataSource dataSource) {
		/* 
		 * Critical for connecting the mySQL database
		 * and tomcat in eclipse. 
		 */
		this.dataSource = dataSource;
	}
	
	private void close(Connection connection, 
			Statement statement, ResultSet result) {
		/* Closes connections to the database. */
		try {
			if (connection != null)
				connection.close();
			if (statement != null)
				statement.close();
			if (result != null) {
				result.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private String[] stripSpaces(String sentence) {
		/* Splits a string by '', " ", \t, or \n as delimiters. */
		return sentence.split("\\s+");
	}
	
	private boolean hasPattern(String regex, String str) {
		/* 
		 * Returns true if the regex pattern is found 
		 * in str otherwise return false. 
		 */ 
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.find();
	}
	
	private String createGeneralSearchQuery(String keywords) {
		/* 
		 * Returns a mySQL query string of records that match keywords in any column. 
		 * %% is the escape character for %. 
		 */
		String query = null;
		if (keywords == null || keywords.equalsIgnoreCase(""))
			return null;
		else {
			query = "SELECT DISTINCT m.id, m.title, m.year, m.director, "
					+ "m.banner_url, m.trailer_url "
					+ "FROM movies m, stars s, stars_in_movies sim "
					+ "WHERE ";
			
			ArrayList<String> subqueries = new ArrayList<String>(Arrays.asList(
					"m.title LIKE",
					"m.year =",
					"m.director LIKE",
					"s.id = sim.star_id AND m.id = sim.movie_id AND s.first_name LIKE",
					"s.id = sim.star_id AND m.id = sim.movie_id AND s.last_name LIKE"
					));

			ArrayList<String> result = new ArrayList<String>();
			
			String[] words = stripSpaces(keywords);
			for (String q : subqueries) {
				String temp = "";
				for (int i = 0; i < words.length; ++i) {
					if (temp.equalsIgnoreCase("") 
							&& subqueries.indexOf(q) == 1 
							&& hasPattern("^[0-9]+$", words[i]))
						temp += String.format("%s %s", q, words[i]);
					
					else if (subqueries.indexOf(q) == 1 
							&& hasPattern("^[0-9]+$", words[i]))
						temp += String.format(" OR %s %s", q, words[i]);
					
					else if (temp.equalsIgnoreCase("") 
							&& subqueries.indexOf(q) > 1 
							&& hasPattern("^[a-zA-Z\\-]+$", words[i]))
						temp += String.format("%s '%%%s%%'", q, words[i]);
					
					else if (subqueries.indexOf(q) > 1 
							&& hasPattern("^[a-zA-Z\\-]+$", words[i]))
						temp += String.format(" OR %s '%%%s%%'", q, words[i]);
					
					else if (temp.equalsIgnoreCase("") 
							&& subqueries.indexOf(q) == 0)
						temp += String.format("%s '%%%s%%'", q, words[i]);
			
					else if (subqueries.indexOf(q) == 0)
						temp += String.format(" OR %s '%%%s%%'", q, words[i]);
				}
				if (!temp.equalsIgnoreCase(""))
					result.add(temp);
			}
			
			for (String q : result) {
				if (result.indexOf(q) == 0)
					query += q;
				else
					query += String.format(" OR %s", q);
			}

		}

		return query;
	}
	
	private String createSearchQuery(String title, String year, String director, 
								String firstName, String lastName) {
		/* 
		 * Dynamically generates mySQL query strings
		 * covering 120 (5!) possible search combinations. 
		 */
		
		String query = null;
		String[] argv = {title, year, director, firstName, lastName};
		String[] queries = {
				String.format("m.title LIKE '%%%s%%'", title),
				String.format("m.year = %s", year),
				String.format("m.director LIKE '%%%s%%'", director),
				String.format("s.first_name LIKE '%%%s%%' AND "
						+ "m.id = sim.movie_id AND s.id = sim.star_id", firstName),
				String.format("s.last_name LIKE '%%%s%%' AND "
						+ "m.id = sim.movie_id AND s.id = sim.star_id", lastName)
				};
		
		/* 
		 * Input values are null on initialization 
		 * but are "" after 'submit' button is pressed.
		 * Be aware that calling equalsIgnoreCase() on a 
		 * variable of type String that assigned to null
		 * will yield to a NullPointerException.
		 */
		if ((title == null && year == null && director == null 
				&& firstName == null && lastName == null) 
				|| (title.equalsIgnoreCase("") && year.equalsIgnoreCase("") 
				&& director.equalsIgnoreCase("") && firstName.equalsIgnoreCase("") 
				&& lastName.equalsIgnoreCase("")))
			return null;
		
		else if (firstName.equalsIgnoreCase("") && lastName.equalsIgnoreCase(""))
			query = "SELECT * "
					+ "FROM movies m "
					+ "WHERE ";
		else
			query = "SELECT m.id, m.title, m.year, m.director, "
					+ "m.banner_url, m.trailer_url "
					+ "FROM movies m, stars s, stars_in_movies sim "
					+ "WHERE ";
		
		boolean firstAttribute = true;
		for (int i = 0; i < argv.length; ++i) {
			if (!argv[i].equalsIgnoreCase("") && firstAttribute) {
				query += queries[i];
				firstAttribute = false;
			}
			else if (!argv[i].equalsIgnoreCase("")) {
				query += " AND " + queries[i];
			}
		}
				
		return query;
	}
	
	private ArrayList<Genre> movieGenres(int movieId) throws Exception {
		/* 
		 * Returns a list of Genre objects corresponding to the movie
		 * queried by its movieId. 
		 */
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		ArrayList<Genre> genres = new ArrayList<Genre>();
		
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			String query = String.format("SELECT g.id, g.name "
						+ "FROM genres_in_movies gim, genres g "
						+ "WHERE gim.movie_id = %d AND "
						+ "gim.genre_id = g.id", movieId);
				
			result = statement.executeQuery(query);
			while (result.next()) {
				genres.add(new Genre(result.getInt("id"), 
								result.getString("name"))
						);
			}
			
		} finally {
			close(connection, statement, result);
		}
		
		return genres;
	}
	
	private ArrayList<Star> movieStars(int movieId) throws Exception {
		/*
		 * Returns a list of Star objects corresponding to the movie 
		 * queried by its movieId.
		 */
		
		ArrayList<Star> stars = new ArrayList<Star>();
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			String query = String.format("SELECT s.id, s.first_name, s.last_name, "
										+ "s.dob, s.photo_url "
										+ "FROM stars s, stars_in_movies sim "
										+ "WHERE s.id = sim.star_id AND "
										+ "sim.movie_id = %d", movieId);
			
			result = statement.executeQuery(query);
			
			while (result.next()) {
				stars.add(new Star(result.getInt("id"),
						result.getString("first_name"), 
						result.getString("last_name"), 
						result.getString("dob"), 
						result.getString("photo_url"))
						);
				
			}
			
		} finally {
			close(connection, statement, result);
		}
		
		return stars;
	}
	
	private ArrayList<Movie> movieList(ResultSet result) throws Exception {
		/* Helper function populating a list of movies returned from a query. */
		
		ArrayList<Movie> movies = new ArrayList<Movie>();
		while (result.next()) {
			int movieId = result.getInt("id");
			movies.add(new Movie(movieId, 
					result.getString("title"),
					result.getInt("year"), 
					result.getString("director"), 
					result.getString("banner_url"), 
					result.getString("trailer_url"),
					movieGenres(movieId), 
					movieStars(movieId))
					);
		}
		return movies;
	}
	
	public ArrayList<Movie> moviesByKeywords(String keywords) throws Exception {
		
		/* 
		 * Establishes an individual connection to the database 
		 * and retrieves records from the database that match 
		 * the given mySQL query. 
		 */
		
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		String query = null;
		
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			query = this.createGeneralSearchQuery(keywords);
			
			if (query != null) {
				result = statement.executeQuery(query);
				return movieList(result);
			}
		} finally {
			close(connection, statement, result);
		}
		return null;
	}
	
	public ArrayList<Movie> searchMovies(String title, String year, 
			String director, String firstName, String lastName) throws Exception {
		
		/* 
		 * Establishes an individual connection to the database 
		 * and retrieves records from the database that match 
		 * the given mySQL query. 
		 */
		
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		String query = null;
		
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			query = this.createSearchQuery(title, year, director, firstName, lastName);
			
			if (query != null) {		
				result = statement.executeQuery(query);
				return movieList(result);
			}
			
		} finally {
			close(connection, statement, result);
		}
		return null;
	}

	public boolean processPayment(String firstName, String lastName, 
								String ccid, String expDate) throws Exception {
		/* Returns true if the customer entered a valid credit card. */
		
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		
		if (firstName == null || lastName == null || ccid == null || expDate == null || 
				firstName.equalsIgnoreCase("") || lastName.equalsIgnoreCase("") || 
				ccid.equalsIgnoreCase("") || expDate.equalsIgnoreCase(""))
			return false;
			
		String query = String.format("SELECT cc.first_name, cc.last_name "
				+ "FROM creditcards cc, customers cust "
				+ "WHERE cc.first_name = '%s' AND cc.last_name = '%s' "
				+ "AND cc.id = '%s' AND cc.expiration = '%s' AND cc.id = cust.cc_id", 
				firstName, lastName, ccid, expDate);
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			return result.next();		// returns true if ResultSet is not empty

		} finally {
			close(connection, statement, result);
		}
	}

	public Movie getMovie(String movieId) throws Exception{
		/* 
		 * Gets a single movie from the database by movieId
		 * and returns a new Movie object. 
		 */
		
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		Movie movie = null;
		
		if (movieId == null)
			return null;
		
		try {
			String query = String.format("SELECT * "
					+ "FROM movies m WHERE m.id = %s", movieId);
			
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			
			if (result.next()) {
				movie = new Movie(Integer.parseInt(movieId), 
						result.getString("title"),
						result.getInt("year"), 
						result.getString("director"), 
						result.getString("banner_url"), 
						result.getString("trailer_url"), 
						movieGenres(Integer.parseInt(movieId)), 
						movieStars(Integer.parseInt(movieId)));
			}
		} finally {
			close(connection, statement, result);
		}
		return movie;
	}

	public Customer login(String email, String password) throws Exception {
		/* Temporarily implemented login function that returns a Customer object. */
		
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		Customer customer = null;
		
		if (email == null || password == null)
			return null;
		
		try {
			String query = String.format("SELECT * "
					+ "FROM customers c "
					+ "WHERE c.email = '%s' AND c.password = '%s'", email, password);
			
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			
			if (result.next()) {
				customer = new Customer(result.getInt("id"), 
										result.getString("first_name"),
										result.getString("last_name"), 
										result.getString("cc_id"),
										result.getString("address"), 
										email);
			}
			
		} finally {
			close(connection, statement, result);
		}
		
		return customer;
	}

	public int addSale(int customerId, int movieId, String currentDate) throws Exception {
		/* 
		 * Inserts a new sale into the "sales" table, 
		 * returning the number of records updated if successful. 
		 */
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		Sale sale = new Sale(0, customerId, movieId, currentDate);		// sale ID is auto-incremented in mySQL, hence the 0	
		int newRecords = 0;
		
		try {
			String query = String.format("INSERT INTO sales VALUES(%d, %d, %d, '%s')", 
										sale.getId(), sale.getCustomerId(), 
										sale.getMovieId(), sale.getSaleDate());
			
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			newRecords = statement.executeUpdate(query);
			
		} finally {
			close(connection, statement, result);
		}
		
		return newRecords;
	}
	

}
