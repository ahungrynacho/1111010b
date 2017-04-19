package project2;
import java.sql.*;
import java.util.ArrayList;

public class SearchModelJdbc {
	private static String URL = "jdbc:mysql:///moviedb?autoReconnect=true&useSSL=false";
	private static String USER = null;		// bbhuynh
	private static String PASSWORD = null;		// spring2017
	private static String[] TABLES = {
			"movies", "stars", "stars_in_movies",
			"genres", "genres_in_movies", "customers",
			"sales", "creditcards"
			};
	
	public SearchModelJdbc() throws Exception {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
	}
	
	private void close(Connection connection, 
			Statement statement, 
			ArrayList<ResultSet> result) {
		try {
			if (connection != null)
				connection.close();
			if (statement != null)
				statement.close();
			if (result != null) {
				for (ResultSet r : result) {
					r.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private String searchByTitle(String title) {
		return String.format("SELECT * FROM movies m WHERE m.title = '%s'", title);
	}
	
	private String searchByYear(int year) {
		return String.format("SELECT * FROM movies m WHERE m.year = %d", year);
	}
	
	private String searchByDirector(String director) {
		return String.format("SELECT * FROM movies m WHERE m.director = '%s'", director);
	}
	
	private String searchByFirstName(String name) {
		return String.format("SELECT * FROM movies m, stars s, stars_in_movies sim "
							+ "WHERE s.first_name LIKE '%%1$s%%' AND "
							+ "m.id = sim.movie_id AND s.id = sim.star_id", name);
	}
	
	private String searchByLastName(String name) {
		return String.format("SELECT * FROM movies m, stars s, stars_in_movies sim "
							+ "WHERE s.last_name LIKE '%%1$s%%' AND "
							+ "m.id = sim.movie_id AND s.id = sim.star_id", name);
	}
	
	private String searchByFullName(String name) {
		return String.format("SELECT * FROM movies m, stars s, stars_in_movies sim "
							+ "WHERE s.first_name LIKE '%%1$s%%' AND "
							+ "s.last_name LIKE '%%1$s%%' AND "
							+ "m.id = sim.movie_id AND s.id = sim.star_id", name);
	}
	
	private String generateQuery(String title, String year, String director, 
								String firstName, String lastName) {
		
		/* Dynamically generates mySQL query strings. */
		String[] argv = {title, year, director, firstName, lastName};
		String[] queries = {
				String.format("m.title = '%%1$s%%'", title),
				String.format("m.year = %s", year),
				String.format("m.director = '%%1$s%%'", director),
				String.format("s.first_name LIKE '%%1$s%%' AND "
						+ "m.id = sim.movie_id AND s.id = sim.star_id", firstName),
				String.format("s.last_name LIKE '%%1$s%%' AND "
						+ "m.id = sim.movie_id AND s.id = sim.star_id", lastName)
				};
		
//		String matchFullName = String.format("s.first_name LIKE '%%1$s%%' AND "
//				+ "s.last_name LIKE %%1$s%%' AND "
//				+ "m.id = sim.movie_id AND s.id = sim.star_id", firstName);
		
		String query = "SELECT * "
				+ "FROM movies m, stars s, stars_in_movies sim "
				+ "WHERE ";
		boolean firstAttribute = true;
		
		for (int i = 0; i < argv.length; ++i) {
			if (argv[i] != null && firstAttribute) {
				query += queries[i];
				firstAttribute = false;
			}
			else if (argv[i] != null) {
				query += " AND " + queries[i];
			}
		}
		
		return query;
	}
	
	
	private String searchByKeywords(String keywords) {
		return String.format("SELECT * FROM movies m, stars s, stars_in_movies sim "
			+ "WHERE m.title LIKE '%%1$s%%' OR m.director LIKE '%%1$s%%' OR "
			+ "(s.id = sim.star_id AND m.id = sim.movie_id AND "
			+ "(s.first_name LIKE '%%1$s%%' OR s.last_name LIKE '%%1$s%%'))", keywords);
	}
	
	public ArrayList<Movie> getMovies(String title, String year, 
			String director, String firstName, String lastName) throws Exception {
		
		String[] args = {title, year, director, firstName, lastName};
		ArrayList<Movie> movies = new ArrayList<Movie>();
		ArrayList<ResultSet> results = new ArrayList<ResultSet>();
		
		Connection connection = null;
		Statement statement = null;
		
		try {
			connection = DriverManager.getConnection(SearchModelJdbc.URL,
														SearchModelJdbc.USER,
														SearchModelJdbc.PASSWORD);
			statement = connection.createStatement();
			
			for (String arg : args) {
				if (arg != null) {
					String query = null;
					if (arg.equalsIgnoreCase(title))
						query = searchByTitle(title);
					else if (arg.equalsIgnoreCase(year))
						query = searchByYear(Integer.parseInt(year));
					else if (arg.equalsIgnoreCase(director))
						query = searchByDirector(director);
					else if (arg.equalsIgnoreCase(firstName))
						query = searchByFirstName(firstName);
					else if (arg.equalsIgnoreCase(lastName))
						query = searchByLastName(lastName);

					if (query != null)
						results.add(statement.executeQuery(query));
				}
			}
			
			// JOIN TABLES by matching movie ids 
			for (ResultSet r : results) {
				
			}

		} finally {
			close(connection, statement, results);
		}
		return movies;
	}


}
