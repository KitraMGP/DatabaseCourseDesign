package kitra.employeetraining.manager.ui;

import kitra.employeetraining.manager.tools.GradeExportTool;

import javax.swing.*;
import java.awt.*;

public class DataExportPanel extends JPanel {
    private final JFrame parent;

    public DataExportPanel(JFrame parent) {
        this.parent = parent;
        this.setLayout(new BorderLayout());
        this.add(createFunctionPanel(), BorderLayout.CENTER);
    }

    private JPanel createFunctionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JButton gradeExportButton = new JButton("导出成绩报表");
        gradeExportButton.addActionListener(e -> doExport());
        panel.add(gradeExportButton);
        return panel;
    }

    private void doExport() {
        try {
            GradeExportTool.exportData(parent);
            JOptionPane.showMessageDialog(parent, "导出成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent, "导出时发生错误：" + e.getMessage(), "导出失败", JOptionPane.WARNING_MESSAGE);
        }
    }
}
