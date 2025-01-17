package kitra.employeetraining.manager.ui;

import kitra.employeetraining.common.datamodel.Course;
import kitra.employeetraining.common.datamodel.Department;
import kitra.employeetraining.common.datamodel.Person;
import kitra.employeetraining.common.datamodel.TrainingPlan;
import kitra.employeetraining.common.logging.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

public class ManagerMainWindow extends JFrame {

    private static final Logger logger = Logger.getInstance("ManagerMainWindow");
    private final int userId;
    // 页面标识符常量
    private static final String PAGE_COURSE_MANAGEMENT = "course_management";
    private static final String PAGE_COURSE_SELECT_MANAGEMENT = "course_select_management";
    private static final String PAGE_TRAINING_RESULT_MANAGEMENT = "training_result_management";
    private static final String PAGE_DEPARTMENT_MANAGEMENT = "department_management";
    private static final String PAGE_PERSON_MANEGEMEENT = "person_management";
    private static final String PAGE_STATISTICS_MANAGEMENT = "statistics_management";
    private static final String PAGE_SETTING_ABOUT = "setting_about";
    private final JPanel panelTopBar;
    private final JPanel panelBody;
    // 在标题中显示的状态
    private String currentPageName = "课程管理";

    public ManagerMainWindow(int userId) {
        this.userId = userId;
        // 顶栏面板
        panelTopBar = createTopBarPanel();
        // 主体容器
        panelBody = new JPanel(new CardLayout());
        // 设置容器边框
        panelBody.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // 主容器的五个页面
        panelBody.add(createCourseManagementPanel(), PAGE_COURSE_MANAGEMENT);
        panelBody.add(createCourseSelectManagementPanel(), PAGE_COURSE_SELECT_MANAGEMENT);
        panelBody.add(createTrainingResultManagementPanel(), PAGE_TRAINING_RESULT_MANAGEMENT);
        panelBody.add(createDepartmentManagementPanel(), PAGE_DEPARTMENT_MANAGEMENT);
        panelBody.add(createPersonsManagementPanel(), PAGE_PERSON_MANEGEMEENT);
        panelBody.add(createStatisticsManagementPanel(), PAGE_STATISTICS_MANAGEMENT);
        panelBody.add(createSettingAboutPanel(), PAGE_SETTING_ABOUT);

        this.setLayout(new BorderLayout());
        this.add(panelTopBar, BorderLayout.NORTH);
        this.add(panelBody, BorderLayout.CENTER);

        updateWindowTitle();

        this.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 400);
        this.setMinimumSize(new Dimension(800, 300));
        this.setLocationRelativeTo(null);   // 让窗口居中显示
        this.setVisible(true);
    }

    private void updateWindowTitle() {
        this.setTitle(String.format("员工培训管理系统(管理员) - 当前页面：%s", this.currentPageName));
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
            case 0 -> PAGE_COURSE_MANAGEMENT;
            case 1 -> PAGE_COURSE_SELECT_MANAGEMENT;
            case 2 -> PAGE_TRAINING_RESULT_MANAGEMENT;
            case 3 -> PAGE_DEPARTMENT_MANAGEMENT;
            case 4 -> PAGE_PERSON_MANEGEMEENT;
            case 5 -> PAGE_STATISTICS_MANAGEMENT;
            case 6 -> PAGE_SETTING_ABOUT;
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
        JButton buttonCourseManagement = new JButton("课程管理");
        JButton buttonCourseSelectManagement = new JButton("选课结果管理");
        JButton buttonTrainingResultManagement = new JButton("培训结果管理");
        JButton buttonDepartmentManagement = new JButton("部门管理");
        JButton buttonPersonManagement = new JButton("员工管理");
        JButton buttonStatisticsManagement = new JButton("统计报表");
        JButton buttonSettingAbout = new JButton("设置&关于");

        // 添加顶栏按钮
        panelTopBar.add(buttonCourseManagement);
        panelTopBar.add(buttonCourseSelectManagement);
        panelTopBar.add(buttonTrainingResultManagement);
        panelTopBar.add(buttonDepartmentManagement);
        panelTopBar.add(buttonPersonManagement);
        panelTopBar.add(buttonStatisticsManagement);
        panelTopBar.add(buttonSettingAbout);

        // 五个页面切换逻辑
        buttonCourseManagement.addActionListener(this::switchPage);
        buttonCourseSelectManagement.addActionListener(this::switchPage);
        buttonTrainingResultManagement.addActionListener(this::switchPage);
        buttonDepartmentManagement.addActionListener(this::switchPage);
        buttonPersonManagement.addActionListener(this::switchPage);
        buttonStatisticsManagement.addActionListener(this::switchPage);
        buttonSettingAbout.addActionListener((this::switchPage));

        return panelTopBar;
    }

    private JPanel createCourseManagementPanel() {
        return new BaseDataPanel(this, Course.class, "课程管理（可搜索课程名和教师）");
    }

    private JPanel createCourseSelectManagementPanel() {
        return new CourseSelectManagementPanel(this);
    }

    private JPanel createTrainingResultManagementPanel() {
        return new BaseDataPanel(this, TrainingPlan.class, "选课管理（可搜索员工名和课程名）");
    }

    private JPanel createDepartmentManagementPanel() {
        return new BaseDataPanel(this, Department.class, "部门管理（可搜索部门名）");
    }

    private JPanel createPersonsManagementPanel() {
        return new BaseDataPanel(this, Person.class, "员工管理（可搜索登录名、姓名和权限）");
    }

    private JPanel createStatisticsManagementPanel() {
        return new DataExportPanel(this);
    }

    private JPanel createSettingAboutPanel() {
        return new SettingAboutPanel(this, userId);
    }
}
