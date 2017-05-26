package fabflix.cinephim;

/**
 * Created by Brian B. Huynh on 5/21/2017.
 */

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FabflixModelJdbc {
    private String IP = "54.245.41.15:3306";
    private String DATABASE = "moviedb";
    private String USER = "android-user";
    private String PASSWORD = "spring2017";
    private String URL = String.format("jdbc:mysql://%s/%s?autoReconnect=true&useSSL=false", IP, DATABASE);

    public FabflixModelJdbc() throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
    }

    public Customer login(String email, String password) throws Exception {
		/* Implemented login function that returns a Customer object. */

        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        Customer customer = null;

        if (email == null || password == null)
            return null;

        try {
            String query = String.format("SELECT * "
                            + "FROM customers c "
                            + "WHERE c.email = '%s' AND c.password = '%s'",
                    email, password);

            connection = DriverManager.getConnection(URL, USER, PASSWORD);
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

    private List<Movie> movieList(ResultSet result) throws Exception {
		/* Helper function populating a list of movies returned from a query. */

        List<Movie> movies = new ArrayList<Movie>();
        while (result.next()) {
            int movieId = result.getInt("id");
            movies.add(new Movie(movieId,
                    result.getString("title"),
                    result.getInt("year"),
                    result.getString("director"),
                    result.getString("banner_url"),
                    result.getString("trailer_url"),
                    null,
                    null)
            );
        }
        return movies;
    }

    public List<Movie> searchByTitle(String keywords) throws Exception {
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        String query = null;

        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            statement = connection.createStatement();
            query = String.format("SELECT * " +
                    "FROM movies m " +
                    "WHERE m.title LIKE '%s%%'", keywords);

            result = statement.executeQuery(query);
            return movieList(result);

        } finally {
            close(connection, statement, result);
        }
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
}
