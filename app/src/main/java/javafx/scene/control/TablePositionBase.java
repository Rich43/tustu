package javafx.scene.control;

import java.lang.ref.WeakReference;
import javafx.scene.control.TableColumnBase;

/* loaded from: jfxrt.jar:javafx/scene/control/TablePositionBase.class */
public abstract class TablePositionBase<TC extends TableColumnBase> {
    private final int row;
    private final WeakReference<TC> tableColumnRef;

    public abstract int getColumn();

    protected TablePositionBase(int row, TC tableColumn) {
        this.row = row;
        this.tableColumnRef = new WeakReference<>(tableColumn);
    }

    public int getRow() {
        return this.row;
    }

    public TC getTableColumn() {
        return this.tableColumnRef.get();
    }

    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TablePositionBase other = (TablePositionBase) obj;
        if (this.row != other.row) {
            return false;
        }
        TableColumnBase tableColumn = getTableColumn();
        TableColumnBase otherTableColumn = other.getTableColumn();
        if (tableColumn == otherTableColumn) {
            return true;
        }
        if (tableColumn == null || !tableColumn.equals(otherTableColumn)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = (79 * 5) + this.row;
        TableColumnBase tableColumn = getTableColumn();
        return (79 * hash) + (tableColumn != null ? tableColumn.hashCode() : 0);
    }
}
