import java.sql.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

public class Welcome extends JFrame{
    public Welcome() {
        super();
        super.setLayout(null);
        super.setSize(640, 640);
        super.setBackground(Color.black);

        ImageIcon icon = new ImageIcon("resources/welcome_640.png");
        JLabel label = new JLabel(icon);
        label.setBounds(0, 0, 640, 654);

        super.add(label);
        super.setVisible(true);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Login();
        this.dispose();
    }
    public static void main (String[] args) {
        Welcome page = new Welcome();
    }
}
