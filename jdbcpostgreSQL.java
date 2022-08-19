import java.sql.*;
import java.io.*;
import java.security.spec.EdDSAParameterSpec;
import java.util.Scanner;

// import jdk.javadoc.internal.tool.Start;

/*
CSCE 315
9-27-2021 Lab
 */
public class jdbcpostgreSQL {

    // Commands to run this script
    // This will compile all java files in this directory
    // javac *.java 
    // This command tells the file where to find the postgres jar which it needs to execute postgres commands, then executes the code
    // java -cp “.;postgresql-42.2.8.jar” jdbcpostgreSQL

    // MAKE SURE YOU ARE ON VPN or TAMU WIFI TO ACCESS DATABASE
    public static void main(String args[]) {
        /*  #########################  CONNECTING TO THE DATABASE  ######################### */
        // Building the connection with your credentials
        // TODO: update dbName, userName, and userPassword here
        Connection conn = null;
        String teamNumber = "3";
        String sectionNumber = "904";
        String dbName = "csce315" + sectionNumber + "_" + teamNumber + "db";
        String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;
        String userName = "csce315" + sectionNumber + "_" + teamNumber + "user";
        String userPassword = "group3pass";

        // Connecting to the database 
        try {
            conn = DriverManager.getConnection(dbConnectionString,userName, userPassword);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }

        System.out.println("Opened database successfully");


        /*  ###############################  NAMES TABLE  ###############################  */

        String new_Table = "CREATE TABLE names (Name_ID TEXT PRIMARY KEY, Primary_Name TEXT NOT NULL, Birth_Year Integer, Death_Year Integer, Profession TEXT)";
        send_Request(new_Table, conn);

        Scanner sc;
        String request = "INSERT INTO names VALUES ";
        try {
            sc = new Scanner(new File("clean_data/CleanNames.csv"));
            sc.useDelimiter("\t|\n");
            sc.nextLine();
            int i = 0;
            while (sc.hasNext()) {
                String Name_ID = sc.next();
                String Primary_Name = sc.next();
                if (Primary_Name.contains("\'")) {
                    Primary_Name = Primary_Name.replace("\'", "''");
                }
                String Birth_Year = sc.next();
                if (Birth_Year.equals("")) {
                    Birth_Year = "NULL";
                }
                String Death_Year = sc.next();
                if (Death_Year.equals("")) {
                    Death_Year = "NULL";
                }
                String Primary_Profession = sc.next();

                
                // send_Request(String.format("INSERT INTO names VALUES ('%s', '%s', %s, %s, '%s') ON CONFLICT DO NOTHING", Name_ID, Primary_Name, Birth_Year, Death_Year, Primary_Profession), conn);
                // System.out.println(String.format("INSERT INTO names VALUES ('%s', '%s', %s, %s, '%s') ON CONFLICT DO NOTHING", Name_ID, Primary_Name, Birth_Year, Death_Year, Primary_Profession));
                i++;
                if (i % 15000 == 0 || !sc.hasNext()) { 
                    request += String.format("('%s', '%s', %s, %s, '%s') ON CONFLICT DO NOTHING", Name_ID, Primary_Name, Birth_Year, Death_Year, Primary_Profession);
                    System.out.println("line " + i + " of names");
                    send_Request(request, conn);
                    request = "INSERT INTO names VALUES ";
                } else {
                    request += String.format("('%s', '%s', %s, %s, '%s'), ", Name_ID, Primary_Name, Birth_Year, Death_Year, Primary_Profession);

                }
            }
        } catch (Exception e) {
            System.out.println("Error");
        }
     
        // send_Request("INSERT INTO teammembers (student_name, section, favorite_movie, favorite_holiday) VALUES ('Cranjis McBasketball', 904, 'Monsters', '01/01/01')", conn);
    
        /*  ###############################  CUSTOMER TABLE  ###############################  */
        new_Table = "CREATE TABLE customer (Customer_ID TEXT, Rating INTEGER, Date DATE, Title_ID TEXT, PRIMARY KEY (Customer_ID, Title_ID))";
        send_Request(new_Table, conn);

        String IDs_For_Analyst = "";

        request = "INSERT INTO customer VALUES ";
        try {
            sc = new Scanner(new File("clean_data/user.csv"));
            sc.useDelimiter("\t|\n");
            sc.nextLine();
            int i = 0;
            while (sc.hasNext()) {
                String Customer_ID = sc.next();
                String Rating = sc.next();
                String Date = sc.next();
                String Title_ID = sc.next();
                
                i++;
                if (i % 15000 == 0 || !sc.hasNext()) { 
                    if (sc.hasNext()) {
                        IDs_For_Analyst += Customer_ID + ", ";
                    }
                    IDs_For_Analyst += Customer_ID;
                    request += String.format("('%s', %s, '%s', '%s') ON CONFLICT DO NOTHING", Customer_ID, Rating, Date, Title_ID);
                    System.out.println("line " + i + " of customer");
                    send_Request(request, conn);
                    request = "INSERT INTO customer VALUES ";
                } else {
                    request += String.format("('%s', %s, '%s', '%s'), ", Customer_ID, Rating, Date, Title_ID);
                }
            }
        } catch (Exception e) {
            System.out.println("Error");
        }

        /*  ###############################  ANALYST TABLE  ###############################  */
        new_Table = "CREATE TABLE analyst (Analyst_ID Integer PRIMARY KEY, Customer_IDs TEXT [])";
        send_Request(new_Table, conn);

        System.out.println(request);
        request = String.format("INSERT INTO analyst VALUES (101, '{%s}')", IDs_For_Analyst);
        send_Request(request, conn);

        /*  ###############################  CREW TABLE  ###############################  */
        new_Table = "CREATE TABLE crew (Title_ID VARCHAR(255) PRIMARY KEY, Director_ID TEXT [], Writer_ID TEXT [])";
        send_Request(new_Table, conn);

        request = "INSERT INTO crew VALUES ";
        try {
            sc = new Scanner(new File("clean_data/crew.csv"));
            sc.useDelimiter("\t|\n");
            sc.nextLine();
            int i = 0;
            while (sc.hasNext()) {
                String Title_ID = sc.next();
                String Directors_ID = sc.next();
                if (Directors_ID.equals("")) {
                    Directors_ID = "NULL";
                }
                String Writers_ID = sc.next();
                if (Writers_ID.equals("")) {
                    Writers_ID = "NULL";
                }
                                
                i++;
                if (i % 15000 == 0 || !sc.hasNext()) { 
                    request += String.format("('%s', '{%s}', '{%s}') ON CONFLICT DO NOTHING", Title_ID, Directors_ID, Writers_ID);
                    System.out.println("line " + i + " of crew");
                    send_Request(request, conn);
                    request = "INSERT INTO crew VALUES ";
                } else {
                    request += String.format("('%s', '{%s}', '{%s}'), ", Title_ID, Directors_ID, Writers_ID);

                }
            }
        } catch (Exception e) {
            System.out.println("Error");
        }

        /*  ###############################  TITLES TABLE  ###############################  */
        new_Table = "CREATE TABLE titles (Title_ID TEXT PRIMARY KEY, Type TEXT, Title TEXT, Year INTEGER," +  
                    " runTime INTEGER, Genre TEXT, Avg_Rating NUMERIC(3,1), Num_Votes INTEGER)";
        send_Request(new_Table, conn);

        request = "INSERT INTO titles VALUES ";
        try {
            sc = new Scanner(new File("clean_data/titles.csv"));
            sc.useDelimiter("\t|\n");
            sc.nextLine();
            int i = 0;
            while (sc.hasNext()) {
                String Movie_ID = sc.next();
                String Title_Type = sc.next();
                String Title = sc.next();
                Title = Title.replace("'", "''");
                if (Title.equals("")) {
                    Title = "NULL";
                }
                String Year = sc.next();
                if (Year.equals("")) {
                    Year = "NULL";
                }
                String Runtime = sc.next();
                if (Runtime.equals("")) {
                    Runtime = "NULL";
                }
                String Genre = sc.next();
                if (Genre.equals("")) {
                    Genre = "NULL";
                }
                String Avg_Rating = sc.next();
                String Num_Votes = sc.next();
                
                                
                i++;
                if (i % 15000 == 0 || !sc.hasNext()) { 
                    request += String.format("('%s', '%s', '%s', %s, %s, '%s', %s, %s) ON CONFLICT DO NOTHING", Movie_ID, Title_Type, Title,  
                                                                                                        Year, Runtime, Genre, Avg_Rating, Num_Votes);
                    System.out.println("line " + i + " of titles");
                    send_Request(request, conn);
                    request = "INSERT INTO titles VALUES ";
                } else {
                    request += String.format("('%s', '%s', '%s', %s, %s, '%s', %s, %s), ", Movie_ID, Title_Type, Title,  
                                                                                           Year, Runtime, Genre, Avg_Rating, Num_Votes);

                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        /*  ###############################  PRINCIPALS TABLE  ###############################  */
        new_Table = "CREATE TABLE principals (Movie_ID TEXT, Name_ID TEXT PRIMARY KEY, Category TEXT, Job TEXT, Characters TEXT)";
        send_Request(new_Table, conn);

        request = "INSERT INTO principals VALUES ";
        try {
            sc = new Scanner(new File("clean_data/principals.csv"));
            sc.useDelimiter("\t|\n");
            sc.nextLine();
            int i = 0;
            while (sc.hasNext()) {
                String Movie_ID = sc.next();
                String Name_ID = sc.next();
                String Category = sc.next();
                String Job = sc.next();
                Job = Job.replace("'", "''");
                if (Job.equals("")) {
                    Job = "NULL";
                }
                String Characters = sc.next();
                Characters = Characters.replace("'", "''");
                if (Characters.equals("")) {
                    Characters = "NULL";
                }

                
                i++;
                if (i % 15000 == 0 || !sc.hasNext()) { 
                    request += String.format("('%s', '%s', '%s', '%s', '%s') ON CONFLICT DO NOTHING", Movie_ID, Name_ID, Category, Job, Characters);
                    System.out.println("line " + i + " of principals");
                    send_Request(request, conn);
                    request = "INSERT INTO principals VALUES ";
                } else {
                    request += String.format("('%s', '%s', '%s', '%s', '%s'), ", Movie_ID, Name_ID, Category, Job, Characters);

                }
            }
        } catch (Exception e) {
            System.out.println("Error");
        }

        //closing the connection
        try {
            conn.close();
            System.out.println("Connection Closed.");
        } catch(Exception e) {
            System.out.println("Connection NOT Closed.");
        }//end try catch
    }//end main

    public static void send_Request(String request, Connection conn) {
        try{
            // create a statement object
            Statement stmt = conn.createStatement();

            // Running a query
            // TODO: update the sql command here
            String sqlStatement = request;// ;
            
            // send statement to DBMS
            // This executeQuery command is useful for data retrieval
            // ResultSet result = stmt.executeQuery(sqlStatement);
            // OR
            // This executeUpdate command is useful for updating data
            int result = stmt.executeUpdate(sqlStatement);

            // OUTPUT
            // You will need to output the results differently depeninding on which function you use
//            System.out.println("--------------------Query Results--------------------");
            // while (result.next()) {
            // System.out.println(result.getString("column_name"));
            // }
            // OR
            // System.out.println(result);
        } catch (Exception e){
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }
}//end Class
