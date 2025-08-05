package javax.swing.table;

import java.io.Serializable;
import java.util.Vector;
import javax.swing.event.TableModelEvent;

/* loaded from: rt.jar:javax/swing/table/DefaultTableModel.class */
public class DefaultTableModel extends AbstractTableModel implements Serializable {
    protected Vector dataVector;
    protected Vector columnIdentifiers;

    public DefaultTableModel() {
        this(0, 0);
    }

    private static Vector newVector(int i2) {
        Vector vector = new Vector(i2);
        vector.setSize(i2);
        return vector;
    }

    public DefaultTableModel(int i2, int i3) {
        this(newVector(i3), i2);
    }

    public DefaultTableModel(Vector vector, int i2) {
        setDataVector(newVector(i2), vector);
    }

    public DefaultTableModel(Object[] objArr, int i2) {
        this(convertToVector(objArr), i2);
    }

    public DefaultTableModel(Vector vector, Vector vector2) {
        setDataVector(vector, vector2);
    }

    public DefaultTableModel(Object[][] objArr, Object[] objArr2) {
        setDataVector(objArr, objArr2);
    }

    public Vector getDataVector() {
        return this.dataVector;
    }

    private static Vector nonNullVector(Vector vector) {
        return vector != null ? vector : new Vector();
    }

    public void setDataVector(Vector vector, Vector vector2) {
        this.dataVector = nonNullVector(vector);
        this.columnIdentifiers = nonNullVector(vector2);
        justifyRows(0, getRowCount());
        fireTableStructureChanged();
    }

    public void setDataVector(Object[][] objArr, Object[] objArr2) {
        setDataVector(convertToVector(objArr), convertToVector(objArr2));
    }

    public void newDataAvailable(TableModelEvent tableModelEvent) {
        fireTableChanged(tableModelEvent);
    }

    private void justifyRows(int i2, int i3) {
        this.dataVector.setSize(getRowCount());
        for (int i4 = i2; i4 < i3; i4++) {
            if (this.dataVector.elementAt(i4) == null) {
                this.dataVector.setElementAt(new Vector(), i4);
            }
            ((Vector) this.dataVector.elementAt(i4)).setSize(getColumnCount());
        }
    }

    public void newRowsAdded(TableModelEvent tableModelEvent) {
        justifyRows(tableModelEvent.getFirstRow(), tableModelEvent.getLastRow() + 1);
        fireTableChanged(tableModelEvent);
    }

    public void rowsRemoved(TableModelEvent tableModelEvent) {
        fireTableChanged(tableModelEvent);
    }

    public void setNumRows(int i2) {
        int rowCount = getRowCount();
        if (rowCount == i2) {
            return;
        }
        this.dataVector.setSize(i2);
        if (i2 <= rowCount) {
            fireTableRowsDeleted(i2, rowCount - 1);
        } else {
            justifyRows(rowCount, i2);
            fireTableRowsInserted(rowCount, i2 - 1);
        }
    }

    public void setRowCount(int i2) {
        setNumRows(i2);
    }

    public void addRow(Vector vector) {
        insertRow(getRowCount(), vector);
    }

    public void addRow(Object[] objArr) {
        addRow(convertToVector(objArr));
    }

    public void insertRow(int i2, Vector vector) {
        this.dataVector.insertElementAt(vector, i2);
        justifyRows(i2, i2 + 1);
        fireTableRowsInserted(i2, i2);
    }

    public void insertRow(int i2, Object[] objArr) {
        insertRow(i2, convertToVector(objArr));
    }

    private static int gcd(int i2, int i3) {
        return i3 == 0 ? i2 : gcd(i3, i2 % i3);
    }

    private static void rotate(Vector vector, int i2, int i3, int i4) {
        int i5 = i3 - i2;
        int i6 = i5 - i4;
        int iGcd = gcd(i5, i6);
        for (int i7 = 0; i7 < iGcd; i7++) {
            int i8 = i7;
            Object objElementAt = vector.elementAt(i2 + i8);
            while (true) {
                int i9 = (i8 + i6) % i5;
                if (i9 != i7) {
                    vector.setElementAt(vector.elementAt(i2 + i9), i2 + i8);
                    i8 = i9;
                }
            }
            vector.setElementAt(objElementAt, i2 + i8);
        }
    }

    public void moveRow(int i2, int i3, int i4) {
        int i5;
        int i6;
        int i7 = i4 - i2;
        if (i7 < 0) {
            i5 = i4;
            i6 = i3;
        } else {
            i5 = i2;
            i6 = (i4 + i3) - i2;
        }
        rotate(this.dataVector, i5, i6 + 1, i7);
        fireTableRowsUpdated(i5, i6);
    }

    public void removeRow(int i2) {
        this.dataVector.removeElementAt(i2);
        fireTableRowsDeleted(i2, i2);
    }

    public void setColumnIdentifiers(Vector vector) {
        setDataVector(this.dataVector, vector);
    }

    public void setColumnIdentifiers(Object[] objArr) {
        setColumnIdentifiers(convertToVector(objArr));
    }

    public void setColumnCount(int i2) {
        this.columnIdentifiers.setSize(i2);
        justifyRows(0, getRowCount());
        fireTableStructureChanged();
    }

    public void addColumn(Object obj) {
        addColumn(obj, (Vector) null);
    }

    public void addColumn(Object obj, Vector vector) {
        this.columnIdentifiers.addElement(obj);
        if (vector != null) {
            int size = vector.size();
            if (size > getRowCount()) {
                this.dataVector.setSize(size);
            }
            justifyRows(0, getRowCount());
            int columnCount = getColumnCount() - 1;
            for (int i2 = 0; i2 < size; i2++) {
                ((Vector) this.dataVector.elementAt(i2)).setElementAt(vector.elementAt(i2), columnCount);
            }
        } else {
            justifyRows(0, getRowCount());
        }
        fireTableStructureChanged();
    }

    public void addColumn(Object obj, Object[] objArr) {
        addColumn(obj, convertToVector(objArr));
    }

    @Override // javax.swing.table.TableModel
    public int getRowCount() {
        return this.dataVector.size();
    }

    @Override // javax.swing.table.TableModel
    public int getColumnCount() {
        return this.columnIdentifiers.size();
    }

    @Override // javax.swing.table.AbstractTableModel, javax.swing.table.TableModel
    public String getColumnName(int i2) {
        Object objElementAt = null;
        if (i2 < this.columnIdentifiers.size() && i2 >= 0) {
            objElementAt = this.columnIdentifiers.elementAt(i2);
        }
        return objElementAt == null ? super.getColumnName(i2) : objElementAt.toString();
    }

    @Override // javax.swing.table.AbstractTableModel, javax.swing.table.TableModel
    public boolean isCellEditable(int i2, int i3) {
        return true;
    }

    @Override // javax.swing.table.TableModel
    public Object getValueAt(int i2, int i3) {
        return ((Vector) this.dataVector.elementAt(i2)).elementAt(i3);
    }

    @Override // javax.swing.table.AbstractTableModel, javax.swing.table.TableModel
    public void setValueAt(Object obj, int i2, int i3) {
        ((Vector) this.dataVector.elementAt(i2)).setElementAt(obj, i3);
        fireTableCellUpdated(i2, i3);
    }

    protected static Vector convertToVector(Object[] objArr) {
        if (objArr == null) {
            return null;
        }
        Vector vector = new Vector(objArr.length);
        for (Object obj : objArr) {
            vector.addElement(obj);
        }
        return vector;
    }

    protected static Vector convertToVector(Object[][] objArr) {
        if (objArr == null) {
            return null;
        }
        Vector vector = new Vector(objArr.length);
        for (Object[] objArr2 : objArr) {
            vector.addElement(convertToVector(objArr2));
        }
        return vector;
    }
}
