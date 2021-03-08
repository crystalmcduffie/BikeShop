package com.revature.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//import com.revature.data.BikeDAO;
//import com.revature.data.BikeDAOImpl;

/**
 * Class used to retrieve DAO Implementations. Serves as a factory. Also manages a single instance of the database connection.
 */
public class DAOUtilities {

	private static final String CONNECTION_USERNAME = "postgres";
	private static final String CONNECTION_PASSWORD = "0dragon0";
	private static final String URL = "jdbc:postgresql://localhost:5432/BikeShop";
	private static Connection connection;
	
	public static synchronized Connection getConnection() throws SQLException {
		if (connection == null) {
			try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException e) {
				System.out.println("Could not register driver!");
				e.printStackTrace();
			}
			connection = DriverManager.getConnection(URL, CONNECTION_USERNAME, CONNECTION_PASSWORD);
		}
		
		//If connection was closed then retrieve a new connection
		if (connection.isClosed()){
			System.out.println("Opening new connection...");
			connection = DriverManager.getConnection(URL, CONNECTION_USERNAME, CONNECTION_PASSWORD);
		}
		return connection;
	}
	
//	public static BikeDAO getBikeDAO() {
//		return new BikeDAOImpl();
//	}
	
//	public static BookDAO getBookDAO() {
//		return new BookDAOImpl();
//	}
//	
//	public static TagDAO getTagDAO() {
//		return new TagDAOImpl();
//	}

}
