package kitra.employeetraining.common.ui;

import kitra.employeetraining.common.auth.PasswordVerify;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * 验证用户输入密码是否正确的对话框
 */
public class PasswordVerifyDialog extends JDialog {
    private final JFrame parent;
    private final int id;
    private final JPasswordField passwordField;

    public PasswordVerifyDialog(JFrame parent, int id) {
        super(parent, "请输入密码", true);
        this.parent = parent;
        this.id = id;
        this.setLayout(new BorderLayout());
        JPanel container = new JPanel(new FlowLayout(FlowLayout.CENTER));
        container.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel label = new JLabel("输入密码：");
        passwordField = new JPasswordField(15);

        container.add(label);
        container.add(passwordField);
        this.add(container, BorderLayout.CENTER);

        JButton button = new JButton("确认");
        button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "okAction");
        button.getActionMap().put("okAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonClicked();
            }
        });
        button.addActionListener(l -> buttonClicked());
        this.add(button, BorderLayout.SOUTH);

        this.setLocationRelativeTo(parent);
        this.pack();
        this.setVisible(true);
    }

    private void buttonClicked() {
        if(!PasswordVerify.hasPassword(id)) {
            JOptionPane.showMessageDialog(this, "您还没有设置密码，请先设置密码！", "提示", JOptionPane.WARNING_MESSAGE);
            new AdminSetPasswordDialog(parent, id);
            return;
        }
        boolean valid = PasswordVerify.verifyPassword(id, passwordField.getPassword());
        if(valid) {
            JOptionPane.showMessageDialog(this, "您输入的密码正确", "提示", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "您输入的密码错误，若您忘记密码，请联系管理员重置密码", "提示", JOptionPane.WARNING_MESSAGE);
        }
    }
}
