import java.sql.*;
import java.util.Scanner;
import java.io.File;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame{
    int x_index = 125;
    int y_index = 200;
    String actionCommand = "";
    JFrame current_frame = this;

    public Login() {
        super();
        super.setLayout(new GridLayout());
        super.setSize(640, 640);

        JPanel panel = new JPanel();    
        // adding panel to frame
        super.add(panel);
        placeComponents(panel);

        super.setVisible(true);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // check if user id is valid (compare with list of csvs)
    private static Boolean idExists(String id) {
        // open file
        Scanner sc = null;
        try {
            sc = new Scanner(new File("user_ids.csv"));
        } catch(Exception e) { e.printStackTrace(); }

        if (sc == null)
            return false;

        // check for id in file
        String next = null;
        Boolean result = false;
        while(sc.hasNextLine()) {
            next = sc.next().replaceAll("\n", "");
            if (id.equals(next)) {
                result = true;
                break;
            }
        }

        // return result
        sc.close();
        return result;
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);
        
        // initialize ribbon
        ImageIcon icon = new ImageIcon("resources/top_ribbon_640.png");
        JLabel label = new JLabel(icon);
        label.setBounds(0, 0, 640, 66);
        panel.add(label);

        //Creating Mode dropdown ... declare so we can have login listener use it
        String s1[] = { "SELECT", "ANALYST", "USER" };
        JComboBox<String> mode_men = new JComboBox<String>(s1);
        mode_men.setBounds(x_index+165,y_index,200,50);
        panel.add(mode_men);
        
        // Creating Mode label
        JLabel modeLabel = new JLabel("Mode");
        modeLabel.setBounds(x_index+10,y_index,160,50);
        modeLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        panel.add(modeLabel);

        // Creating username label
        JLabel userLabel = new JLabel("Username");
        userLabel.setBounds(x_index+10,y_index+55,165,50);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        panel.add(userLabel);

        //Creating username text
        JTextField userText = new JTextField(20);
        userText.setBounds(x_index+165,y_index+55,200,50);
        panel.add(userText);

        // Creating password label
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(x_index+10,y_index+115,165,50);
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        panel.add(passwordLabel);

        //Creating password text
        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(x_index+165,y_index+115,200,50);
        panel.add(passwordText);

        // Creating login button
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(x_index+10, y_index+175, 355, 50);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mode = (String) mode_men.getSelectedItem();
                String userName = userText.getText();
                String password = new String(passwordText.getPassword());

                if (userName.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Username / Password fields empty.");
                    return;
                }

                if (mode == "USER") {
                    if (!idExists(userName))
                        JOptionPane.showMessageDialog(null, "Invalid Username");
                    else {
                        // create new user window and dispose current
                        String[] args = {userText.getText()};
                        Viewer.main(args);
                        current_frame.dispose();
                    }
                } else if (mode == "ANALYST") {
                    // create analyst window and dispose current
                    Analyst.main(null);
                    current_frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Select entry mode via dropdown.");
                }
            }
        });
        loginButton.setActionCommand(actionCommand);
        panel.add(loginButton);

        // Creating create button
        JButton createButton = new JButton("Create");
        createButton.setBounds(x_index+10, y_index+235, 355, 50);
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Account successfully registered.");
            }
        });
        panel.add(createButton);
    }

    public static void main(String[] args) {
        Login page = new Login();
    }
}