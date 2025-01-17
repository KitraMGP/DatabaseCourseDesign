package kitra.employeetraining.employee.ui;

import kitra.employeetraining.common.dao.TrainingPlanDao;
import kitra.employeetraining.common.datamodel.TrainingPlan;
import kitra.employeetraining.common.util.EntityUtils;
import kitra.employeetraining.common.util.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.Vector;

public class GradePanel extends JPanel {
    private final JFrame parent;
    private final int personId;
    private JTable table;
    private Vector<String> columnVector;
    private Vector<Vector<String>> rowVector = new Vector<>();

    public GradePanel(JFrame parent, int personId) {
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
        columnVector = EntityUtils.getColumnNames(TrainingPlan.class);
        table = new JTable(rowVector, columnVector) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);
        JLabel title = new JLabel("已选课程成绩列表", JLabel.CENTER);
        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        return panel;
    }

    private JPanel createFunctionPanel() {
        // 按钮Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton updateButton = new JButton("刷新");
        updateButton.addActionListener(e -> updateData());
        buttonPanel.add(updateButton);
        return buttonPanel;
    }

    private void updateData() {
        List<TrainingPlan> trainingPlanList;
        try(SqlSession session = MyBatisUtils.getSqlSession()) {
            trainingPlanList = session.getMapper(TrainingPlanDao.class).getByPerson(personId);
        }
        rowVector.clear();
        for(TrainingPlan trainingPlan : trainingPlanList) {
            Vector<String> contentVector = EntityUtils.getStringVectorFromEntity(trainingPlan);
            for(int i = 0; i < contentVector.size(); i++) {
                if(contentVector.get(i).isEmpty())
                    contentVector.set(i, "<空>");
            }
            rowVector.add(contentVector);
        }
        table.updateUI();
    }
}
