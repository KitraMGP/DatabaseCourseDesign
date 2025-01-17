package kitra.employeetraining.manager.ui;

import kitra.employeetraining.common.datamodel.Entity;
import kitra.employeetraining.common.util.EntityUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Vector;

public class BaseDataPanel extends JPanel {
    private Vector<Vector<String>> rowVector = new Vector<>();
    private Vector<String> columnVector;
    private JTable table;
    private JTextField searchTextField;
    private JFrame parent;
    private Class<? extends Entity> entityClass;
    private String tableTitle;

    public BaseDataPanel(JFrame parent, Class<? extends Entity> entityClass, String tableTitle) {
        super(new BorderLayout());
        this.parent = parent;
        this.entityClass = entityClass;
        this.columnVector = EntityUtils.getColumnNames(entityClass);
        this.tableTitle = tableTitle;
        this.add(createDataViewPanel(), BorderLayout.CENTER);
        this.add(createFunctionPanel(), BorderLayout.SOUTH);
        this.setBorder(new EmptyBorder(0, 0, 10, 0));

        runSearch();
    }

    private JPanel createDataViewPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        table = new JTable(rowVector, columnVector) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.getTableHeader().setReorderingAllowed(false);
        // 双击表格的一行打开编辑窗口
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.rowAtPoint(e.getPoint());
                    int id = Integer.parseInt((String) table.getValueAt(row, 0));
                    Entity entity = EntityUtils.getEntityById(entityClass, id);
                    new RecordEditDialog(parent, entityClass, entity);
                    runSearch(); // 更新数据表
                }
            }
        });
        JLabel title = new JLabel(tableTitle, JLabel.CENTER);
        JScrollPane scrollPane = new JScrollPane(table);
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
        searchTextField = new JTextField(15);
        searchTextField.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, KeyEvent.ALT_DOWN_MASK), "activateSearchAction");
        searchTextField.getActionMap().put("activateSearchAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                focusSearchTextField();
            }
        });

        JButton buttonQuery = new JButton("搜索");
        buttonQuery.addActionListener(e -> runSearch());
        buttonQuery.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "enterAction");
        buttonQuery.getActionMap().put("enterAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runSearch();
            }
        });

        JButton buttonAdd = new JButton("新建");
        buttonAdd.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("alt N"), "addAction");
        buttonAdd.getActionMap().put("addAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RecordEditDialog(parent, entityClass, null);
                runSearch();
            }
        });
        buttonAdd.addActionListener(e -> {
            new RecordEditDialog(parent, entityClass, null);
            runSearch();
        });

        buttonPanel.add(searchTextField);
        buttonPanel.add(buttonQuery);
        buttonPanel.add(buttonAdd);

        JPanel functionPanel = new JPanel();
        functionPanel.setLayout(new BoxLayout(functionPanel, BoxLayout.Y_AXIS));
        functionPanel.add(buttonPanel);
        functionPanel.add(tipsPanel);
        return functionPanel;
    }

    private JPanel createTipsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("按 Alt+/ 输入或覆盖搜索内容，按 Enter 执行搜索，按 Alt+N 新建数据记录", JLabel.CENTER), BorderLayout.CENTER);
        return panel;
    }

    public void runSearch() {
        List<Entity> entityList;
        String search = searchTextField.getText();
        // 搜索框为空查询全部
        if (search.isBlank()) {
            entityList = EntityUtils.getAll(entityClass);
        } else {
            // 模糊查询
            entityList = EntityUtils.searchByName(entityClass, search);
        }
        rowVector.clear();
        for (Entity entity : entityList) {
            Vector<String> contentVector = EntityUtils.getStringVectorFromEntity(entity);
            for(int i = 0; i < contentVector.size(); i++) {
                if(contentVector.get(i).isEmpty())
                    contentVector.set(i, "<空>");
            }
            rowVector.add(contentVector);
        }
        table.updateUI();
    }

    private void focusSearchTextField() {
        this.searchTextField.dispatchEvent(new FocusEvent(this.searchTextField, FocusEvent.FOCUS_GAINED, true));
        this.searchTextField.requestFocus();
        this.searchTextField.selectAll();
    }

}
