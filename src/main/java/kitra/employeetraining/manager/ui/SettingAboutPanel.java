package kitra.employeetraining.manager.ui;

import kitra.employeetraining.common.ui.PasswordResetDialog;
import kitra.employeetraining.common.ui.PasswordVerifyDialog;

import javax.swing.*;
import java.awt.*;

public class SettingAboutPanel extends JPanel {

    private final JFrame parent;
    private final int userId;

    public SettingAboutPanel(JFrame parent, int userId) {
        this.parent = parent;
        this.userId = userId;
        this.setLayout(new BorderLayout());
        this.add(createFunctionPanel(), BorderLayout.CENTER);
    }

    private JPanel createFunctionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JButton passwordVerifyButton = new JButton("验证密码");
        passwordVerifyButton.addActionListener(e -> new PasswordVerifyDialog(parent, userId));
        JButton passwordResetButton = new JButton("重置密码");
        passwordResetButton.addActionListener(e -> new PasswordResetDialog(parent, userId));
        panel.add(passwordVerifyButton);
        panel.add(passwordResetButton);
        return panel;
    }
}
