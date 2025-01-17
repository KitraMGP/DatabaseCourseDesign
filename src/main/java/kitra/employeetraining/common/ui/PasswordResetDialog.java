package kitra.employeetraining.common.ui;

import kitra.employeetraining.common.auth.PasswordVerify;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * 普通用户使用的可以更改密码的对话框，需要输入原密码
 */
public class PasswordResetDialog extends JDialog {
    private final int id;
    private final JPasswordField oldPassword;
    private final JPasswordField newPassword;

    public PasswordResetDialog(JFrame parent, int id) {
        super(parent, "请输入密码", true);
        this.id = id;
        this.setLayout(new BorderLayout());
        Box container = Box.createVerticalBox();
        container.setBorder(new EmptyBorder(20, 20, 20, 20));

        Box oldLine = Box.createHorizontalBox();
        JLabel oldLabel = new JLabel("输入旧密码：");
        oldPassword = new JPasswordField(15);
        oldLine.add(oldLabel);
        oldLine.add(oldPassword);

        Box newLine = Box.createHorizontalBox();
        JLabel newLabel = new JLabel("输入新密码：");
        newPassword = new JPasswordField(15);
        newLine.add(newLabel);
        newLine.add(newPassword);

        container.add(oldLine);
        container.add(newLine);
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
        boolean valid = PasswordVerify.verifyPassword(id, oldPassword.getPassword());
        if(!valid) {
            JOptionPane.showMessageDialog(this, "您输入的密码错误，若您忘记密码，请联系管理员重置密码", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        } else if(newPassword.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, "不能设置空密码！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        PasswordVerify.setPassword(id, new String(newPassword.getPassword()));
        JOptionPane.showMessageDialog(this, "设置成功", "提示", JOptionPane.INFORMATION_MESSAGE);
        this.dispose();
    }
}
