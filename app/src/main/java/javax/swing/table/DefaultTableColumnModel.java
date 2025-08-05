package javax.swing.table;

import com.sun.media.jfxmedia.MetadataParser;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Vector;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;

/* loaded from: rt.jar:javax/swing/table/DefaultTableColumnModel.class */
public class DefaultTableColumnModel implements TableColumnModel, PropertyChangeListener, ListSelectionListener, Serializable {
    protected ListSelectionModel selectionModel;
    protected int columnMargin;
    protected boolean columnSelectionAllowed;
    protected int totalColumnWidth;
    protected EventListenerList listenerList = new EventListenerList();
    protected transient ChangeEvent changeEvent = null;
    protected Vector<TableColumn> tableColumns = new Vector<>();

    public DefaultTableColumnModel() {
        setSelectionModel(createSelectionModel());
        setColumnMargin(1);
        invalidateWidthCache();
        setColumnSelectionAllowed(false);
    }

    @Override // javax.swing.table.TableColumnModel
    public void addColumn(TableColumn tableColumn) {
        if (tableColumn == null) {
            throw new IllegalArgumentException("Object is null");
        }
        this.tableColumns.addElement(tableColumn);
        tableColumn.addPropertyChangeListener(this);
        invalidateWidthCache();
        fireColumnAdded(new TableColumnModelEvent(this, 0, getColumnCount() - 1));
    }

    @Override // javax.swing.table.TableColumnModel
    public void removeColumn(TableColumn tableColumn) {
        int iIndexOf = this.tableColumns.indexOf(tableColumn);
        if (iIndexOf != -1) {
            if (this.selectionModel != null) {
                this.selectionModel.removeIndexInterval(iIndexOf, iIndexOf);
            }
            tableColumn.removePropertyChangeListener(this);
            this.tableColumns.removeElementAt(iIndexOf);
            invalidateWidthCache();
            fireColumnRemoved(new TableColumnModelEvent(this, iIndexOf, 0));
        }
    }

    @Override // javax.swing.table.TableColumnModel
    public void moveColumn(int i2, int i3) {
        if (i2 < 0 || i2 >= getColumnCount() || i3 < 0 || i3 >= getColumnCount()) {
            throw new IllegalArgumentException("moveColumn() - Index out of range");
        }
        if (i2 == i3) {
            fireColumnMoved(new TableColumnModelEvent(this, i2, i3));
            return;
        }
        TableColumn tableColumnElementAt = this.tableColumns.elementAt(i2);
        this.tableColumns.removeElementAt(i2);
        boolean zIsSelectedIndex = this.selectionModel.isSelectedIndex(i2);
        this.selectionModel.removeIndexInterval(i2, i2);
        this.tableColumns.insertElementAt(tableColumnElementAt, i3);
        this.selectionModel.insertIndexInterval(i3, 1, true);
        if (zIsSelectedIndex) {
            this.selectionModel.addSelectionInterval(i3, i3);
        } else {
            this.selectionModel.removeSelectionInterval(i3, i3);
        }
        fireColumnMoved(new TableColumnModelEvent(this, i2, i3));
    }

    @Override // javax.swing.table.TableColumnModel
    public void setColumnMargin(int i2) {
        if (i2 != this.columnMargin) {
            this.columnMargin = i2;
            fireColumnMarginChanged();
        }
    }

    @Override // javax.swing.table.TableColumnModel
    public int getColumnCount() {
        return this.tableColumns.size();
    }

    @Override // javax.swing.table.TableColumnModel
    public Enumeration<TableColumn> getColumns() {
        return this.tableColumns.elements();
    }

    @Override // javax.swing.table.TableColumnModel
    public int getColumnIndex(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Identifier is null");
        }
        Enumeration<TableColumn> columns = getColumns();
        int i2 = 0;
        while (columns.hasMoreElements()) {
            if (obj.equals(columns.nextElement2().getIdentifier())) {
                return i2;
            }
            i2++;
        }
        throw new IllegalArgumentException("Identifier not found");
    }

    @Override // javax.swing.table.TableColumnModel
    public TableColumn getColumn(int i2) {
        return this.tableColumns.elementAt(i2);
    }

    @Override // javax.swing.table.TableColumnModel
    public int getColumnMargin() {
        return this.columnMargin;
    }

    @Override // javax.swing.table.TableColumnModel
    public int getColumnIndexAtX(int i2) {
        if (i2 < 0) {
            return -1;
        }
        int columnCount = getColumnCount();
        for (int i3 = 0; i3 < columnCount; i3++) {
            i2 -= getColumn(i3).getWidth();
            if (i2 < 0) {
                return i3;
            }
        }
        return -1;
    }

    @Override // javax.swing.table.TableColumnModel
    public int getTotalColumnWidth() {
        if (this.totalColumnWidth == -1) {
            recalcWidthCache();
        }
        return this.totalColumnWidth;
    }

    @Override // javax.swing.table.TableColumnModel
    public void setSelectionModel(ListSelectionModel listSelectionModel) {
        if (listSelectionModel == null) {
            throw new IllegalArgumentException("Cannot set a null SelectionModel");
        }
        ListSelectionModel listSelectionModel2 = this.selectionModel;
        if (listSelectionModel != listSelectionModel2) {
            if (listSelectionModel2 != null) {
                listSelectionModel2.removeListSelectionListener(this);
            }
            this.selectionModel = listSelectionModel;
            listSelectionModel.addListSelectionListener(this);
        }
    }

    @Override // javax.swing.table.TableColumnModel
    public ListSelectionModel getSelectionModel() {
        return this.selectionModel;
    }

    @Override // javax.swing.table.TableColumnModel
    public void setColumnSelectionAllowed(boolean z2) {
        this.columnSelectionAllowed = z2;
    }

    @Override // javax.swing.table.TableColumnModel
    public boolean getColumnSelectionAllowed() {
        return this.columnSelectionAllowed;
    }

    @Override // javax.swing.table.TableColumnModel
    public int[] getSelectedColumns() {
        if (this.selectionModel != null) {
            int minSelectionIndex = this.selectionModel.getMinSelectionIndex();
            int maxSelectionIndex = this.selectionModel.getMaxSelectionIndex();
            if (minSelectionIndex == -1 || maxSelectionIndex == -1) {
                return new int[0];
            }
            int[] iArr = new int[1 + (maxSelectionIndex - minSelectionIndex)];
            int i2 = 0;
            for (int i3 = minSelectionIndex; i3 <= maxSelectionIndex; i3++) {
                if (this.selectionModel.isSelectedIndex(i3)) {
                    int i4 = i2;
                    i2++;
                    iArr[i4] = i3;
                }
            }
            int[] iArr2 = new int[i2];
            System.arraycopy(iArr, 0, iArr2, 0, i2);
            return iArr2;
        }
        return new int[0];
    }

    @Override // javax.swing.table.TableColumnModel
    public int getSelectedColumnCount() {
        if (this.selectionModel != null) {
            int minSelectionIndex = this.selectionModel.getMinSelectionIndex();
            int maxSelectionIndex = this.selectionModel.getMaxSelectionIndex();
            int i2 = 0;
            for (int i3 = minSelectionIndex; i3 <= maxSelectionIndex; i3++) {
                if (this.selectionModel.isSelectedIndex(i3)) {
                    i2++;
                }
            }
            return i2;
        }
        return 0;
    }

    @Override // javax.swing.table.TableColumnModel
    public void addColumnModelListener(TableColumnModelListener tableColumnModelListener) {
        this.listenerList.add(TableColumnModelListener.class, tableColumnModelListener);
    }

    @Override // javax.swing.table.TableColumnModel
    public void removeColumnModelListener(TableColumnModelListener tableColumnModelListener) {
        this.listenerList.remove(TableColumnModelListener.class, tableColumnModelListener);
    }

    public TableColumnModelListener[] getColumnModelListeners() {
        return (TableColumnModelListener[]) this.listenerList.getListeners(TableColumnModelListener.class);
    }

    protected void fireColumnAdded(TableColumnModelEvent tableColumnModelEvent) {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == TableColumnModelListener.class) {
                ((TableColumnModelListener) listenerList[length + 1]).columnAdded(tableColumnModelEvent);
            }
        }
    }

    protected void fireColumnRemoved(TableColumnModelEvent tableColumnModelEvent) {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == TableColumnModelListener.class) {
                ((TableColumnModelListener) listenerList[length + 1]).columnRemoved(tableColumnModelEvent);
            }
        }
    }

    protected void fireColumnMoved(TableColumnModelEvent tableColumnModelEvent) {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == TableColumnModelListener.class) {
                ((TableColumnModelListener) listenerList[length + 1]).columnMoved(tableColumnModelEvent);
            }
        }
    }

    protected void fireColumnSelectionChanged(ListSelectionEvent listSelectionEvent) {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == TableColumnModelListener.class) {
                ((TableColumnModelListener) listenerList[length + 1]).columnSelectionChanged(listSelectionEvent);
            }
        }
    }

    protected void fireColumnMarginChanged() {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == TableColumnModelListener.class) {
                if (this.changeEvent == null) {
                    this.changeEvent = new ChangeEvent(this);
                }
                ((TableColumnModelListener) listenerList[length + 1]).columnMarginChanged(this.changeEvent);
            }
        }
    }

    public <T extends EventListener> T[] getListeners(Class<T> cls) {
        return (T[]) this.listenerList.getListeners(cls);
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        String propertyName = propertyChangeEvent.getPropertyName();
        if (propertyName == MetadataParser.WIDTH_TAG_NAME || propertyName == "preferredWidth") {
            invalidateWidthCache();
            fireColumnMarginChanged();
        }
    }

    @Override // javax.swing.event.ListSelectionListener
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        fireColumnSelectionChanged(listSelectionEvent);
    }

    protected ListSelectionModel createSelectionModel() {
        return new DefaultListSelectionModel();
    }

    protected void recalcWidthCache() {
        Enumeration<TableColumn> columns = getColumns();
        this.totalColumnWidth = 0;
        while (columns.hasMoreElements()) {
            this.totalColumnWidth += columns.nextElement2().getWidth();
        }
    }

    private void invalidateWidthCache() {
        this.totalColumnWidth = -1;
    }
}
