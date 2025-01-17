package kitra.employeetraining.employee.ui;

import kitra.employeetraining.common.datamodel.Person;
import kitra.employeetraining.common.ui.PasswordResetDialog;
import kitra.employeetraining.common.ui.PasswordVerifyDialog;
import kitra.employeetraining.common.util.EntityUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class BasicInformationPanel extends JPanel {

    private final JFrame parent;
    private final int personId;
    private Person personEntity;
    private Object[] entityParams;
    private Vector<String> fieldNames;

    private Vector<String> fieldValues = new Vector<>();
    private List<JLabel> labelList;
    private List<DataTextField> textFieldList;

    public BasicInformationPanel(JFrame parent, int personId) {
        this.parent = parent;
        this.personId = personId;
        this.fieldNames = EntityUtils.getColumnNames(Person.class);
        this.setLayout(new BorderLayout());

        JPanel container = new JPanel(new FlowLayout(FlowLayout.LEADING));
        container.add(createInformationPanel());

        JPanel functionPanel = createFunctionPanel();
        this.add(container, BorderLayout.CENTER);
        this.add(functionPanel, BorderLayout.SOUTH);
        updateData();
    }

    private JPanel createFunctionPanel() {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEADING);
        flowLayout.setHgap(20);
        JPanel functionPanel = new JPanel(flowLayout);
        functionPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        JButton buttonSave = new JButton("保存");
        buttonSave.addActionListener(e -> saveData());
        JButton buttonReset = new JButton("重置");
        buttonReset.addActionListener(e -> updateData());
        functionPanel.add(buttonSave);
        functionPanel.add(buttonReset);
        return functionPanel;
    }

    private JPanel createInformationPanel() {
        fetchEntity();
        JPanel panel = new JPanel();
        GroupLayout groupLayout = new GroupLayout(panel);
        panel.setLayout(groupLayout);

        groupLayout.setAutoCreateContainerGaps(true);
        GroupLayout.SequentialGroup horizontalGroup = groupLayout.createSequentialGroup();
        GroupLayout.SequentialGroup verticalGroup = groupLayout.createSequentialGroup();

        labelList = new ArrayList<>();
        textFieldList = new ArrayList<>();

        labelList.add(new JLabel("员工号"));
        textFieldList.add(createTextField("员工号", false));

        labelList.add(new JLabel("姓名"));
        textFieldList.add(createTextField("姓名", true));

        labelList.add(new JLabel("性别"));
        textFieldList.add(createTextField("性别", true));

        labelList.add(new JLabel("出生日期"));
        textFieldList.add(createTextField("出生日期", true));

        labelList.add(new JLabel("专业技能"));
        textFieldList.add(createTextField("专业技能", true));

        labelList.add(new JLabel("家庭住址"));
        textFieldList.add(createTextField("家庭住址", true));

        labelList.add(new JLabel("联系电话"));
        textFieldList.add(createTextField("联系电话", true));

        labelList.add(new JLabel("电子信箱"));
        textFieldList.add(createTextField("电子信箱", true));

        labelList.add(new JLabel("备注"));
        textFieldList.add(createTextField("备注", true));

        JLabel passwordLabel = new JLabel("密码");
        JButton passwordVerifyButton = new JButton("验证密码");
        passwordVerifyButton.addActionListener(e -> new PasswordVerifyDialog(parent, personId));
        JButton passwordResetButton = new JButton("重置密码");
        passwordResetButton.addActionListener(e -> new PasswordResetDialog(parent, personId));
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEADING);
        flowLayout.setHgap(0);
        flowLayout.setVgap(0);
        JPanel passwordButtonPanel = new JPanel(flowLayout);

        passwordButtonPanel.add(passwordVerifyButton);
        passwordButtonPanel.add(passwordResetButton);

        GroupLayout.ParallelGroup labelGroup = groupLayout.createParallelGroup();
        GroupLayout.ParallelGroup fieldGroup = groupLayout.createParallelGroup();

        for(int i = 0; i < labelList.size(); i++) {
            labelGroup.addComponent(labelList.get(i));
            fieldGroup.addComponent(textFieldList.get(i));
        }
        labelGroup.addComponent(passwordLabel);
        fieldGroup.addComponent(passwordButtonPanel);

        horizontalGroup.addGroup(labelGroup);
        horizontalGroup.addGap(20);
        horizontalGroup.addGroup(fieldGroup);
        for(int i = 0; i < labelList.size(); i++) {
            GroupLayout.ParallelGroup lineGroup = groupLayout.createParallelGroup();
            lineGroup.addComponent(labelList.get(i));
            lineGroup.addComponent(textFieldList.get(i));
            verticalGroup.addGroup(lineGroup);
        }

        GroupLayout.ParallelGroup passwordLineGroup = groupLayout.createParallelGroup();
        passwordLineGroup.addComponent(passwordLabel);
        passwordLineGroup.addComponent(passwordButtonPanel);
        verticalGroup.addGroup(passwordLineGroup);

        groupLayout.setHorizontalGroup(horizontalGroup);
        groupLayout.setVerticalGroup(verticalGroup);

        return panel;
    }

    private void fetchEntity() {
        this.personEntity = (Person) EntityUtils.getEntityById(Person.class, this.personId);
        if(personEntity == null) throw new RuntimeException("Invalid person id " + personId);
        this.entityParams = EntityUtils.getEntityParams(personEntity);
    }

    private void updateData() {
        fetchEntity();
        this.fieldValues.clear();
        if(this.personEntity != null)
            this.fieldValues = EntityUtils.getStringVectorFromEntity(this.personEntity);
        for(DataTextField textField : this.textFieldList) {
            textField.updateData();
        }
    }

    private void saveData() {
        for(int i = 1; i < textFieldList.size(); i++) {
            int index = textFieldList.get(i).getIndex();
            entityParams[index] = textFieldList.get(i).getText();
        }

        EntityUtils.updateEntity(Person.class, entityParams);
        updateData();
    }

    private String getValue(String columnName) {
        int index = fieldNames.indexOf(columnName);
        if(index == -1 || fieldValues.isEmpty()) return "";
        return fieldValues.get(index);
    }

    private DataTextField createTextField(String columnName, boolean enabled) {
        return new DataTextField(columnName, 20, enabled);
    }

    private class DataTextField extends JTextField {
        private final String columnName;
        // 在entityParams中的index
        private final int index;

        public DataTextField(String columnName, int columns, boolean enabled) {
            super(columns);
            this.setEnabled(enabled);
            this.columnName = columnName;
            this.index = fieldNames.indexOf(columnName);
            this.updateData();
        }

        public void updateData() {
            if(this.isEnabled())
                this.setText(getValue(columnName));
            else
                this.setText(getValue(columnName) + "<不可更改>");
        }

        public int getIndex() {
            return this.index;
        }
    }
}
