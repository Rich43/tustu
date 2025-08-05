package javax.swing.event;

import java.util.EventObject;
import javax.swing.table.TableColumnModel;

/* loaded from: rt.jar:javax/swing/event/TableColumnModelEvent.class */
public class TableColumnModelEvent extends EventObject {
    protected int fromIndex;
    protected int toIndex;

    public TableColumnModelEvent(TableColumnModel tableColumnModel, int i2, int i3) {
        super(tableColumnModel);
        this.fromIndex = i2;
        this.toIndex = i3;
    }

    public int getFromIndex() {
        return this.fromIndex;
    }

    public int getToIndex() {
        return this.toIndex;
    }
}
