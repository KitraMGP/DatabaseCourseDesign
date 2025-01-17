package kitra.employeetraining.common.ui;

import kitra.employeetraining.Main;
import kitra.employeetraining.common.auth.PasswordVerify;
import kitra.employeetraining.common.dao.PersonDao;
import kitra.employeetraining.common.datamodel.Authority;
import kitra.employeetraining.common.util.MyBatisUtils;
import kitra.employeetraining.employee.ui.EmployeeMainWindow;
import kitra.employeetraining.manager.ui.ManagerMainWindow;
import org.apache.ibatis.session.SqlSession;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {
    private final JTextField accountField;
    private final JPasswordField passwordField;

    public LoginFrame() {

        this.setLayout(new BorderLayout());
        Box container = Box.createVerticalBox();
        container.setBorder(new EmptyBorder(20, 20, 20, 20));

        Box accountLine = Box.createHorizontalBox();
        JLabel userLabel = new JLabel("请输入账号：");
        accountField = new JTextField(15);
        accountLine.add(userLabel);
        accountLine.add(accountField);

        Box passwordLine = Box.createHorizontalBox();
        JLabel passwordLabel = new JLabel("请输入密码：");
        passwordField = new JPasswordField(15);
        passwordLine.add(passwordLabel);
        passwordLine.add(passwordField);

        container.add(accountLine);
        container.add(passwordLine);
        this.add(container, BorderLayout.CENTER);

        JButton button = new JButton("登录");
        button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "okAction");
        button.getActionMap().put("okAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonClicked();
            }
        });
        button.addActionListener(l -> buttonClicked());
        this.add(button, BorderLayout.SOUTH);

        this.setTitle("登录");
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    private void buttonClicked() {

        int userId;

        boolean valid = PasswordVerify.verifyPassword(accountField.getText(), passwordField.getPassword());
        if(!valid) {
            JOptionPane.showMessageDialog(this, "账号或密码错误", "登录失败", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try(SqlSession session = MyBatisUtils.getSqlSession()) {
            userId = session.getMapper(PersonDao.class).getByUserName(accountField.getText()).getId();
        }

        Authority authority = PasswordVerify.getAuthority(userId);

        if(authority == null) {
            JOptionPane.showMessageDialog(this, "账户未设置用户权限，无法登录", "登录失败", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Main.setUserId(userId);

        if(!PasswordVerify.hasPassword(userId)) {
            JOptionPane.showMessageDialog(this, "您还没有设置密码，请先设置密码！", "提示", JOptionPane.WARNING_MESSAGE);
            new AdminSetPasswordDialog(this, userId);
        }

        if(authority.getCode() == 0) {
            // 管理员
            new ManagerMainWindow(userId);
            this.dispose();
        } else {
            new EmployeeMainWindow(userId);
            this.dispose();
        }
    }
}
