package project2;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

public class SearchModelJdbc {
	
	private DataSource dataSource;
	
	public SearchModelJdbc(DataSource dataSource) {
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
	
	
	
	private String generateGeneralQuery(String keywords) {
		/* 
		 * Returns a mySQL query string of records that match keywords in any column. 
		 * %% is the escape character for %. 
		 */
		String query = null;
		if (keywords == null || keywords.equalsIgnoreCase(""))
			return null;
		else {
			query = "SELECT DISTINCT m.id, m.title, m.year, m.director, m.banner_url, m.trailer_url "
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
	
	private String generateQuery(String title, String year, String director, 
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
			query = "SELECT m.id, m.title, m.year, m.director, m.banner_url, m.trailer_url "
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
	
	private void addMovies(ResultSet result, ArrayList<Movie> movies) throws Exception {
		/* Helper function populating a list of movies returned from a query. */
		
		while (result.next()) {
			movies.add(new Movie(result.getInt("id"), result.getString("title"),
					result.getInt("year"), result.getString("director"), 
					result.getString("banner_url"), result.getString("trailer_url")));
		}
	}
	
	
	public ArrayList<Movie> moviesByKeywords(String keywords) throws Exception {
		
		/* 
		 * Establishes an individual connection to the database 
		 * and retrieves records from the database that match 
		 * the given mySQL query. 
		 */
		
		ArrayList<Movie> movies = new ArrayList<Movie>();
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		String query = null;
		
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			query = this.generateGeneralQuery(keywords);
			
			if (query != null) {
				result = statement.executeQuery(query);
				addMovies(result, movies);
			}
		} finally {
			close(connection, statement, result);
		}
		return movies;
	}
	
	public ArrayList<Movie> searchMovies(String title, String year, 
			String director, String firstName, String lastName) throws Exception {
		
		/* 
		 * Establishes an individual connection to the database 
		 * and retrieves records from the database that match 
		 * the given mySQL query. 
		 */
		
		ArrayList<Movie> movies = new ArrayList<Movie>();
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		String query = null;
		
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			query = this.generateQuery(title, year, director, firstName, lastName);
			
			if (query != null) {		
				result = statement.executeQuery(query);
				addMovies(result, movies);
			}
			
		} finally {
			close(connection, statement, result);
		}
		return movies;
	}


}
