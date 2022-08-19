// Commands to run:
// Compile: javac -cp ".:xchart-3.8.1.jar:postgresql-42.2.8.jar" Analyst.java
// Run: java -cp ".:xchart-3.8.1.jar:postgresql-42.2.8.jar" Analyst

import java.sql.*;
import java.awt.event.*;
import javax.swing.*;
import javax.xml.transform.Templates;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import org.knowm.xchart.*;

import java.util.*;

public class Analyst extends JFrame implements ActionListener {
    static JFrame f;
    JTextField movies_Display[] = new JTextField[10];
    JTextField directors_Display[] = new JTextField[5];
    CategoryChart barChart;
    XYChart lineChart;
    PieChart pieChart;
    Connection conn = null;
    // private final JMenuItem menuA, menuB; // manu selection (used for time windows)\
    public static void main(String args[]) {
        Analyst a = new Analyst();
        a.window();
    }
    String Time = "all";
    public void window() { //String args[]
        //Building the connection
        //TODO STEP 1
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
        JOptionPane.showMessageDialog(null,"Opened database successfully");


        // Getting the genres
        Genre_info top_Ten_Genres[] = get_Genres(conn, Time);
        Person people[] = get_People(conn, Time);

        // Constructing pie chart
        String names[] = new String[10];
        int amt[] = new int[10];
        for (int i = 0; i < 10; i++) {
            names[i] = top_Ten_Genres[i].get_Genre();
            amt[i] = top_Ten_Genres[i].get_Occurences();
        }
        pieChart = buildPieChart( names, amt );

        // Constructing people bar chart
        List<String> people_Names = new ArrayList<String>();
        List<Double> people_Popularity = new ArrayList<Double>();
        for (int i = 0; i < people.length; i++) {
            people_Names.add(people[i].get_Name());
            people_Popularity.add(people[i].get_Popularity());
        }
        barChart = buildBarChart(people_Names, people_Popularity);

        // Constructing the views line chart
        int views[] = get_Views(conn, Time);
        lineChart = buildLineChart(views);

        // Building the frame
        
        f = new JFrame("Analyst GUI");
        f.setLayout(new GridLayout(1,3));
        f.setDefaultCloseOperation(EXIT_ON_CLOSE);

        Analyst s = new Analyst();

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();

        JMenu user_ID = new JMenu("User ID");
        user_ID.add(new JMenuItem("All"));

        JMenu watch_History = new JMenu("Indirect Director");
        JTextField entry = new JTextField("Add Actor");
        watch_History.add(entry);
        JButton indirect_director_button = new JButton("Submit");
        watch_History.add(indirect_director_button);
        indirect_director_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] movies = get_Movie(conn, "indirect_director", entry.getText());
                JDialog jd = new JDialog(new JFrame());
                jd.setBounds(500, 300, 400, 150);
                String answer = " ";
                for (int i = 0; i < 3; i++) {
                    answer += movies[i];
                    answer += " ";
                }
                jd.add(new JTextField(answer));
                jd.setVisible(true);
            }
            
        });

        JMenu cult_classics = new JMenu("Cult Classics");
        JButton cult_submit = new JButton("View List");
        cult_classics.add(cult_submit);
        cult_submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] movies = get_Movie(conn, "cult_classics", entry.getText());
                JDialog jd = new JDialog(new JFrame());
                jd.setBounds(500, 300, 600, 150);
                String answer = "";
                for (int i = 0; i < movies.length; i++) {
                    answer += movies[i];
                    if (i != movies.length - 1)
                        answer += "\n";
                }
                jd.add(new JTextArea(answer));
                jd.setVisible(true);
            }
        });

        // JMenu stats = new JMenu("Stats");
        // stats.add(new JMenuItem("1 out of 100"));
        // stats.add(new JMenuItem("15%"));

        ActionListener all_listener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                set_Time("all");
                update_window();
            }
        };

        ActionListener one_year_listener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                set_Time("recent 1 year");
                update_window();
            }
        };

        JMenu time = new JMenu("Time");
        JMenuItem menuA = new JMenuItem("All");
        JMenuItem menuB = new JMenuItem("Recent 1 year");
        time.add(menuA);
        time.add(menuB);
        menuA.addActionListener(all_listener);
        menuB.addActionListener(one_year_listener);
        // String Time;
        // void set_Time(String v) {
        //     Time = v;
        // }
        

        menuBar.add(user_ID);
        menuBar.add(watch_History);
        menuBar.add(cult_classics);
        // menuBar.add(stats);
        menuBar.add(time); 



        // Body
        // Split into four sections
        // Genre
        JPanel genres = new JPanel();
        genres.setLayout(new BoxLayout(genres,BoxLayout.PAGE_AXIS));
        JTextField genre_title = new JTextField("Genre");
        genre_title.setEditable(false);
        genres.add(genre_title, BorderLayout.CENTER);
        JPanel chartPanel = new XChartPanel<PieChart>(pieChart);
        genres.add(chartPanel);


        // Trending actors
        JPanel actors = new JPanel();
        actors.setLayout(new BoxLayout(actors,BoxLayout.PAGE_AXIS));
        JTextField actors_title = new JTextField("Trending Actors");
        actors_title.setEditable(false);
        actors.add(actors_title, BorderLayout.CENTER);
        JPanel actorsChart = new XChartPanel<CategoryChart>(barChart);
        actors.add(actorsChart);
        
        // // Trending movies
        // JPanel movies = new JPanel();
        // movies.setLayout(new GridLayout(2,1));
        // JPanel movie_area = new JPanel();
        // JTextField movies_title = new JTextField("Trending Movies");
        // movies_title.setEditable(false);
        // movies.add(movies_title);
        // JTextField movies_Display[] = new JTextField[3];
        // for (int i = 0; i < movies_Display.length; i++) {
        //     movies_Display[i] = new JTextField("Movie " + (i+1));
        //     movies_Display[i].setEditable(false);
        //     movie_area.add(movies_Display[i]);
        // }
        // movies.add(movie_area);

        // Trending movies
        String top_Ten_Movies[] = get_Movie(conn, Time, ""); // Getting the top 10 movies
        JPanel movies = new JPanel();
        movies.setLayout(new GridLayout(2,1));
        JPanel movie_area = new JPanel();
        JTextField movies_title = new JTextField("Trending Movies");
        movies_title.setEditable(false);
        movies.add(movies_title);
        for (int i = 0; i < movies_Display.length; i++) {
            movies_Display[i] = new JTextField(top_Ten_Movies[i]);
            movies_Display[i].setEditable(false);
            movie_area.add(movies_Display[i]);
        }
        movies.add(movie_area);


        // // Trending directors
        // JPanel directors = new JPanel();
        // directors.setLayout(new GridLayout(2,1));
        // JPanel director_area = new JPanel();
        // JTextField directors_title = new JTextField("Trending Directors");
        // directors_title.setEditable(false);
        // directors.add(directors_title);
        // JTextField directors_Display[] = new JTextField[3];
        // for (int i = 0; i < movies_Display.length; i++) {
        //     directors_Display[i] = new JTextField("Director " + (i+1));
        //     directors_Display[i].setEditable(false);
        //     director_area.add(directors_Display[i]);
        // }
        // directors.add(director_area);

        // Trending directors
        Person top_Five_Directors[] = get_Director(conn, Time); // Getting the top 5 movies
        JPanel directors = new JPanel();
        directors.setLayout(new GridLayout(2,1));
        JPanel director_area = new JPanel();
        JTextField directors_title = new JTextField("Trending Directors");
        directors_title.setEditable(false);
        directors.add(directors_title);
        for (int i = 0; i < directors_Display.length; i++) {
            directors_Display[i] = new JTextField(top_Five_Directors[i].get_Name());
            directors_Display[i].setEditable(false);
            director_area.add(directors_Display[i]);
        }
        directors.add(director_area);

        // Making right most JPanel

//        link freshTomatoes = getFreshTomatoes("tt1074993", "tt0419299");
        JTextArea display_connection = new JTextArea();
        JPanel movie_connections = new JPanel();
        movie_connections.setLayout(new GridLayout(3,1));
        JPanel movie_boxes = new JPanel();
        movie_boxes.setLayout(new GridLayout(1,2));
        JTextField start_movie = new JTextField("Enter Start Movie");
        JTextField end_movie = new JTextField("Enter End Movie");
        movie_boxes.add(start_movie);
        movie_boxes.add(end_movie);
        movie_connections.add(movie_boxes);

        // Making the button
        ActionListener search_for_connection = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String fresh_tomatoes = getFreshTomatoes(start_movie.getText(), end_movie.getText());
                display_connection.setText(fresh_tomatoes);
            }
        };
        JButton find_connections = new JButton("Find connection");
        find_connections.addActionListener(search_for_connection);
        movie_connections.add(find_connections);
        movie_connections.add(display_connection);

        // Hollywood pairs
        Pair top_Ten_Pairs[] = get_Pairs(conn, Time);
        JPanel pairs = new JPanel();
        String[][] out = {{},{},{},{},{},{},{},{},{},{}}; // 
        
        pairs.setLayout(new GridLayout(2,1));
        JPanel pair_area = new JPanel();
        JTextField pair_title = new JTextField("Hollywood Pairs");
        pair_title.setEditable(false);
        pairs.add(pair_title);
        for (int i = 0; i < 10; i++) {
            String [] temp = {top_Ten_Pairs[i].get_actor_a(), top_Ten_Pairs[i].get_actor_b(), String.valueOf(top_Ten_Pairs[i].get_Chem())};
            out[i]=temp;
        }
        String[] header = {"Actor_a","Actor_b","Chem"};
        JTable table = new JTable(out, header);
        pair_area.add(new JScrollPane(table));
        pairs.add(pair_area);

        // App JPanels to JFrame
        JPanel left = new JPanel();
        left.setLayout(new GridLayout(2,1));
        JPanel mid = new JPanel();
        mid.setLayout(new GridLayout(3,1));
        JPanel right = new JPanel();
        right.setLayout(new GridLayout(3,1));
        left.add(genres);
        left.add(actors);
        mid.add(movies);
        mid.add(new XChartPanel(lineChart));
        mid.add(directors);
        right.add(movie_connections);
        right.add(pairs);
        f.add(left);
        f.add(mid);
        f.add(right);

        f.setSize(1000, 1000);
        f.setJMenuBar(menuBar);

        f.setVisible(true);
    }

    public void update_window() {
        System.out.println("Updating window");
        String top_Ten_Movies[] = get_Movie(conn, Time, "");
        Person top_Five_Directors[] = get_Director(conn, Time);

        for (int i = 0; i < top_Ten_Movies.length; i++) {
            movies_Display[i].setText(top_Ten_Movies[i]);
        }
        for (int i = 0; i < top_Five_Directors.length; i++) {
            directors_Display[i].setText(top_Five_Directors[i].get_Name());
        }

        // Constructing pie chart
        Genre_info top_Ten_Genres[] = get_Genres(conn, Time);
        String names[] = new String[10];
        int amt[] = new int[10];
        for (int i = 0; i < 10; i++) {
            names[i] = top_Ten_Genres[i].get_Genre();
            amt[i] = top_Ten_Genres[i].get_Occurences();
            pieChart.updatePieSeries(names[i], amt[i]);
        }
        // pieChart.updatePieChart() = buildPieChart( names, amt );

        // Constructing people bar chart
        Person people[] = get_People(conn, Time);
        List<String> people_Names = new ArrayList<String>();
        List<Double> people_Popularity = new ArrayList<Double>();
        for (int i = 0; i < people.length; i++) {
            people_Names.add(people[i].get_Name());
            people_Popularity.add(people[i].get_Popularity());
        }
        barChart.updateCategorySeries("Actor", people_Names, people_Popularity, null);

        // Constructing the views line chart
        // int views_int[] = get_Views(conn, Time);
        // double views[] = new double[views_int.length];
        // for (int i = 0; i < views_int.length; i++) {
        //     views[i] = views_int[i];
        // }
        // lineChart.updateXYSeries​("First", new double[] {7,6,5,4,3,2,1}, views, null);
    }


    void set_Time(String v) {
            Time = v;
        }

    public static Genre_info[] get_Genres (Connection conn, String time) {
        Genre_info info[] = new Genre_info[10];
        int idx = 0;
        try{
            //create a statement object
            Statement stmt = conn.createStatement();
            //create an SQL statement
            //TODO Step 2
            String sqlStatement = "";
            if (time == "all") {
                sqlStatement = "SELECT genre,COUNT(genre) FROM titles GROUP BY genre ORDER BY COUNT(genre) DESC";
            } else if (time == "recent 1 year") {
                sqlStatement = "SELECT genre,COUNT(genre) FROM " + 
                                    "((SELECT title_id,genre FROM titles) as movies " + 
                                    "NATURAL JOIN " +
                                    "(SELECT title_id FROM customer WHERE date>'2004-12-31') as timing) " +
                                "GROUP BY genre ORDER BY COUNT(genre) DESC";
            }
            //send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);
            while (result.next() && idx < 10) {
                String genre = result.getString("genre");
                int count = Integer.parseInt(result.getString("count"));
                info[idx] = new Genre_info(genre, count);
                idx++;
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(null,"Error accessing Database.");
        }
        return info;
    }
    
    public static Person[] get_People (Connection conn, String time) {
        Person people[] = new Person[5];
        int idx = 0;
        try{
            //create a statement object
            Statement stmt = conn.createStatement();
            //create an SQL statement
            String sqlStatement = "";
            if (time == "all") {
                sqlStatement = "SELECT primary_name AS name, total_popularity AS popularity FROM " +
                                        "(SELECT name_id, SUM(popularity) as total_popularity FROM " + 
                                            "(SELECT name_id, movie_id FROM principals WHERE category='actor' OR category='actress') " + 
                                            "AS ids NATURAL JOIN " + 
                                            "(SELECT * FROM movie_popularity) as interest GROUP BY name_id) AS name_popularity " + 
                                        "NATURAL JOIN (SELECT name_id, primary_name FROM names) AS name_pool " + 
                                    "ORDER BY popularity DESC";
            } else if (time == "recent 1 year") {
                sqlStatement = "SELECT primary_name AS name, total_popularity AS popularity FROM " +
                                    "(SELECT name_id, SUM(popularity) as total_popularity FROM " + 
                                        "(SELECT name_id, movie_id FROM principals WHERE category='actor' OR category='actress') " + 
                                        "AS ids NATURAL JOIN " + 
                                            "(SELECT title_id AS movie_id, view_count*avg_rating AS popularity FROM " +
                                                "( SELECT title_id,COUNT(title_id) AS view_count FROM customer WHERE date>'2004-12-31' GROUP BY title_id ) as title_count " + 
                                                "NATURAL JOIN " + 
                                                "( SELECT title_id, avg_rating FROM titles ) as rating) as foo GROUP BY name_id) " +
                                        "AS name_popularity " + 
                                    "NATURAL JOIN (SELECT name_id, primary_name FROM names) AS name_pool " + 
                                "ORDER BY popularity DESC";
            }
            
            //send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);
            while (result.next() && idx < 5) {
                String name = result.getString("name");
                double popularity = Double.parseDouble(result.getString("popularity"));
                people[idx] = new Person(name, popularity);
                idx++;
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(null,"Error accessing Database.");
        }
        return people;
    }

    public static int[] get_Views(Connection conn, String time) {
        int views[] = new int[7];
        int idx = 0;
        try{
            //create a statement object
            Statement stmt = conn.createStatement();
            //create an SQL statement
            String sqlStatement = "";
            if (time == "all") {
                sqlStatement = "SELECT date, COUNT(date) FROM CUSTOMER GROUP BY date ORDER BY date DESC LIMIT 7";
            } else if (time == "recent 1 year") {
                sqlStatement = "SELECT date, COUNT(date) FROM CUSTOMER WHERE date>'2004-12-31' GROUP BY date ORDER BY date DESC LIMIT 7";
            }
            
            //send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);
            while (result.next()) {
                views[idx] = Integer.parseInt(result.getString("count"));
                idx++;
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(null,"Error accessing Database.");
        }
        return views;
    }

    public static String[] get_Movie (Connection conn, String time, String actor) {
        String info[] = new String[10];
        int idx = 0;
        try{
            //create a statement object
            Statement stmt = conn.createStatement();
            //create an SQL statement
            String sqlStatement = "";
            if (time == "all") {
                sqlStatement = "SELECT title FROM titles WHERE title_ID IN (SELECT title_ID FROM customer GROUP BY title_ID ORDER BY COUNT(title_ID) DESC LIMIT 10);";
            } else if (time == "recent 1 year") {
                sqlStatement = "SELECT title FROM titles WHERE title_ID IN (SELECT title_ID FROM customer WHERE date>'2004-12-31' GROUP BY title_ID ORDER BY COUNT(title_ID) DESC LIMIT 10);";
            } else if (time == "indirect_director") {
                sqlStatement = String.format("SELECT primary_name FROM names where name_id IN (SELECT name_id FROM principals WHERE category='director' AND movie_id IN (SELECT movie_id FROM principals WHERE name_id IN (SELECT name_id FROM principals WHERE name_id NOT IN (SELECT name_id FROM names WHERE primary_name='%s') AND movie_id IN (SELECT movie_id FROM principals WHERE name_id LIKE (SELECT name_id FROM names WHERE primary_name='%s')) AND (category='actor' OR category ='actress') GROUP BY name_id ORDER BY count(name_id) DESC LIMIT 3)) GROUP BY name_id ORDER BY count(name_id) DESC) LIMIT 3;", actor, actor);
            } else if (time == "cult_classics") {
                sqlStatement = "SELECT title FROM titles WHERE title_id IN (SELECT title_id FROM customer WHERE rating > 3 GROUP BY title_id ORDER BY COUNT(rating) DESC LIMIT 20);";
            }
            //send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);
            while (result.next() && idx < 10) {
                String movie = null;
                if (time == "indirect_director") {
                    movie = result.getString("primary_name");
                } else {
                    movie = result.getString("title");
                }
                info[idx] = movie; // new Movie_info(movie, titleID);
                idx++;
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(null,"Error accessing Database.");
        }
        return info;
    }

    public static Person[] get_Director (Connection conn, String time) {
        Person info[] = new Person[5];
        int idx = 0;
        try{
            //create a statement object
            Statement stmt = conn.createStatement();
            String sqlStatement = "";
            //create an SQL statement
            if (time == "all") {
                sqlStatement = "SELECT primary_name AS name, total_popularity AS popularity FROM (SELECT name_id, SUM(popularity)" 
                    + " as total_popularity FROM (SELECT name_id, movie_id FROM principals WHERE category='director') " 
                    + "AS ids NATURAL JOIN (SELECT * FROM movie_popularity) as interest GROUP BY name_id) " 
                    + "AS name_popularity NATURAL JOIN (SELECT name_id, primary_name FROM names) AS name_pool ORDER BY popularity DESC LIMIT 5;";
            } else if (time == "recent 1 year") {
                sqlStatement = "SELECT primary_name AS name, total_popularity AS popularity FROM " +
                                    "(SELECT name_id, SUM(popularity) as total_popularity FROM " + 
                                        "(SELECT name_id, movie_id FROM principals WHERE category='director') " + 
                                        "AS ids NATURAL JOIN " + 
                                            "(SELECT title_id AS movie_id, view_count*avg_rating AS popularity FROM " +
                                                "( SELECT title_id,COUNT(title_id) AS view_count FROM customer WHERE date>'2004-12-31' GROUP BY title_id ) as title_count " + 
                                                "NATURAL JOIN " + 
                                                "( SELECT title_id, avg_rating FROM titles ) as rating) as foo GROUP BY name_id) " +
                                        "AS name_popularity " + 
                                    "NATURAL JOIN (SELECT name_id, primary_name FROM names) AS name_pool " + 
                                "ORDER BY popularity DESC";
            }
            
            //send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);
            // while (result.next() && idx < 5) {
            //     String movie = result.getString("director_id");
            //     info[idx] = movie; // new Movie_info(movie, titleID);
            //     idx++;
            // }
            while (result.next() && idx < 5) {
                String director = result.getString("name");
                float pop = Float.parseFloat(result.getString("popularity"));
                info[idx] = new Person(director, pop);
                idx++;
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(null,"Error accessing Database.");
        }
        return info;
    }

    public static Pair[] get_Pairs(Connection conn, String time) {
        Pair pairs[] = new Pair[10];
        int idx = 0;
        try{
            //create a statement object
            Statement stmt = conn.createStatement();
            //create an SQL statement
            String sqlStatement = "SELECT actor_a, actor_b, AVG(avg_rating) as chem FROM movie_pair GROUP BY actor_a, actor_b ORDER BY chem DESC LIMIT 10;";
            //send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);
            while (result.next() && idx < 10) {
                String actor_a = result.getString("actor_a");
                String actor_b = result.getString("actor_b");
                float chem = Float.parseFloat(result.getString("chem"));
                pairs[idx] = new Pair(actor_a, actor_b, chem);
                idx++;
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(null,"Error accessing Database.");
        }
        return pairs;
    }


    public PieChart buildPieChart(String names[], int occurences[]) { // static
        // Building an example chart
        PieChart chart = new PieChartBuilder().width(500).height(400).title("Popular genres").build();
        // Customize Chart
        Color[] sliceColors = new Color[] { new Color (214, 32, 7), new Color(224, 68, 14), new Color(230, 105, 62), new Color(236, 143, 110), 
                                            new Color(243, 180, 159), new Color(246, 199, 182), new Color(250, 216, 200) };
        chart.getStyler().setSeriesColors(sliceColors);

        if (occurences.length <= 7) {
            for (int i = 0; i < occurences.length; i++) {
                chart.addSeries(names[i], occurences[i]);
            }
        } else {
            for (int i = 0; i < 6; i++) {
                chart.addSeries(names[i], occurences[i]);
            }
            int other_Total = 0;
            for (int i = 6; i < occurences.length; i++) {
                other_Total += occurences[i];
            }
            chart.addSeries("other", other_Total);
        }
        return chart;
    }

    public CategoryChart buildBarChart(List<String> names, List<Double> popularity) { // static
        CategoryChart chart = new CategoryChartBuilder().width(500).height(400).title("Actor Popularity").xAxisTitle("People").build();
        // Customize Chart
        // chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
        // chart.getStyler().setHasAnnotations(false);
        chart.getStyler().setLegendVisible(false);
    
        // Series
        chart.addSeries("Actor", names, popularity);
        chart.getStyler().setBaseFont(new Font("SansSerif", java.awt.Font.BOLD, 5));
    
        return chart;
    }

    public XYChart buildLineChart(int vals[]) { // static
        XYChart chart = new XYChartBuilder().width(500).height(200).title("Views over the last week").xAxisTitle("Days ago").build();
        // return QuickChart.getChart("", "days ago", "views", "views", new int[] {7,6,5,4,3,2,1}, vals);
        chart.getStyler().setLegendVisible(false);
        // chart.getAxesStyler().setYAxisMin(0);

        chart.addSeries("First", new int[] {7,6,5,4,3,2,1}, vals);

        return chart;
    }

    public String getFreshTomatoes (String start_movie, String end_movie) {
        if (!check_valid_name(start_movie) || !check_valid_name(end_movie)) {
            return "Invalid movies";
        }
        HashMap<String, Boolean> visited_nodes = new HashMap<String, Boolean>();
        LinkedList<link> links = new LinkedList();
        link l = new link(start_movie);
        links.add(l);

        while ( links.size() != 0 ) {
            if ( links.peek().people.size() != links.peek().movies.size() ) {
                String movie = links.peek().get_latest_movie();
                ArrayList<String> possible_customers = makeSQLQuery("SELECT * FROM customer WHERE title_id='" + movie + "' AND rating>3");
                for (String customer : possible_customers) {
                    if (!links.peek().has_person(customer)) {
                        if (visited_nodes.get(customer) == null) {
                            link temp_link = new link(links.peek().movies, links.peek().people);
                            temp_link.people.add(customer);
                            links.add(temp_link);
                            visited_nodes.put(customer, true);
                        }
                    }
                }
            } else {
                String customer = links.peek().get_latest_person();
                ArrayList<String> possible_movies = makeSQLQuery("SELECT title_id FROM customer WHERE customer_id='" + customer + "' AND rating>3");
                for (String movie : possible_movies) {
                    if (movie.equals(end_movie)) {
                        link temp_link = new link (links.peek().movies, links.peek().people);
                        temp_link.movies.add(movie);
                        return temp_link.printLink();
                    }
                    if (!links.peek().has_movie(movie)) {
                        if (visited_nodes.get(movie) == null) {
                            link temp_link = new link(links.peek().movies, links.peek().people);
                            temp_link.movies.add(movie);
                            links.add(temp_link);
                            visited_nodes.put(movie, true);
                        }
                    }
                }
            }
            links.poll();
            // System.out.println(links.poll());
        }

        return l.printLink();
    }

    public boolean check_valid_name(String name) {
        try {
            Statement stmt = conn.createStatement();

            //send statement to DBMS
            ResultSet result = stmt.executeQuery("SELECT * FROM titles WHERE title_id='" + name + "'");
            result.next();
            result.getString(1);
        } catch (Exception e){
            return false;
//            JOptionPane.showMessageDialog(null,"Error accessing Database: " + e.getMessage());
        }
        return true;
    }

    public ArrayList<String> makeSQLQuery(String query) {
        ArrayList<String> results = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            
            //send statement to DBMS
            ResultSet result = stmt.executeQuery(query);
            while (result.next()) {
                // System.out.println("adding movie " + result.getString(1));
                results.add(result.getString(1));
            }
        } catch (Exception e){ 
            JOptionPane.showMessageDialog(null,"Error accessing Database: " + e.getMessage());
        }
        return results;
    }

    // if button is pressed
    public void actionPerformed(ActionEvent e)
    {
        String s = e.getActionCommand();
        if (s.equals("Close")) {
            f.dispose();
        }
    }
}

class Genre_info {
    String genre = "";
    int numOccurences = 0;

    Genre_info(String g, int o) {
        genre = g;
        numOccurences = o;
    }

    public void set_Genre(String new_Genre) {
        genre = new_Genre;
    }
    public String get_Genre() {
        return genre;
    }

    public void set_Occurences(int num) {
        numOccurences = num;
    }
    public int get_Occurences() {
        return numOccurences;
    }
}

class Person {
    String name = "";
    double popularity = 0.0;

    Person(String n, double p) {
        name = n;
        popularity = p;
    }

    public void set_Name(String n) {
        name = n;
    }
    public String get_Name() {
        return name;
    }

    public void set_Popularity(double p) {
        popularity = p;
    }
    public double get_Popularity() {
        return popularity;
    }
}

class Pair {
    String actor_a = "";
    String actor_b = "";
    float chem;

    Pair(String a, String b, float c) {
        actor_a = a;
        actor_b = b;
        chem = c;
    }

    public void set_Pair(String a, String b) {
        actor_a = a;
        actor_b = b;
    }
    public String get_actor_a() {
        return actor_a;
    }
    public String get_actor_b() {
        return actor_b;
    }

    public void set_Chem(float c) {
        chem = c;
    }
    public float get_Chem() {
        return chem;
    }
}

class link {
    ArrayList<String> movies = new ArrayList<>();
    ArrayList<String> people = new ArrayList<>();

    link (String title) {
        movies.add(title);
    }
    link (ArrayList<String> m, ArrayList<String> p) {
        for (String movie : m) {
            movies.add(movie);
        }
        for (String person : p) {
            people.add(person);
        }
    }

    public String get_latest_movie() {
        return movies.get(movies.size() - 1);
    }
    public String get_latest_person() {
        return people.get(people.size() - 1);
    }

    public boolean has_person(String person) {
        for (String p : people) {
            if (p.equals(person)) {
                return true;
            }
        }
        return false;
    }
    public boolean has_movie(String movie) {
        for (String m : movies) {
            if (m.equals(movie)) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        String result = "movies: {";
        for (String movie : movies) {
            result += movie + ", ";
        }
        result += "} people: {";
        for (String person : people) {
            result += person + ", ";
        }
        result += "}";
        return result;
    }

    public String printLink() {
        if (people.size() == 0) {
            return "No link found";
        }
        String response = "";
        for (int i = 0; i < people.size(); i++) {
            response += movies.get(i);
            response += " -> ";
            response += people.get(i);
            response += " -> ";
        }
        response += movies.get(movies.size() - 1);
        return response;
    }
}
