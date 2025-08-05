package javax.swing.table;

import java.io.Serializable;
import java.util.EventListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/* loaded from: rt.jar:javax/swing/table/AbstractTableModel.class */
public abstract class AbstractTableModel implements TableModel, Serializable {
    protected EventListenerList listenerList = new EventListenerList();

    @Override // javax.swing.table.TableModel
    public String getColumnName(int i2) {
        String str = "";
        while (i2 >= 0) {
            str = ((char) (((char) (i2 % 26)) + 'A')) + str;
            i2 = (i2 / 26) - 1;
        }
        return str;
    }

    public int findColumn(String str) {
        for (int i2 = 0; i2 < getColumnCount(); i2++) {
            if (str.equals(getColumnName(i2))) {
                return i2;
            }
        }
        return -1;
    }

    @Override // javax.swing.table.TableModel
    public Class<?> getColumnClass(int i2) {
        return Object.class;
    }

    @Override // javax.swing.table.TableModel
    public boolean isCellEditable(int i2, int i3) {
        return false;
    }

    @Override // javax.swing.table.TableModel
    public void setValueAt(Object obj, int i2, int i3) {
    }

    @Override // javax.swing.table.TableModel
    public void addTableModelListener(TableModelListener tableModelListener) {
        this.listenerList.add(TableModelListener.class, tableModelListener);
    }

    @Override // javax.swing.table.TableModel
    public void removeTableModelListener(TableModelListener tableModelListener) {
        this.listenerList.remove(TableModelListener.class, tableModelListener);
    }

    public TableModelListener[] getTableModelListeners() {
        return (TableModelListener[]) this.listenerList.getListeners(TableModelListener.class);
    }

    public void fireTableDataChanged() {
        fireTableChanged(new TableModelEvent(this));
    }

    public void fireTableStructureChanged() {
        fireTableChanged(new TableModelEvent(this, -1));
    }

    public void fireTableRowsInserted(int i2, int i3) {
        fireTableChanged(new TableModelEvent(this, i2, i3, -1, 1));
    }

    public void fireTableRowsUpdated(int i2, int i3) {
        fireTableChanged(new TableModelEvent(this, i2, i3, -1, 0));
    }

    public void fireTableRowsDeleted(int i2, int i3) {
        fireTableChanged(new TableModelEvent(this, i2, i3, -1, -1));
    }

    public void fireTableCellUpdated(int i2, int i3) {
        fireTableChanged(new TableModelEvent(this, i2, i2, i3));
    }

    public void fireTableChanged(TableModelEvent tableModelEvent) {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == TableModelListener.class) {
                ((TableModelListener) listenerList[length + 1]).tableChanged(tableModelEvent);
            }
        }
    }

    public <T extends EventListener> T[] getListeners(Class<T> cls) {
        return (T[]) this.listenerList.getListeners(cls);
    }
}
