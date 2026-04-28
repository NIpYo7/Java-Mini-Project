package ui;

import ui.theme.AppTheme;
import dao.DBConnection;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        try {
            com.formdev.flatlaf.FlatLightLaf.setup();

            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 10);
            UIManager.put("TextComponent.arc", 10);

            UIManager.put("defaultFont", new Font("Segoe UI", Font.PLAIN, 14));

        } catch (Exception e) {
            System.err.println("FlatLaf setup failed. Using system look and feel.");
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
        }

        System.setProperty("sun.java2d.opengl", "true");
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        AppTheme.apply();

        if (DBConnection.getConnection() == null) {
            JOptionPane.showMessageDialog(null,
                    "Database connection failed!\n\n" +
                            "Please check:\n" +
                            "1. MySQL is running\n" +
                            "2. Password in dao/DBConnection.java\n" +
                            "3. schema.sql and data.sql have been run\n" +
                            "4. mysql-connector-j.jar is in project libraries",
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SwingUtilities.invokeLater(() -> {
            try {
                MainFrame frame = new MainFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
