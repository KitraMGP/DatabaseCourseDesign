package kitra.employeetraining;

import kitra.employeetraining.common.ui.LoginFrame;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;

public class Main {

    private static int userId;

    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        initGlobalFonts(new Font("Microsoft YaHei", Font.PLAIN, 14));

        new LoginFrame();
    }

    private static void initGlobalFonts(Font font) {
        FontUIResource resource = new FontUIResource(font);

        for (Object key : UIManager.getDefaults().keySet()) {
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, resource);
            }
        }
    }

    public static void setUserId(int userId) {
        Main.userId = userId;
    }

    public static int getUserId() {
        return userId;
    }
}
