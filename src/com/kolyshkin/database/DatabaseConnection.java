package com.kolyshkin.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
	  static final String JDBC_DRIVER= "org.postgresql.Driver";
	  static final String DB_URL= "jdbc:postgresql://ec2-174-129-221-240.compute-1.amazonaws.com:5432/dbc4304gs0ck88?sslmode=require";
	  static final String USER= "npczqgmvapxjax";
	  static final String PASS= "30e8042bfb209c4d52e1d1e6cd5f09f8e6df75da647c607ba1445d27dd59f013";
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
