package javax.swing.event;

import java.util.EventObject;
import javax.swing.RowSorter;

/* loaded from: rt.jar:javax/swing/event/RowSorterEvent.class */
public class RowSorterEvent extends EventObject {
    private Type type;
    private int[] oldViewToModel;

    /* loaded from: rt.jar:javax/swing/event/RowSorterEvent$Type.class */
    public enum Type {
        SORT_ORDER_CHANGED,
        SORTED
    }

    public RowSorterEvent(RowSorter rowSorter) {
        this(rowSorter, Type.SORT_ORDER_CHANGED, null);
    }

    public RowSorterEvent(RowSorter rowSorter, Type type, int[] iArr) {
        super(rowSorter);
        if (type == null) {
            throw new IllegalArgumentException("type must be non-null");
        }
        this.type = type;
        this.oldViewToModel = iArr;
    }

    @Override // java.util.EventObject
    public RowSorter getSource() {
        return (RowSorter) super.getSource();
    }

    public Type getType() {
        return this.type;
    }

    public int convertPreviousRowIndexToModel(int i2) {
        if (this.oldViewToModel != null && i2 >= 0 && i2 < this.oldViewToModel.length) {
            return this.oldViewToModel[i2];
        }
        return -1;
    }

    public int getPreviousRowCount() {
        if (this.oldViewToModel == null) {
            return 0;
        }
        return this.oldViewToModel.length;
    }
}
