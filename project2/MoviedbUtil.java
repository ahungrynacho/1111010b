package project2;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.*;
public class MoviedbUtil {
	
	private DataSource dataSource;
	
	public MoviedbUtil(DataSource theDataSource) {
		dataSource = theDataSource;	
	}
	
	public List<Movie> getMovies(String sort, String order, Integer limit, int page,
			String browseTitle, String browseGenre) throws Exception {
		
		
		List<Movie> movies = new ArrayList<>();
		
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		String nextTitle= "";
		String sql = "";
		List<Genre> genres = new ArrayList<Genre>();
		List<Star> star = new ArrayList<Star>();
		List<Integer> tempStarId = new ArrayList<Integer>();
		List<String> genreList = new ArrayList<String>();
		
		try {
			// get a connection
			myConn = dataSource.getConnection();
			
			if (browseGenre!=null){
				if(browseGenre.equals("Adventure"))
					browseGenre = "Adventure' or g.name = 'advanture'";
				else if(browseGenre.equals("Comedy"))
					browseGenre = "Comedy' or g.name = 'Commedy'";
				else if(browseGenre.equals("Epics/Historical"))
					browseGenre = "Epics/Historical' or g.name = 'Epics/Historial'";
				else if(browseGenre.equals("Musical"))
					browseGenre = "Musical' or g.name = 'musial' or g.name = 'Musicals'";
				else if(browseGenre.equals("Science Fiction")){
					browseGenre = "Science Fiction' or g.name = 'Sci-Fi' or "
							+ "g.name = 'SCI/FI' or g.name = 'SciFi'";
				}
				else if(browseGenre.equals("Western"))
					browseGenre = "Western or g.name = Westerns";
				else
					browseGenre = browseGenre.concat("'");

				sql = String.format("select m.id, m.title, m.year, m.director, s.first_name,"
					+ "s.last_name, s.id AS s_Id, g.name, g.id AS gID from movies m, genres_in_movies gim, stars s,"
					+ " stars_in_movies sim, genres g where "
					+ "sim.star_id = s.id and sim.movie_id = m.id and g.id = gim.genre_id "
					+ "and gim.movie_id = m.id and (g.name = '%s)",browseGenre);
			}
			
			if(browseTitle!=null){
				browseTitle = browseTitle.concat("%'");
				sql = "select m.id, m.title, m.year, m.director, s.first_name,"
						+ "s.last_name, s.id AS s_Id, g.name, g.id AS gID from movies m, genres_in_movies gim, stars s,"
						+ " stars_in_movies sim, genres g where "
						+ "sim.star_id = s.id and sim.movie_id = m.id and g.id = gim.genre_id "
						+ "and gim.movie_id = m.id and m.title LIKE '" + browseTitle;
			}
					
			myStmt = myConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
			        ResultSet.CONCUR_READ_ONLY);
			
			// execute query
			myRs = myStmt.executeQuery(sql);
			
			// process result set
			while (myRs.next()) {
				
				// Moves RS cursor forward, used to check if current RS row is dupilcate
				if (!myRs.isLast()) {
				myRs.next();
				nextTitle = myRs.getString("title");
				myRs.previous();
				}
				else {
					nextTitle = "";
				}
				

				String title = myRs.getString("title");
				int id = myRs.getInt("id");
				int sId = myRs.getInt("s_Id");
				int year = myRs.getInt("year");
				String director = myRs.getString("director");
				String genreName = myRs.getString("name");
				int genreId = myRs.getInt("gID");
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				
				if(!tempStarId.contains(sId)) {
					Star tempStar = new Star(sId, firstName, lastName);
					star.add(tempStar);
					tempStarId.add(sId);
				}
				
				if (!genreList.contains(genreName)) {
					genreList.add(genreName);
					Genre tempGenres = new Genre(genreId,genreName);
					genres.add(tempGenres);
				}
					
					
			// create new student object
				if(!title.equals(nextTitle)){
					Movie tempMovie = new Movie(id, title, director, year, genres, star);
					
					// add it to the list of students
					movies.add(tempMovie);	
					tempStarId = new ArrayList<Integer>();
					genreList = new ArrayList<String>();
					star = new ArrayList<Star>();
					genres = new ArrayList<Genre>();
				}
			}
			
			return movies;		
		}
		finally {
			// close JDBC objects
			close(myConn, myStmt, myRs);
		}		
	}
	
	public Movie getSingleMovie(String movieid) throws Exception {
		
		Movie singleMovie = new Movie();
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		String nextTitle= "";

		List<Genre> genres = new ArrayList<Genre>();
		List<Star> star = new ArrayList<Star>();
		List<Integer> tempStarId = new ArrayList<Integer>();
		List<String> genreList = new ArrayList<String>();
		
		try {
			// get a connection
			myConn = dataSource.getConnection();

			// create sql statement
		
			
			String sql =String.format("select m.id, m.title, m.year, m.director,s.id AS s_Id, s.first_name,"
					+ "s.last_name, g.name,g.id AS gId, m.banner_url, m.trailer_url from movies m, genres_in_movies gim, stars s,"
					+ " stars_in_movies sim, genres g where "
					+ "(sim.star_id = s.id and sim.movie_id = m.id and g.id = gim.genre_id "
					+ "and gim.movie_id = m.id and m.id = '%s')",movieid);
			
			myStmt = myConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
			        ResultSet.CONCUR_READ_ONLY);
			
			// execute query
			myRs = myStmt.executeQuery(sql);
			while(myRs.next()) {
					
					
					if (!myRs.isLast()) {
					myRs.next();
					nextTitle = myRs.getString("title");
					myRs.previous();
					}
					else{
						nextTitle = "";
					}
					

					String title = myRs.getString("title");
					int id = myRs.getInt("id");
					int year = myRs.getInt("year");
					int sId = myRs.getInt("S_Id");
					String director = myRs.getString("director");
					String genreName = myRs.getString("name");
					int genreId = myRs.getInt("gId");
					String firstName = myRs.getString("first_name");
					String lastName = myRs.getString("last_name");
					String banner_url = myRs.getString("banner_url");
					String trailer_url = myRs.getString("trailer_url");
					
					if(!tempStarId.contains(sId)) {
						Star tempStar = new Star(sId, firstName, lastName);
						star.add(tempStar);
						tempStarId.add(sId);
						
					}
					
					if (!genreList.contains(genreName)) {
						genreList.add(genreName);
						Genre tempGenres = new Genre(genreId,genreName);
						genres.add(tempGenres);
					}
						
						
				// create new student object
					if(!title.equals(nextTitle)){
						singleMovie = new Movie(id, title, year, director, banner_url, trailer_url, genres, star);

						star = new ArrayList<Star>();
						genres = new ArrayList<Genre>();
						genreList = new ArrayList<String>();
						tempStarId = new ArrayList<Integer>();
					}
				}
			
			
			
			
			return singleMovie;
		}
			finally {
				// close JDBC objects
				close(myConn, myStmt, myRs);
			}
	}
	
public Star getSingleStar(String starId) throws Exception {
		
		Star star = new Star();
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		String nextTitle= "";

		List<Movie> movie = new ArrayList<Movie>();
		List<String> titleList = new ArrayList<String>();
		
		try {
			// get a connection
			myConn = dataSource.getConnection();

			// create sql statement	
			String sql =String.format("select s.id, s.dob, s.first_name, s.last_name, s.photo_url, m.id AS m_Id, m.title "
					+ "from movies m, stars s, stars_in_movies sim "
					+ "where sim.star_id = s.id and sim.movie_id = m.id and s.id = '%s'",starId);
			
			myStmt = myConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
			        ResultSet.CONCUR_READ_ONLY);
			
			// execute query
			myRs = myStmt.executeQuery(sql);
			while(myRs.next()) {
					
					
					if (!myRs.isLast()) {
					myRs.next();
					nextTitle = myRs.getString("title");
					myRs.previous();
					}
					else{
						nextTitle = "";
					}
					

					String title = myRs.getString("title");
					int id = myRs.getInt("id");
					int mId = myRs.getInt("m_Id");
					String firstName = myRs.getString("first_name");
					String lastName = myRs.getString("last_name");
					String dob = myRs.getString("dob");
					String photoUrl = myRs.getString("photo_url");
					
					if(!titleList.contains(title)) {
						titleList.add(title);
						Movie tempMovie = new Movie(mId, title);
						movie.add(tempMovie);
					}
					
				// create new student object
					if(!title.equals(nextTitle)){
						 star = new Star(id, firstName, lastName, dob, photoUrl, movie);
						
						// add it to the list of students
						
					}
				}

			return star;
		}
			finally {
				// close JDBC objects
				close(myConn, myStmt, myRs);
			}
	}
	
	private void close(Connection myConn, Statement myStmt, ResultSet myRs) {

		try {
			if (myRs != null) {
				myRs.close();
			}
			
			if (myStmt != null) {
				myStmt.close();
			}
			
			if (myConn != null) {
				myConn.close();   // doesn't really close it ... just puts back in connection pool
			}
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
	}
}