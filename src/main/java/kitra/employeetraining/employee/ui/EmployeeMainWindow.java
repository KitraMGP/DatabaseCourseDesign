package kitra.employeetraining.employee.ui;

import kitra.employeetraining.common.logging.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

public class EmployeeMainWindow extends JFrame {
    private static final Logger logger = Logger.getInstance("EmployerMainWindow");
    // 页面标识符常量
    private static final String PAGE_BASIC_INFO = "basic_information";
    private static final String PAGE_COURSE_SELECT = "course_select";
    private static final String PAGE_TRAINING_RESULT = "training_result";

    private final JPanel panelTopBar;
    private final JPanel panelBody;
    // 在标题中显示的状态
    private String currentPageName = "课程管理";
    private int personId;

    public EmployeeMainWindow(int personId) {
        this.personId = personId;
        // 顶栏面板
        panelTopBar = createTopBarPanel();
        // 主体容器
        panelBody = new JPanel(new CardLayout());
        // 设置容器边框
        panelBody.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // 主容器的五个页面
        panelBody.add(createBasicInformationPanel(), PAGE_BASIC_INFO);
        panelBody.add(createCourseSelectPanel(), PAGE_COURSE_SELECT);
        panelBody.add(createTrainingResultPanel(), PAGE_TRAINING_RESULT);

        this.setLayout(new BorderLayout());
        this.add(panelTopBar, BorderLayout.NORTH);
        this.add(panelBody, BorderLayout.CENTER);

        updateWindowTitle();

        this.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 500);
        //this.pack();
        this.setMinimumSize(new Dimension(800, 500));
        this.setLocationRelativeTo(null);   // 让窗口居中显示
        this.setVisible(true);
    }

    private void updateWindowTitle() {
        this.setTitle(String.format("员工培训管理系统(普通员工) - 当前页面：%s", this.currentPageName));
    }

    /**
     * 根据JButton实例获取它是顶栏的第几个按钮
     *
     * @param button 按钮实例
     * @return 按钮次序，从0开始
     */
    private int getTopBarButtonIndex(JButton button) {
        return Arrays.asList(button.getParent().getComponents()).indexOf(button);
    }

    /**
     * 传入顶栏按钮的ActionEvent，切换主容器中显示的页面
     *
     * @param e 按钮ActionEvent实例
     */
    private void switchPage(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        int buttonIndex = getTopBarButtonIndex(button);
        String pageIdentifier = switch (buttonIndex) {
            case 0 -> PAGE_BASIC_INFO;
            case 1 -> PAGE_COURSE_SELECT;
            case 2 -> PAGE_TRAINING_RESULT;
            default ->
                    throw new RuntimeException("Illegal buttonIndex: " + buttonIndex + ", buttonActionCommand: " + e.getActionCommand());
        };
        CardLayout cardLayout = (CardLayout) panelBody.getLayout();
        cardLayout.show(panelBody, pageIdentifier);
        // 更新标题栏
        this.currentPageName = e.getActionCommand();
        updateWindowTitle();
    }

    private JPanel createTopBarPanel() {
        JPanel panelTopBar = new JPanel(new FlowLayout(FlowLayout.LEADING));
        // 顶栏内容
        JButton buttonBasicInfo = new JButton("个人信息修改");
        JButton buttonCourseSelect = new JButton("选课");
        JButton buttonTrainingResult = new JButton("成绩查询");

        // 添加顶栏按钮
        panelTopBar.add(buttonBasicInfo);
        panelTopBar.add(buttonCourseSelect);
        panelTopBar.add(buttonTrainingResult);

        // 五个页面切换逻辑
        buttonBasicInfo.addActionListener(this::switchPage);
        buttonCourseSelect.addActionListener(this::switchPage);
        buttonTrainingResult.addActionListener(this::switchPage);

        return panelTopBar;
    }

    private JPanel createBasicInformationPanel() {
        return new BasicInformationPanel(this, personId);
    }

    private JPanel createCourseSelectPanel() {
        return new CourseSelectPanel(this, personId);
    }

    private JPanel createTrainingResultPanel() {
        return new GradePanel(this, personId);
    }

}
