import java.sql.*;
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NavigateButtonListener implements ActionListener {

    JFrame caller = null;

    public NavigateButtonListener(JFrame caller) {
        this.caller = caller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String query = e.getActionCommand();
        if (query == "analyst")
            Analyst.main(null);
        else if (query == "user")
            Viewer.main(null);
        else if (query == "invalid")
            return;
        caller.dispose();
    }
    
}
