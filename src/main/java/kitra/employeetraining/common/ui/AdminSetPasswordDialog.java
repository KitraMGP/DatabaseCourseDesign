package kitra.employeetraining.common.ui;

import kitra.employeetraining.common.auth.PasswordVerify;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * 管理员使用的可以直接更改用户密码的对话框
 */
public class AdminSetPasswordDialog extends JDialog {
    private final JFrame parent;
    private final int id;
    private final JPasswordField passwordField;

    public AdminSetPasswordDialog(JFrame parent, int id) {
        super(parent, "请设置密码", true);
        this.parent = parent;
        this.id = id;
        this.setLayout(new BorderLayout());
        JPanel container = new JPanel(new FlowLayout(FlowLayout.CENTER));
        container.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel label = new JLabel("输入新密码：");
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
        if(passwordField.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, "密码不能为空", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        PasswordVerify.setPassword(id, new String(passwordField.getPassword()));
        JOptionPane.showMessageDialog(this, "密码更改成功", "提示", JOptionPane.INFORMATION_MESSAGE);
        this.dispose();
    }
}
