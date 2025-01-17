package kitra.employeetraining;

import kitra.employeetraining.common.logging.Logger;
import kitra.employeetraining.employee.ui.EmployeeMainWindow;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;

public class EmployeeTestMain {
    private static final Logger logger = Logger.getInstance("ManagerTestMain");

    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        initGlobalFonts(new Font("Microsoft YaHei", Font.PLAIN, 14));

        new EmployeeMainWindow(1);
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
}