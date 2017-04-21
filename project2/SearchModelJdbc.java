package project2;
import java.sql.*;
import java.util.ArrayList;

import javax.sql.DataSource;

public class SearchModelJdbc {
	
	private DataSource dataSource;
	
	public SearchModelJdbc(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	private void close(Connection connection, 
			Statement statement, ResultSet result) {
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
	
	public String generateQuery(String title, String year, String director, 
								String firstName, String lastName) {
		
		/* Dynamically generates mySQL query strings. */
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
		
		if (title == null && year == null && director == null 
				&& firstName == null && lastName == null) {
			return null;
		}
		else if (firstName != null || lastName != null)
			query = "SELECT m.id, m.title, m.year, m.director, m.banner_url, m.trailer_url "
					+ "FROM movies m, stars s, stars_in_movies sim "
					+ "WHERE ";
		else
			query = "SELECT * "
					+ "FROM movies m "
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
	
	public ArrayList<Movie> searchMovies(String title, String year, 
			String director, String firstName, String lastName) throws Exception {
		
		ArrayList<Movie> movies = new ArrayList<Movie>();
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		String query = null;
		
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			
			// query = this.generateQuery(title, year, director, firstName, lastName);
			query = "SELECT m.id, m.title, m.year, m.director, m.banner_url, m.trailer_url "
					+ "FROM movies m, stars s, stars_in_movies sim "
					+ "WHERE s.first_name LIKE '%tom%' AND m.id = sim.movie_id AND s.id = sim.star_id";
			
			if (query != null) {
				result = statement.executeQuery(query);
				
				while (result.next()) {
					movies.add(new Movie(result.getInt("id"), result.getString("title"),
										result.getInt("year"), result.getString("director"), 
										result.getString("banner_url"), result.getString("trailer_url")));
				}
			}
			return movies;
		} finally {
			close(connection, statement, result);
		}
		
	}


}
