package javax.swing.table;

import javax.swing.event.TableModelListener;

/* loaded from: rt.jar:javax/swing/table/TableModel.class */
public interface TableModel {
    int getRowCount();

    int getColumnCount();

    String getColumnName(int i2);

    Class<?> getColumnClass(int i2);

    boolean isCellEditable(int i2, int i3);

    Object getValueAt(int i2, int i3);

    void setValueAt(Object obj, int i2, int i3);

    void addTableModelListener(TableModelListener tableModelListener);

    void removeTableModelListener(TableModelListener tableModelListener);
}
