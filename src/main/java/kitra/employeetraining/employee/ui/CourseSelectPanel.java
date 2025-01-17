package kitra.employeetraining.employee.ui;

import kitra.employeetraining.common.dao.CourseDao;
import kitra.employeetraining.common.dao.CourseSelectDao;
import kitra.employeetraining.common.datamodel.Course;
import kitra.employeetraining.common.util.EntityUtils;
import kitra.employeetraining.common.util.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class CourseSelectPanel extends JPanel {
    private final JFrame parent;
    private final int personId;
    private JTable table;
    private Vector<String> columnVector;
    private Vector<Vector<String>> rowVector = new Vector<>();
    private List<Course> selectedCourses;

    public CourseSelectPanel(JFrame parent, int personId) {
        this.parent = parent;
        this.personId = personId;
        this.setLayout(new BorderLayout());
        this.add(createDataPanel(), BorderLayout.CENTER);
        this.add(createFunctionPanel(), BorderLayout.SOUTH);
        this.setBorder(new EmptyBorder(0, 0, 10, 0));
        updateData();
    }

    private JPanel createDataPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        columnVector = EntityUtils.getColumnNames(Course.class);
        table = new JTable(rowVector, columnVector) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);
        JLabel title = new JLabel("已选课程列表", JLabel.CENTER);
        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        return panel;
    }

    private JPanel createFunctionPanel() {
        // 按钮Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        // 提示Panel
        JPanel tipsPanel = createTipsPanel();

        JButton buttonAdd = new JButton("添加选课");
        buttonAdd.addActionListener(e -> {
            addSelectCourse();
        });

        JButton buttonRemove = new JButton("删除选课");
        buttonRemove.addActionListener(e -> {
            removeSelectedCourse();
        });

        buttonPanel.add(buttonAdd);
        buttonPanel.add(buttonRemove);

        JPanel functionPanel = new JPanel();
        functionPanel.setLayout(new BoxLayout(functionPanel, BoxLayout.Y_AXIS));
        functionPanel.add(buttonPanel);
        functionPanel.add(tipsPanel);
        return functionPanel;
    }

    private JPanel createTipsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("点击添加选课按钮添加选课，选择一门课程后点击删除选课可删除选课", JLabel.CENTER), BorderLayout.CENTER);
        return panel;
    }

    private void updateData() {
        selectedCourses = new ArrayList<>();
        try(SqlSession session = MyBatisUtils.getSqlSession()) {
            CourseSelectDao courseSelectMapper = session.getMapper(CourseSelectDao.class);
            CourseDao courseMapper = session.getMapper(CourseDao.class);
            List<Integer> selectedCoursesId = courseSelectMapper.getSelectedCourses(personId);
            for(int courseId : selectedCoursesId) {
                selectedCourses.add(courseMapper.getById(courseId));
            }
        }
        rowVector.clear();
        for(Course course : selectedCourses) {
            Vector<String> contentVector = EntityUtils.getStringVectorFromEntity(course);
            for(int i = 0; i < contentVector.size(); i++) {
                if(contentVector.get(i).isEmpty())
                    contentVector.set(i, "<空>");
            }
            rowVector.add(contentVector);
        }
        table.updateUI();
    }

    private void addSelectCourse() {
        AddCourseSelectDialog dialog = new AddCourseSelectDialog(parent);
        if(dialog.hasResult()) {
            int courseId = dialog.getResult();
            for(Course course : selectedCourses) {
                if(course.getId() == courseId) {
                    JOptionPane.showMessageDialog(this, "不能重复选课！", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            try(SqlSession session = MyBatisUtils.getSqlSession()) {
                CourseSelectDao courseSelectMapper = session.getMapper(CourseSelectDao.class);
                CourseDao courseMapper = session.getMapper(CourseDao.class);
                int max = courseMapper.getById(courseId).getNumber();
                int count = courseSelectMapper.getSelectedCount(courseId);
                if(count >= max) {
                    JOptionPane.showMessageDialog(this, "选课人数已达到上限，不能选课。", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if(courseMapper.getById(courseId).getState().getCode() != 0) {
                    JOptionPane.showMessageDialog(this, "选课已结束，不能选课。", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                courseSelectMapper.selectCourse(personId, courseId);
                session.commit();
            }
            JOptionPane.showMessageDialog(this, "选课成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
        }
        updateData();
    }

    private void removeSelectedCourse() {
        int selectedRow = table.getSelectedRow();
        if(selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择一门课程", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int option = JOptionPane.showConfirmDialog(this, "确定要取消选课 " + selectedCourses.get(selectedRow).toString() + "？", "取消选课", JOptionPane.YES_NO_OPTION);
        if(option == JOptionPane.YES_OPTION) {
            try(SqlSession session = MyBatisUtils.getSqlSession()) {
                CourseSelectDao mapper = session.getMapper(CourseSelectDao.class);
                mapper.unselectCourse(personId, selectedCourses.get(selectedRow).getId());
                session.commit();
            }
            JOptionPane.showMessageDialog(this, "取消选课成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
        }
        updateData();
    }
}
