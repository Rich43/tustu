package javax.swing.table;

import java.util.Enumeration;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableColumnModelListener;

/* loaded from: rt.jar:javax/swing/table/TableColumnModel.class */
public interface TableColumnModel {
    void addColumn(TableColumn tableColumn);

    void removeColumn(TableColumn tableColumn);

    void moveColumn(int i2, int i3);

    void setColumnMargin(int i2);

    int getColumnCount();

    Enumeration<TableColumn> getColumns();

    int getColumnIndex(Object obj);

    TableColumn getColumn(int i2);

    int getColumnMargin();

    int getColumnIndexAtX(int i2);

    int getTotalColumnWidth();

    void setColumnSelectionAllowed(boolean z2);

    boolean getColumnSelectionAllowed();

    int[] getSelectedColumns();

    int getSelectedColumnCount();

    void setSelectionModel(ListSelectionModel listSelectionModel);

    ListSelectionModel getSelectionModel();

    void addColumnModelListener(TableColumnModelListener tableColumnModelListener);

    void removeColumnModelListener(TableColumnModelListener tableColumnModelListener);
}
