// Compile: javac -cp ".;postgresql-42.2.8.jar" Viewer.java
// Run: java -cp ".;postgresql-42.2.8.jar" Viewer <user-id>

import javax.swing.border.BevelBorder;
import java.sql.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

class MenuActionListener implements ActionListener {
  public void actionPerformed(ActionEvent e) {
    System.out.println("Selected: " + e.getActionCommand());

  }
}

public class Viewer extends JFrame {

    public static void main(String[] args) {
        // attempt to connect to database
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315904_3db",
            "csce315904_3user", "group3pass");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            JOptionPane.showMessageDialog(null,"Failed to open database");
            System.exit(0);
        }
        final Connection finalconn = conn;
        JOptionPane.showMessageDialog(null,"Opened database successfully");

        // main GUI
        final JFrame f = new JFrame("Viewer GUI");

        // grab list of movies via queries
        String trending_Movies[] = get_Movie(conn, "trending", args[0]);
        String directors_Movies[] = get_Movie(conn, "directors", args[0]);
        String actors_Movies[] = get_Movie(conn, "actors", args[0]);
        String viewers_choice[] = get_Movie(conn, "viewers_choice", args[0]);
        String viewers_beware[] = get_Movie(conn, "viewers_beware", args[0]);

        // scroll panes with corresponding panels
        JScrollPane jScrollPane1 = new JScrollPane();
        JPanel panel1 = new JPanel();
        JScrollPane jScrollPane2 = new JScrollPane();
        JPanel panel2 = new JPanel();
        JScrollPane jScrollPane3 = new JScrollPane();
        JPanel panel3 = new JPanel();
        JScrollPane jScrollPane4 = new JScrollPane();
        JPanel panel4 = new JPanel();
        JScrollPane jScrollPane11 = new JScrollPane();
        JPanel panel11 = new JPanel();
        
        // containers
        JScrollPane jScrollPane6 = new JScrollPane();
        JScrollPane jScrollPane7 = new JScrollPane();
        JScrollPane jScrollPane8 = new JScrollPane();
        JScrollPane jScrollPane9 = new JScrollPane();
        JScrollPane jScrollPane10 = new JScrollPane();
        JScrollPane jScrollPane12 = new JScrollPane();

        // add movies to each panel
        panel1 = AddMoviesToPanels(trending_Movies, panel1);
        panel2 = AddMoviesToPanels(directors_Movies, panel2);
        panel3 = AddMoviesToPanels(actors_Movies, panel3);
        panel4 = AddMoviesToPanels(viewers_choice, panel4);
        panel11 = AddMoviesToPanels(viewers_beware, panel11);

        // set pref size for panels
        panel1.setPreferredSize(new Dimension(2050, 100));
        panel2.setPreferredSize(new Dimension(2050, 100));
        panel3.setPreferredSize(new Dimension(2050, 100));
        panel4.setPreferredSize(new Dimension(2050, 100));
        panel11.setPreferredSize(new Dimension(2050, 100));

        jScrollPane1.setPreferredSize(new Dimension(1000, 300));
        jScrollPane2.setPreferredSize(new Dimension(1000, 300));
        jScrollPane3.setPreferredSize(new Dimension(1000, 300));
        jScrollPane4.setPreferredSize(new Dimension(1000, 300));
        jScrollPane11.setPreferredSize(new Dimension(1000, 300));


        // headers
        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        JPanel p3 = new JPanel();
        JPanel p4 = new JPanel();
        JPanel p6 = new JPanel();

        p1.setLayout(new FlowLayout()); 
        p1.add(new JLabel("Trending Movies"));

        p2.setLayout(new FlowLayout()); 
        p2.add(new JLabel("Movies By Directors You Like"));

        p3.setLayout(new FlowLayout()); 
        p3.add(new JLabel("Movies Starring Actors You Like"));

        p4.setLayout(new FlowLayout()); 
        p4.add(new JLabel("Viewer's Choice - revisit movies you really liked"));

        p6.setLayout(new FlowLayout()); 
        p6.add(new JLabel("Viewers Beware"));
        
        // viewport stuff
        jScrollPane1.setViewportView(panel1);
        jScrollPane2.setViewportView(panel2);
        jScrollPane3.setViewportView(panel3);
        jScrollPane4.setViewportView(panel4);
        jScrollPane11.setViewportView(panel11);

        jScrollPane6.setViewportView(p1);
        jScrollPane7.setViewportView(p2);
        jScrollPane8.setViewportView(p3);
        jScrollPane9.setViewportView(p4);
        jScrollPane12.setViewportView(p6);


        // map everything out
        JPanel tempPanel = new JPanel();
        GroupLayout layout = new GroupLayout(tempPanel);

        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(p1)
            .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(p2)
            .addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(p3)
            .addComponent(jScrollPane3, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(p4)
            .addComponent(jScrollPane4, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(p6)
            .addComponent(jScrollPane11, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE));

        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(p1,25,25,25)
                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
                .addComponent(p2,25,25,25)
                .addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
                .addComponent(p3,25,25,25)
                .addComponent(jScrollPane3, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
                .addComponent(p4,25,25,25)
                .addComponent(jScrollPane4, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
                .addComponent(p6,25,25,25)
                .addComponent(jScrollPane11, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)));

        tempPanel.setLayout(layout);
       
        // Menu Bar
        JMenuBar menuBar = new JMenuBar();

        JMenu user_ID = new JMenu("User ID");
        user_ID.add(new JMenuItem(args[0]));
        menuBar.add(user_ID);

        JMenu watch_History = new JMenu("Watch History");
        menuBar.add(watch_History);

        JMenuItem interval1 = new JMenuItem("Within last year");
        JMenuItem interval2 = new JMenuItem("Within last two years");
        JMenuItem interval3 = new JMenuItem("All time");

        // listen to clicks and bring up corresponding screens
       interval1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JDialog jd = new JDialog(new JFrame());
                jd.setBounds(500, 300, 400, 300);
                String res= "Within the last year \n\n";
                JTextArea txt = new JTextArea(res);
                String[] temp = get_Movie(finalconn, "recent 1 year", args[0]);
                for (String a : temp)
                {
                    res = a;
                    txt.setText(txt.getText() + "\n");
                    txt.setText(txt.getText() + res);
                }
                jd.add(txt);
                jd.setVisible(true);
            }

        });
        interval2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JDialog jd = new JDialog(new JFrame());
                jd.setBounds(500, 300, 400, 300);
                String res= "Within the last two years \n\n";
                JTextArea txt = new JTextArea(res);
                String[] temp = get_Movie(finalconn, "recent 2 year", args[0]);
                for (String a : temp)
                {
                    res = a;
                    txt.setText(txt.getText() + "\n");
                    txt.setText(txt.getText() + res);
                }
                jd.add(txt);
                jd.setVisible(true);
            }

        });
        interval3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JDialog jd = new JDialog(new JFrame());
                jd.setBounds(500, 300, 400, 300);
                String res= "All-time \n\n";
                JTextArea txt = new JTextArea(res);
                String[] temp = get_Movie(finalconn, "all", args[0]);
                for (String a : temp)
                {
                    res = a;
                    txt.setText(txt.getText() + "\n");
                    txt.setText(txt.getText() + res);
                }
                jd.add(txt);
                jd.setVisible(true);
            }

        });
        watch_History.add(interval1);
        watch_History.add(interval2);
        watch_History.add(interval3);
        

        f.setContentPane(tempPanel);
        f.setJMenuBar(menuBar);
        f.setSize(1000, 1000);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);     
    }

    // puts movie info into movie tiles which are placed inside panel... returns updated panel
    // ** no pass by reference in java :(
    public static JPanel AddMoviesToPanels(String[] movies, JPanel panel) {
        for (int i = 0; i < movies.length; i++) {
            String temp = movies[i];
            JButton movieTile = new JButton(temp);
            movieTile.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            movieTile.setFont(movieTile.getFont().deriveFont(20f));
            movieTile.setPreferredSize(new Dimension(200,100));
            movieTile.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    JDialog jd = new JDialog(new JFrame());
                    jd.setBounds(500, 300, 400, 300);
                    jd.add(new JTextField(temp));
                    jd.setVisible(true);
                }

            });
            panel.add(movieTile);
        }
        
        return panel;
    }

    // gets a list of movie names based on query
    public static String[] get_Movie(Connection conn, String time, String user_id) {
        String info[] = new String[10];
        int idx = 0;
        try{
            //create a statement object
            Statement stmt = conn.createStatement();
            //create an SQL statement
            String sqlStatement = "";
            if (time == "all") {
                // returns all movies user ever watched
                sqlStatement = String.format("SELECT title FROM titles WHERE title_ID IN (SELECT title_id FROM customer WHERE customer_id='%s' ORDER BY date DESC LIMIT 10);", user_id);
            } else if (time == "recent 1 year") {
                // returns all movies user ever watched in the past year
                sqlStatement = String.format("SELECT title FROM titles WHERE title_ID IN (SELECT title_id FROM customer WHERE customer_id='%s' AND date > '2004-12-31' ORDER BY date DESC LIMIT 10);", user_id);
            } else if (time == "recent 2 year") {
                // returns all movies user ever watched in past two years
                sqlStatement = String.format("SELECT title FROM titles WHERE title_ID IN (SELECT title_id FROM customer WHERE customer_id='%s' AND date < '2004-12-31' AND date > '2003-12-31' ORDER BY date DESC LIMIT 10);", user_id);
            } else if (time == "viewers_choice") {
                // returns movies that user hasn't seen within past year and sorts by the highest rated
                // so basically: re-recommend highly-rated movies user hasnt watched within a year
                sqlStatement = String.format("SELECT title FROM titles WHERE title_id IN (SELECT title_id FROM customer WHERE customer_id='%s' AND date < '2005-01-01' ORDER BY rating DESC LIMIT 10);", user_id);
            } else if (time == "trending") {
                sqlStatement = "SELECT title FROM titles WHERE title_ID IN (SELECT title_ID FROM customer GROUP BY title_ID ORDER BY COUNT(title_ID) DESC LIMIT 10);";
            } else if (time == "directors") {
                sqlStatement = String.format("SELECT title FROM titles WHERE title_ID IN (SELECT title_ID FROM customer WHERE customer_id='%s' AND date >'2004-12-31' GROUP BY title_ID ORDER BY COUNT(title_ID) DESC LIMIT 10);", user_id);
            } else if (time == "actors") {
                sqlStatement = String.format("SELECT title FROM titles WHERE title_ID IN (SELECT title_ID FROM customer WHERE customer_id='%s' AND date >'2005-06-30' GROUP BY title_ID ORDER BY COUNT(title_ID) DESC LIMIT 10);", user_id);
            } else if (time == "viewers_beware") {
                sqlStatement = String.format("SELECT title FROM titles WHERE title_id IN (SELECT title_id FROM customer WHERE title_id IN (SELECT title_id FROM customer WHERE customer_id IN (SELECT customer_id FROM customer WHERE customer_id <> '%s' AND (title_id,rating) IN (SELECT title_id, rating FROM customer WHERE customer_id='%s' AND rating < 3) GROUP BY customer_id ORDER BY count(title_id) DESC LIMIT 1) ORDER BY rating) AND title_id NOT IN (SELECT title_id FROM customer WHERE customer_id='%s') LIMIT 10);", user_id, user_id, user_id);
            } else {
                return null;
            }
            //send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);
            while (result.next() && idx < 10) {
                String movie = result.getString("title");
                info[idx] = movie; // new Movie_info(movie, titleID);
                idx++;
            }
        } catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Error accessing Database.");
        }
        return info;
    }
}
