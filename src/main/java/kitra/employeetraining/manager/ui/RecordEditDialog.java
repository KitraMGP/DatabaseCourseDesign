package kitra.employeetraining.manager.ui;

import kitra.employeetraining.Main;
import kitra.employeetraining.common.annotation.EntityClass;
import kitra.employeetraining.common.auth.PasswordVerify;
import kitra.employeetraining.common.dao.CourseDao;
import kitra.employeetraining.common.dao.CourseSelectDao;
import kitra.employeetraining.common.dao.PersonDao;
import kitra.employeetraining.common.dao.TrainingPlanDao;
import kitra.employeetraining.common.datamodel.*;
import kitra.employeetraining.common.ui.AdminSetPasswordDialog;
import kitra.employeetraining.common.util.EntityUtils;
import kitra.employeetraining.common.util.JTableUtils;
import kitra.employeetraining.common.util.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class RecordEditDialog extends JDialog {

    private final JFrame parent;
    private final Class<? extends Entity> entityClass;
    private final Vector<String> entityFieldNames;
    private final Entity entity;
    private final boolean[] isForeignKey;
    private final Field[] entityFields;
    private final Object[] entityParams;
    private final boolean isInsertDialog;
    private List<Component> textFieldList;
    // 记录这个外键是否已设定，由saveAndClose方法的检查功能使用，由createDataPanel方法和notifyItemSelection方法设置
    private final boolean[] isForeignKeySet;
    private final JButton buttonDelete = new JButton("删除");

    /**
     * 打开一个数据库记录编辑窗口，它不需要依靠其他的窗口就可以工作，可支持所有的实体类型
     *
     * @param owner       父窗体，可为null
     * @param entityClass 实体类
     * @param entity      实体类的实例，若为null则打开新建数据库记录的窗口
     */
    public RecordEditDialog(JFrame owner, Class<? extends Entity> entityClass, Entity entity) {
        super(owner, true);
        this.isInsertDialog = entity == null;
        this.setTitle(isInsertDialog ? "新建数据项" : "编辑数据");
        try {
            if (entity == null) {
                this.entity = entityClass.getConstructor().newInstance();
            } else {
                this.entity = entity;
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        this.parent = owner;
        this.entityClass = entityClass;
        this.entityFieldNames = EntityUtils.getColumnNames(entityClass);
        this.isForeignKey = EntityUtils.getIsForeignKey(entityClass);
        this.isForeignKeySet = new boolean[isForeignKey.length];

        this.entityFields = EntityUtils.getFields(entityClass);
        this.entityParams = EntityUtils.getEntityParams(this.entity);


        this.setLayout(new BorderLayout());
        this.add(createDataPanel(), BorderLayout.NORTH);
        this.add(createFunctionPanel(), BorderLayout.CENTER);
        this.add(createTipsPanel(), BorderLayout.SOUTH);
        this.pack();
        this.setLocationRelativeTo(owner);
        this.setMinimumSize(this.getPreferredSize());
        this.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    private JPanel createDataPanel() {
        JPanel panel = new JPanel();
        GroupLayout groupLayout = new GroupLayout(panel);
        panel.setLayout(groupLayout);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);

        // 开始设置GroupLayout
        List<JLabel> labelList = new ArrayList<>(entityFieldNames.size());
        textFieldList = new ArrayList<>(entityFieldNames.size());

        for (int i = 0; i < entityFieldNames.size(); i++) {
            labelList.add(new JLabel(entityFieldNames.get(i)));
            if (isForeignKey[i]) {
                Field foreignKeyField = entityClass.getDeclaredFields()[i];
                Class<?> foreignKeyType = foreignKeyField.getType();
                if (Entity.class.isAssignableFrom(foreignKeyType))
                    textFieldList.add(new ItemSelectButton(parent, this, (Class<? extends Entity>) foreignKeyType));
            } else {
                textFieldList.add(new JTextField(15));
            }
        }

        // 将实体的属性填充到文本框
        if (this.entity != null) {
            Vector<Object> textFieldContentVector = new Vector<>(List.of(EntityUtils.getEntityParams(this.entity)));
            for (int i = 0; i < entityFieldNames.size(); i++) {
                Component component = textFieldList.get(i);
                if (component instanceof JTextField) {
                    ((JTextField) textFieldList.get(i)).setText(textFieldContentVector.get(i).toString());
                } else if (component instanceof ItemSelectButton) {
                    try {
                        // 将外键对象转换为可读字符串
                        Object obj = entityFields[i].get(entity);
                        String content;
                        if (obj == null) {
                            content = "<请选择>";
                        }
                        else {
                            content = obj.toString();
                            isForeignKeySet[i] = true;
                        }
                        ((ItemSelectButton) textFieldList.get(i)).setText(content);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        }
        // 设置第一个属性（id）不可更改
        if (textFieldList.get(0) instanceof JTextField firstTextField) {
            firstTextField.setText(firstTextField.getText() + "<不可更改>");
            firstTextField.setEnabled(false);
        }
        // 特判：如果是员工表的密码，则不允许直接更改，也不允许删除当前登录的用户
        if (entityClass == Person.class) {
            JButton passwordResetButton = new JButton("更改密码");
            passwordResetButton.addActionListener(e -> {
                new AdminSetPasswordDialog(parent, (Integer) entityParams[0]);
                // 把更新后的密码同步到entityParams中，防止密码保存时被覆盖
                try(SqlSession session = MyBatisUtils.getSqlSession()) {
                    Person person = session.getMapper(PersonDao.class).getById((Integer) entityParams[0]);
                    entityParams[2] = person.getPassword();
                }
            });
            // 新建员工时不可以直接设置密码
            if(isInsertDialog) passwordResetButton.setEnabled(false);
            textFieldList.set(2, passwordResetButton);
            // 不允许删除当前登录的账号，也不允许修改当前登录的账号的权限
            if((int) entityParams[0] == Main.getUserId()) {
                buttonDelete.setEnabled(false);
                textFieldList.get(3).setEnabled(false);
            }
        }


        // 水平垂直各一个SequantalGroup
        GroupLayout.SequentialGroup horizontalSequentalGroup = groupLayout.createSequentialGroup();
        GroupLayout.SequentialGroup verticalSequentalGroup = groupLayout.createSequentialGroup();

        // 水平2个ParallelGroup，竖直n个ParallelGroup
        // 水平ParallelGroup
        GroupLayout.ParallelGroup labelParallelGroup = groupLayout.createParallelGroup();
        labelList.forEach(labelParallelGroup::addComponent);
        GroupLayout.ParallelGroup textFieldParallelGroup = groupLayout.createParallelGroup();
        textFieldList.forEach(textFieldParallelGroup::addComponent);

        horizontalSequentalGroup.addGroup(labelParallelGroup);
        horizontalSequentalGroup.addGroup(textFieldParallelGroup);

        // 竖直ParallelGroup
        for (int i = 0; i < entityFieldNames.size(); i++) {
            GroupLayout.ParallelGroup parallelGroup = groupLayout.createParallelGroup();
            parallelGroup.addComponent(labelList.get(i));
            parallelGroup.addComponent(textFieldList.get(i));
            verticalSequentalGroup.addGroup(parallelGroup);
        }

        groupLayout.setHorizontalGroup(horizontalSequentalGroup);
        groupLayout.setVerticalGroup(verticalSequentalGroup);
        return panel;
    }

    private JPanel createFunctionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton buttonSave = new JButton("保存");
        JButton buttonCancel = new JButton("取消");
        //buttonDelete = new JButton("删除");
        buttonSave.addActionListener(e -> this.saveAndClose());
        buttonCancel.addActionListener(e -> this.dispose());
        buttonDelete.addActionListener(e -> this.delete());
        buttonSave.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("alt ENTER"), "saveAction");
        buttonSave.getActionMap().put("saveAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAndClose();
            }
        });
        buttonCancel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "cancelAction");
        buttonCancel.getActionMap().put("cancelAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonDelete.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("alt DELETE"), "deleteAction");
        buttonDelete.getActionMap().put("deleteAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                delete();
            }
        });
        panel.add(buttonSave);
        panel.add(buttonCancel);
        // 如果是新建数据，则不能删除
        if(!isInsertDialog)
            panel.add(buttonDelete);
        return panel;
    }

    private JPanel createTipsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("按Alt+Enter键保存，按Esc键取消，按Alt+Delete删除本条记录", JLabel.CENTER), BorderLayout.CENTER);
        panel.setBorder(new EmptyBorder(0, 10, 10, 10));
        return panel;
    }

    private void notifyItemSelection(ItemSelectButton button, int selection) {
        int index = textFieldList.indexOf(button);
        if (index == -1) throw new RuntimeException("Invalid index " + index);
        if(selection == -1) {
            entityParams[index] = null;
            button.setText("<请选择>");
            isForeignKeySet[index] = false;
        } else {
            entityParams[index] = selection;
            isForeignKeySet[index] = true;
            try (SqlSession session = MyBatisUtils.getSqlSession()) {
                Field fkField = entityFields[index];
                Object daoObject = session.getMapper(entityFields[index].getType().getAnnotation(EntityClass.class).daoClass());
                Object fkEntity = daoObject.getClass().getMethod("getById", Integer.class).invoke(daoObject, selection);
                fkField.set(entity, fkEntity);
                button.setText(fkEntity.toString());
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void saveAndClose() {
        for (int i = 0; i < isForeignKey.length; i++) {
            String label = "\"" + entityFieldNames.get(i) + "\"";
            // 判断是否有未选择选项的外键
            if(isForeignKey[i]) {
                if(!isForeignKeySet[i]) {
                    //JOptionPane.showMessageDialog(this, label + " 为空，请先选择一个选项！", "错误", JOptionPane.WARNING_MESSAGE);
                    //return;
                    entityParams[i] = null;
                }
                // 不允许重复插入课程成绩
                if(entityClass == TrainingPlan.class) {
                    if(isInsertDialog) {
                        try(SqlSession session = MyBatisUtils.getSqlSession()) {
                            TrainingPlan plan = session.getMapper(TrainingPlanDao.class).get((Integer) entityParams[1], (Integer) entityParams[2]);
                            if(plan != null) {
                                JOptionPane.showMessageDialog(this, "课程成绩不能重复录入！", "错误", JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                        }
                    }
                }
            }
            // 处理输入的数据
            if (!isForeignKey[i] && i != 0) {
                // 处理特判情况：有的文本框被替换为了按钮
                if(!(textFieldList.get(i) instanceof JTextField)) continue;
                JTextField textField = (JTextField) textFieldList.get(i);
                try {
                    if (entityParams[i] instanceof String) {
                        if(entityFieldNames.get(i).equals("密码")) continue;
                        if(entityFieldNames.get(i).equals("登录用户名(必填)")) {
                            if(textField.getText().isEmpty()) {
                                JOptionPane.showMessageDialog(this, "登录用户名不能为空！", "错误", JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                            // 新插入用户，或者修改登录名，检测是否重复
                            if((isInsertDialog || !entityParams[i].equals(textField.getText())) && PasswordVerify.hasAccount(textField.getText())) {
                                JOptionPane.showMessageDialog(this, "登录用户名重复，请修改！", "错误", JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                        }
                        if (textField.getText().length() >= 255) {
                            JOptionPane.showMessageDialog(this, label + " 长度超出限制，请修改！", "错误", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        entityParams[i] = textField.getText();
                    } else if (entityParams[i] instanceof Integer)
                        entityParams[i] = Integer.parseInt(textField.getText());
                    else if (entityParams[i] instanceof Short)
                        entityParams[i] = Short.parseShort(textField.getText());
                    else throw new RuntimeException("无法解析输入参数，index = " + i);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, label + " 输入数字格式错误：" + textField.getText(), "错误", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
        }
        int result;
        if (this.isInsertDialog) {
            result = EntityUtils.insertEntity(entityClass, entityParams);
        } else {
            result = EntityUtils.updateEntity(entityClass, entityParams);
        }
        if (result > 0)
            this.dispose();
        else
            JOptionPane.showMessageDialog(this, "保存失败，请检查输入内容是否有误！", "错误", JOptionPane.WARNING_MESSAGE);
    }

    private void delete() {
        int selection = JOptionPane.showConfirmDialog(this, "确定要删除吗？", "删除确认", JOptionPane.OK_CANCEL_OPTION);
        if(selection == JOptionPane.OK_OPTION) {
            int result = EntityUtils.deleteEntity(entityClass, (int) entityParams[0]);
            if(result > 0)
                this.dispose();
            else // 程序出错删除失败
                JOptionPane.showMessageDialog(this, "删除失败！", "错误", JOptionPane.WARNING_MESSAGE);
        }
    }

    private static class ItemSelectButton extends JButton {

        private final JFrame parent;
        private final RecordEditDialog dialog;
        private final Class<? extends Entity> entityClass;

        public ItemSelectButton(JFrame parent, RecordEditDialog dialog, Class<? extends Entity> entityClass) {
            super();
            this.parent = parent;
            this.dialog = dialog;
            this.entityClass = entityClass;
            this.addActionListener(this::onClick);
        }

        private void onClick(ActionEvent event) {
            EntitySearchDialog entitySearchDialog;
            // 特判：如果是成绩管理，只显示普通员工，且只显示员工选了的课程
            if(dialog.entityClass == TrainingPlan.class) {
                if(this.entityClass == Person.class) {
                    entitySearchDialog = new EntitySearchDialog(parent, entityClass) {
                        @Override
                        protected void updateData() {
                            try (SqlSession session = MyBatisUtils.getSqlSession()) {
                                PersonDao mapper = session.getMapper(PersonDao.class);
                                List<Person> personList = mapper.getAllExceptAdmins();
                                rowVector.clear();
                                for (Person o : personList) {
                                    rowVector.add(EntityUtils.filteredGetStringVectorFromEntity(o, importantColumns));
                                }
                            }
                            table.updateUI();
                            JTableUtils.autoResizeColumnWidthToHeader(table);
                        }
                    };
                } else if(this.entityClass == Course.class) {
                    entitySearchDialog = new EntitySearchDialog(parent, entityClass) {
                        @Override
                        protected void updateData() {
                            try (SqlSession session = MyBatisUtils.getSqlSession()) {
                                CourseSelectDao courseSelectMapper = session.getMapper(CourseSelectDao.class);
                                CourseDao courseMapper = session.getMapper(CourseDao.class);
                                List<Integer> courseIdList = courseSelectMapper.getSelectedCourses((Integer)dialog.entityParams[1]);
                                rowVector.clear();
                                for (int o : courseIdList) {
                                    Course course = courseMapper.getById(o);
                                    rowVector.add(EntityUtils.filteredGetStringVectorFromEntity(course, importantColumns));
                                }
                            }
                            table.updateUI();
                            JTableUtils.autoResizeColumnWidthToHeader(table);
                        }
                    };
                } else {
                    entitySearchDialog = new EntitySearchDialog(parent, entityClass);
                }
                // 如果是部门管理，要求部门经理必须是这个部门的
            } else if(dialog.entityClass == Department.class) {
                if(this.entityClass == Person.class) {
                    entitySearchDialog = new EntitySearchDialog(parent, entityClass) {
                        @Override
                        protected void updateData() {
                            try (SqlSession session = MyBatisUtils.getSqlSession()) {
                                PersonDao personMapper = session.getMapper(PersonDao.class);
                                List<Person> personList = personMapper.getPersonsInDepartment((Integer)dialog.entityParams[0]);
                                rowVector.clear();
                                for (Person o : personList) {
                                    rowVector.add(EntityUtils.filteredGetStringVectorFromEntity(o, importantColumns));
                                }
                            }
                            table.updateUI();
                            JTableUtils.autoResizeColumnWidthToHeader(table);
                        }
                    };
                } else {
                    entitySearchDialog = new EntitySearchDialog(parent, entityClass);
                }
            } else {
                entitySearchDialog = new EntitySearchDialog(parent, entityClass);
            }
            if (entitySearchDialog.hasResult()) acceptSelection(entitySearchDialog.getResult());
        }

        private void acceptSelection(int selection) {
            dialog.notifyItemSelection(this, selection);
        }

        @Override
        public void setText(String text) {
            // 防止按钮内容为空时变成一个条难以点击
            if(text.isEmpty())
                text = "   ";
            super.setText(text);
        }
    }

}
