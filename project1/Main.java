package jdbc;
import java.sql.*;

public class Main {
	public static String USER = "bbhuynh";
	public static String PASSWORD = "spring2017";
    
	public static void main(String[] args) {
        // Incorporate mySQL driver
		
		try {
			JDBC jdbc = new JDBC();
			jdbc.login();
			jdbc.run();
			
//	        String query = "SELECT * FROM movies";
//	        Statement select = jdbc.CONNECTION.createStatement();
//	        ResultSet result = select.executeQuery(query);
//	        ResultSetMetaData metadata = result.getMetaData();
//	        
//	        while (result.next()) {
//	        	for (int i = 1; i <= metadata.getColumnCount(); ++i) {
//	        		System.out.println(metadata.getColumnName(i) + " = " + result.getString(i));
//	        	}
//	        	System.out.println();
//	        	
//	        }
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
	

}
