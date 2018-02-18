package com.kolyshkin;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.kolyshkin.database.DatabaseConnection;

public class Logic {
	public static ResultSet resultSet;
    public static InputStream inputStream;
    public static PreparedStatement prepStatement;
    public static Connection connection;
    public static String SQL;
    public static Statement statement;
    private static String databaseUserName;
	private static String databasePassword;

    public Logic() {
	}

    public String getDatabaseUserName() {
	    return databaseUserName;
    }

    public void setDatabaseUserName(String databaseUserName) {
	    Logic.databaseUserName = databaseUserName;
    }

    public String getDatabasePassword() {
	    return databasePassword;
    }

    public void setDatabasePassword(String databasePassword) {
	    Logic.databasePassword = databasePassword;
    }

	  public static ArrayList<Main> getUserList(){
		  try {
			Connection connect= DatabaseConnection.getConnection();
			String SQL= "SELECT * FROM Registration";
			PreparedStatement prepStatement= connect.prepareStatement(SQL);
			 ArrayList<Main> listofUsers= new ArrayList<>();
			  ResultSet resultSet= prepStatement.executeQuery();
			  boolean found= false;
			  while(resultSet.next()== true){
				  Main mainObj= new Main();
				  mainObj.setId(resultSet.getInt("id"));
				  mainObj.setUserName(resultSet.getString("userName"));
				  mainObj.setEmail(resultSet.getString("email"));
				  mainObj.setPassword(resultSet.getString("password"));
				  mainObj.setTimeStamp(resultSet.getTimestamp("timeStamp"));
				  listofUsers.add(mainObj);
				   found= true;
				 System.out.println("Rows retreived: "+listofUsers.size());
			  }
			  resultSet.close();
			  if (found) {
				return listofUsers;
			}else {
				return null; //no entries found!
			}
		} catch (Exception Ex) {
			System.out.println("Error in getUser()--> "+Ex.getMessage());
			return (null);
		}
	  }

	public static String insertNewData(Main insertUser){
		int c= 0;
		try {
			connection= DatabaseConnection.getConnection();
			System.out.println("Inserting records to the table...");
            SQL= "INSERT INTO Registration (userName,email,password,file,timeStamp) VALUES (?,?,?,?,?)";
            prepStatement= connection.prepareStatement(SQL);
            prepStatement.setString(1, insertUser.getUserName());
            prepStatement.setString(2, insertUser.getEmail());
            prepStatement.setString(3, insertUser.getPassword());
            System.out.println(insertUser.getFile().getName());
            inputStream = insertUser.getFile().getInputStream();
            prepStatement.setBinaryStream(4, inputStream);
          //create a java timestamp object that represents the current time ("current timestamp")
			Calendar calendar= Calendar.getInstance();
			Timestamp timeStamp= new Timestamp(calendar.getTime().getTime());
            prepStatement.setTimestamp(5, timeStamp);
            c= prepStatement.executeUpdate();
              System.out.println("User added Successfully!");
            FacesMessage message= new FacesMessage("User added!");
            FacesContext.getCurrentInstance().addMessage("signupForm: button", message);
            prepStatement.close();
            connection.close();
		} catch (SQLException SQLex) {
			// TODO: handle exception
			SQLex.printStackTrace();
		}catch (Exception IOex) {
			// TODO: handle exception
			IOex.printStackTrace();
		}
		if(c>0){
			return "welcome";
		}else {
			return "error";
		}
	}

	public static String checkUser(String userName, String password, Main check){
		boolean isAdmin= false;
		boolean isUser= false;
		try {
			connection= DatabaseConnection.getConnection();
			//Execute a querry
			System.out.println("Selecting records from the table...");
			statement= connection.createStatement();
			SQL= "SELECT userName,password FROM Registration WHERE userName= '"+userName+"' AND password= '"+password+"'";
			resultSet= statement.executeQuery(SQL);
			resultSet.next();
			 databaseUserName = resultSet.getString(1).toString();
			 databasePassword= resultSet.getString(2).toString();
			   if(((check.getUserName().equals(databaseUserName))&&(check.getUserName().equals("Kolyshkin")))&&
					   ((check.getPassword().equals(databasePassword))&&(check.getPassword().equals("12345")))){
				   isAdmin= true;
			   }else if ((check.getUserName().equals(databaseUserName))&&(check.getPassword().equals(databasePassword))){
				   isUser= true;
			   }
			  resultSet.close();
			  statement.close();
			  connection.close();
		} catch (Exception eX) {
			// TODO: handle exception
			System.out.println("There's an error in loginUser()--> "+eX.getMessage());
		}if(isAdmin){
			return "administration";
		}else if (isUser) {
			return "user";
		}else {
			return "error";
		}
	}

	public static String editUserRecord(int userId){
	  Main editRecord= null;
	System.out.println("editRecord(): userId: "+ userId);
	/*Setting the particular user details in session*/
	Map<String, Object> UserDetailsMap= FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	try {
		statement= DatabaseConnection.getConnection().createStatement();
		SQL= "SELECT * FROM Registration WHERE id= '"+userId+"'";
		resultSet= statement.executeQuery(SQL);
		if(resultSet!= null){
			resultSet.next();
		editRecord= new Main();
		editRecord.setId(resultSet.getInt("id"));
		editRecord.setUserName(resultSet.getString("userName"));
		editRecord.setEmail(resultSet.getString("email"));
		editRecord.setPassword(resultSet.getString("password"));
		// editRecord.setTimeStamp(resultSet.getTimestamp("timeStamp"));
		}
	 UserDetailsMap.put("mBean", editRecord);
	// FacesMessage message= new FacesMessage("Data retreived!");
	 FacesContext.getCurrentInstance().addMessage("editForm: grid", new FacesMessage(FacesMessage.SEVERITY_INFO, "Data retreived!", null));
	 resultSet.close();
	 statement.close();
	} catch (SQLException e) {
		// TODO: handle exception
		e.printStackTrace();
	}
		return "edit";
	}

	public static String updateUserRecord(Main updateRecord) {
		try {
			SQL= "UPDATE Registration SET userName=?, email=?, password=?, timeStamp=? WHERE id=?";
			prepStatement= DatabaseConnection.getConnection().prepareStatement(SQL);
			prepStatement.setString(1, updateRecord.getUserName());
			prepStatement.setString(2, updateRecord.getEmail());
			prepStatement.setString(3, updateRecord.getPassword());
			//create a java timestamp object that represents the current time ("current timestamp")
			Calendar calendar= Calendar.getInstance();
			Timestamp timeStamp= new Timestamp(calendar.getTime().getTime());
			prepStatement.setTimestamp(4, timeStamp);
			prepStatement.setInt(5, updateRecord.getId());
			prepStatement.executeUpdate();
			System.out.println("User updated Successfully!");
            FacesMessage message= new FacesMessage("User updated!");
            FacesContext.getCurrentInstance().addMessage("adminForm: head", message);
			prepStatement.close();
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "administration";
	}

	public static String insertNewUser(Main newUser){
		try {
			Connection connection= DatabaseConnection.getConnection();
			System.out.println("Inserting records to the table...");
            String SQL= "INSERT INTO Registration (userName,email,password,file,timeStamp) VALUES (?,?,?,?,?)";
            PreparedStatement prepStatement= connection.prepareStatement(SQL);
            prepStatement.setString(1, newUser.getUserName());
            prepStatement.setString(2, newUser.getEmail());
            prepStatement.setString(3, newUser.getPassword());
            System.out.println(newUser.getFile().getName());
            inputStream = newUser.getFile().getInputStream();
            prepStatement.setBinaryStream(4, inputStream);
          //create a java timestamp object that represents the current time ("current timestamp")
			Calendar calendar= Calendar.getInstance();
            Timestamp timeStamp= new Timestamp(calendar.getTime().getTime());
            prepStatement.setTimestamp(5, timeStamp);
            prepStatement.executeUpdate();
            System.out.println("User added Successfully!");
            FacesMessage message= new FacesMessage("New user added!");
            FacesContext.getCurrentInstance().addMessage("adminForm: buttonAdd", message);
            prepStatement.close();
            connection.close();
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "administration";
	}

	public static String deleteUser(int id){
		if(id!= 0){
			try {
				connection= DatabaseConnection.getConnection();
				SQL= "DELETE FROM Registration WHERE id='"+id+"'";
				prepStatement= connection.prepareStatement(SQL);
				int y= prepStatement.executeUpdate();
				System.out.println("y= "+y);
				 if(y> 0){
					 System.out.println("Row deleted successfully!");
					 FacesMessage message= new FacesMessage("Row deleted!");
					 FacesContext.getCurrentInstance().addMessage("adminForm: buttonDel", message);
				 }
				 prepStatement.close();
				 connection.close();
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("There's an error in dataDelete()--> "+e.getMessage());
			}
		}
		return "administration";
	}
}
