package kitra.employeetraining.common.util;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;

public class JTableUtils {
    public static void autoResizeColumnWidthToHeader(JTable table) {
        JTableHeader header = table.getTableHeader();
        TableCellRenderer renderer = header.getDefaultRenderer();

        int totalWidth = 0;
        for (int i = 0; i < table.getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            Component headerComponent = renderer.getTableCellRendererComponent(table, column.getHeaderValue(), false, false, -1, i);
            int headerPreferredWidth = headerComponent.getPreferredSize().width;
            column.setPreferredWidth(headerPreferredWidth + 50);
            totalWidth += headerPreferredWidth + 50;
        }

        Dimension tableSize = new Dimension(totalWidth, table.getPreferredSize().height);
        table.setPreferredScrollableViewportSize(tableSize);
        table.setPreferredSize(tableSize);
    }
}
