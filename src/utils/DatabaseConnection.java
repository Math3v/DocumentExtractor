package utils;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @class DatabaseConnection
 * @author matej
 * 
 * Connect do database. Singleton.
 */
public class DatabaseConnection {

	private static Connection connection = null;
	
	private String url  = "jdbc:postgresql://localhost:5432/xminar29_drugs_db";
	private String user = "xminar29";
	private String pass = "xminar29";
	
	private DatabaseConnection() {
		connect();
	}
	
	public static Connection getConnection(){
		if( null == connection ) {
			new DatabaseConnection();
		}
		
		return connection;
	}
	
	private void connect() {
		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(url, user, pass);
			if( connection.isValid(2 /* seconds */) == false ) {
				System.err.println("ERROR: Connection invalid");
			}
		} catch(Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(1);
		}
	}
	
}
