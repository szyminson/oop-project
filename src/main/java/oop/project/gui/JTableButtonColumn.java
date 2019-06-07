package oop.project.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class JTableButtonColumn extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ActionListener {
    private JTable table;

    private Object editorValue;
    private int column;

    JTableButtonColumn(JTable table) {
        this.table = table;
    }

    void addToColumn(int column) {
        this.column = column;
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(column).setCellRenderer(this);
        columnModel.getColumn(column).setCellEditor(this);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.editorValue = value;
        return (JButton) value;
    }

    @Override
    public Object getCellEditorValue() {
        return editorValue;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return (JButton) value;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        fireEditingStopped();
        for (int row = 0; row < table.getRowCount(); row++) {
            if (table.getModel().getValueAt(row, column) == e.getSource()) {
                if (row == table.getRowCount() - 1) {
                    ((DefaultTableModel) table.getModel()).insertRow(row, new Object[]{newButton(), 0.0, 0.0});
                } else {
                    ((DefaultTableModel) table.getModel()).removeRow(row);
                }
                break;
            }
        }
    }

    JButton newButton() {
        JButton newButton = new JButton("Remove");
        newButton.addActionListener(this);
        return newButton;
    }
}
