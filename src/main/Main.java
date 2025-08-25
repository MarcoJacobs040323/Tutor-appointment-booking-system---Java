package main;

import javax.swing.UIManager;
import javax.swing.SwingUtilities;
import javax.swing.UnsupportedLookAndFeelException;
import view.Dashboard;
import util.DatabaseSeeder;


public class Main {
    
    private static final boolean ENABLE_DB_SEEDER = false;
    
    public static void main(String[] args) {
        if (ENABLE_DB_SEEDER) {
            DatabaseSeeder.seed();
        }
        
        try {
            // set style to match native OS
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException 
                | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }

        // Launch Dashboard on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new Dashboard().setVisible(true);
        });
    }
}
