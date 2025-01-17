package kitra.employeetraining.manager.ui;

import kitra.employeetraining.common.annotation.EntityClass;
import kitra.employeetraining.common.datamodel.Entity;
import kitra.employeetraining.common.util.EntityUtils;
import kitra.employeetraining.common.util.JTableUtils;
import kitra.employeetraining.common.util.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Vector;

/**
 * 用于外键搜索
 */
public class EntitySearchDialog extends JDialog {

    private final Vector<String> columnVector;
    protected final Vector<Vector<String>> rowVector = new Vector<>();
    private final Class<? extends Entity> entityClass;
    private final Class<?> daoClass;
    protected final boolean[] importantColumns;
    protected JTable table;
    private boolean hasResult = false;
    private int result;

    public EntitySearchDialog(JFrame owner, Class<? extends Entity> entityClass) {
        super(owner, "请双击选择一个项目", true);
        this.setLayout(new BorderLayout());
        this.entityClass = entityClass;
        this.daoClass = entityClass.getAnnotation(EntityClass.class).daoClass();
        this.importantColumns = EntityUtils.getIsImportantColumn(entityClass);
        this.columnVector = EntityUtils.filteredGetColumnNames(entityClass, importantColumns);
        this.add(createDataView(), BorderLayout.CENTER);
        this.add(createFunctionView(), BorderLayout.SOUTH);
        this.setMaximumSize(new Dimension(800, 800));
        this.pack();
        this.setLocationRelativeTo(owner);
        this.setVisible(true);
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
        JDialog dialog = this;
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.rowAtPoint(e.getPoint());
                    int id = Integer.parseInt((String) table.getValueAt(row, 0));
                    hasResult = true;
                    result = id;
                    dialog.dispose();
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        updateData();
        return panel;
    }

    private JPanel createFunctionView() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton buttonSetNull = new JButton("清除选择");
        JButton buttonCancel = new JButton("取消");
        buttonSetNull.addActionListener(e -> {
            result = -1;
            hasResult = true;
            dispose();
        });
        buttonCancel.addActionListener(e -> dispose());
        panel.add(buttonSetNull);
        panel.add(buttonCancel);
        return panel;
    }

    protected void updateData() {
        try (SqlSession session = MyBatisUtils.getSqlSession()) {
            Object mapper = session.getMapper(daoClass);
            List<?> entityList = (List<?>) (mapper.getClass().getMethod("getAll").invoke(mapper));
            rowVector.clear();
            for (Object o : entityList) {
                rowVector.add(EntityUtils.filteredGetStringVectorFromEntity((Entity) o, importantColumns));
            }
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        table.updateUI();
        JTableUtils.autoResizeColumnWidthToHeader(table);
    }

    public boolean hasResult() {
        return hasResult;
    }

    public int getResult() {
        return result;
    }
}
