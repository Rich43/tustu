package javax.swing.event;

import java.util.EventObject;
import javax.swing.table.TableModel;

/* loaded from: rt.jar:javax/swing/event/TableModelEvent.class */
public class TableModelEvent extends EventObject {
    public static final int INSERT = 1;
    public static final int UPDATE = 0;
    public static final int DELETE = -1;
    public static final int HEADER_ROW = -1;
    public static final int ALL_COLUMNS = -1;
    protected int type;
    protected int firstRow;
    protected int lastRow;
    protected int column;

    public TableModelEvent(TableModel tableModel) {
        this(tableModel, 0, Integer.MAX_VALUE, -1, 0);
    }

    public TableModelEvent(TableModel tableModel, int i2) {
        this(tableModel, i2, i2, -1, 0);
    }

    public TableModelEvent(TableModel tableModel, int i2, int i3) {
        this(tableModel, i2, i3, -1, 0);
    }

    public TableModelEvent(TableModel tableModel, int i2, int i3, int i4) {
        this(tableModel, i2, i3, i4, 0);
    }

    public TableModelEvent(TableModel tableModel, int i2, int i3, int i4, int i5) {
        super(tableModel);
        this.firstRow = i2;
        this.lastRow = i3;
        this.column = i4;
        this.type = i5;
    }

    public int getFirstRow() {
        return this.firstRow;
    }

    public int getLastRow() {
        return this.lastRow;
    }

    public int getColumn() {
        return this.column;
    }

    public int getType() {
        return this.type;
    }
}
