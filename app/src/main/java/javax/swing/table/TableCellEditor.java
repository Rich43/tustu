package javax.swing.table;

import java.awt.Component;
import javax.swing.CellEditor;
import javax.swing.JTable;

/* loaded from: rt.jar:javax/swing/table/TableCellEditor.class */
public interface TableCellEditor extends CellEditor {
    Component getTableCellEditorComponent(JTable jTable, Object obj, boolean z2, int i2, int i3);
}
