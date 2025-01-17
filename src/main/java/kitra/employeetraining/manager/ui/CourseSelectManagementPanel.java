package kitra.employeetraining.manager.ui;

import kitra.employeetraining.common.dao.PersonDao;
import kitra.employeetraining.common.datamodel.Entity;
import kitra.employeetraining.common.datamodel.Person;
import kitra.employeetraining.common.util.EntityUtils;
import kitra.employeetraining.common.util.MyBatisUtils;
import kitra.employeetraining.employee.ui.CourseSelectPanel;
import org.apache.ibatis.session.SqlSession;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Vector;

public class CourseSelectManagementPanel extends JPanel {
    private final JFrame parent;
    private final Vector<String> columnVector;
    private final Vector<Vector<String>> rowVector = new Vector<>();
    private final boolean[] importantColumns;
    private JTable table;
    private List<Person> personList;

    public CourseSelectManagementPanel(JFrame parent) {
        this(parent, Person.class);
    }

    private CourseSelectManagementPanel(JFrame parent, Class<? extends Entity> entityClass) {
        this.parent = parent;
        this.setLayout(new BorderLayout());
        this.importantColumns = EntityUtils.getIsImportantColumn(entityClass);
        this.columnVector = EntityUtils.filteredGetColumnNames(entityClass, importantColumns);
        this.add(createDataView(), BorderLayout.CENTER);
        this.add(createFunctionView(), BorderLayout.SOUTH);
        this.setBorder(new EmptyBorder(0, 0, 10, 0));
    }

    private JPanel createDataView() {
        JPanel panel = new JPanel(new BorderLayout());
        this.table = new JTable(rowVector, columnVector) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.getTableHeader().setReorderingAllowed(false);
        // 获取双击的列并且返回
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = table.rowAtPoint(e.getPoint());
                    Person person = personList.get(index);
                    new CourseSelectDialog(parent, person.getId());
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(new JLabel("选课管理（双击一位员工来查看/修改选课信息）", JLabel.CENTER), BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        updateData();
        return panel;
    }

    private JPanel createFunctionView() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel tipPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton updateButton = new JButton("刷新列表");
        updateButton.addActionListener(e -> updateData());
        buttonPanel.add(updateButton);
        JLabel tip = new JLabel("双击一位员工来查看/修改选课信息", JLabel.CENTER);
        tipPanel.add(tip);
        JPanel functionPanel = new JPanel();
        functionPanel.setLayout(new BoxLayout(functionPanel, BoxLayout.Y_AXIS));
        functionPanel.add(buttonPanel);
        functionPanel.add(tipPanel);
        return functionPanel;
    }

    private void updateData() {
        try (SqlSession session = MyBatisUtils.getSqlSession()) {
            PersonDao mapper = session.getMapper(PersonDao.class);
            personList = mapper.getAllExceptAdmins();
            rowVector.clear();
            for (Person o : personList) {
                //rowVector.add(EntityUtils.filteredGetStringVectorFromEntity(o, importantColumns));
                Vector<String> contentVector = EntityUtils.filteredGetStringVectorFromEntity(o, importantColumns);
                for(int i = 0; i < contentVector.size(); i++) {
                    if(contentVector.get(i).isEmpty())
                        contentVector.set(i, "<空>");
                }
                rowVector.add(contentVector);
            }

        }
        table.updateUI();
        ///JTableUtils.autoResizeColumnWidthToHeader(table);
    }

    private static class CourseSelectDialog extends JDialog {
        public CourseSelectDialog(JFrame owner, int personId) {
            super(owner, "修改选课信息", true);
            this.setLayout(new BorderLayout());
            this.add(new CourseSelectPanel(owner, personId));
            this.setSize(800, 500);
            this.setLocationRelativeTo(owner);
            this.setVisible(true);
        }
    }
}
