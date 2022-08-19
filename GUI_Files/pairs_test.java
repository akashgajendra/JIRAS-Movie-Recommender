import java.sql.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
// import org.knowm.xchart.*;
import java.util.List;
import java.util.ArrayList;

public class pairs_test {
  public static void main(String[] args)
  {
    //Building the connection
    Connection conn = null;
    //TODO STEP 1
    try {
      Class.forName("org.postgresql.Driver");
      conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315904_3db",
        "csce315904_3user", "group3pass");
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(e.getClass().getName()+": "+e.getMessage());
      System.exit(0);
    }
    JOptionPane.showMessageDialog(null,"Opened database successfully");

    String name = "";
    try{
      //create a statement object
      Statement stmt = conn.createStatement();
      String sqlStatement = "SELECT * FROM titles LIMIT 10";
      //send statement to DBMS
      ResultSet result = stmt.executeQuery(sqlStatement);
      while (result.next()) {
        name += result.getString("title")+"\n";
      }
    } catch (Exception e){
      JOptionPane.showMessageDialog(null,"Error accessing Database. " + e.getMessage());
    }
  }
}