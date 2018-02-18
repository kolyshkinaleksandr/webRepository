package com.kolyshkin.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
	  static final String JDBC_DRIVER= "com.mysql.jdbc.Driver";
	  static final String DB_URL= "jdbc:mysql://localhost/imageUsers";
	  static final String USER= "Kolyshkin Al";
	  static final String PASS= "Kolyshkin_0782";
	public static Connection getConnection() {
		try {
			Class.forName(JDBC_DRIVER);
			//open connection
			   System.out.println("Connecting to a selected database...");
			Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
			   System.out.println("Connected database successfully...");
		return connection;
	 }catch(SQLException sqlEx){
		 sqlEx.printStackTrace();//handle errors for jdbc
		 System.out.println("Exeption occured in the process: "+sqlEx);
	 }catch(Exception ex){
		 ex.printStackTrace();//handle errors for ClassForName
		 System.out.println("Exception occured in the process: "+ex);
		}
		return null;
	}

	public static void close(Connection connect) {
		try {
			connect.close();
		System.out.println("DataBase Connection closed!!!");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
