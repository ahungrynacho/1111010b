package jdbc;

public class Main {    
	public static void main(String[] args) {
		try {
			JDBC jdbc = new JDBC();
			jdbc.run();
		} catch (Exception e) {
			System.out.println("JDBC instantiation error");
		}		
	}	
}
