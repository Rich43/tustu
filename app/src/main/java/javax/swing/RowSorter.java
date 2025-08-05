package javax.swing;

import java.util.List;
import javax.swing.event.EventListenerList;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;

/* loaded from: rt.jar:javax/swing/RowSorter.class */
public abstract class RowSorter<M> {
    private EventListenerList listenerList = new EventListenerList();

    public abstract M getModel();

    public abstract void toggleSortOrder(int i2);

    public abstract int convertRowIndexToModel(int i2);

    public abstract int convertRowIndexToView(int i2);

    public abstract void setSortKeys(List<? extends SortKey> list);

    public abstract List<? extends SortKey> getSortKeys();

    public abstract int getViewRowCount();

    public abstract int getModelRowCount();

    public abstract void modelStructureChanged();

    public abstract void allRowsChanged();

    public abstract void rowsInserted(int i2, int i3);

    public abstract void rowsDeleted(int i2, int i3);

    public abstract void rowsUpdated(int i2, int i3);

    public abstract void rowsUpdated(int i2, int i3, int i4);

    public void addRowSorterListener(RowSorterListener rowSorterListener) {
        this.listenerList.add(RowSorterListener.class, rowSorterListener);
    }

    public void removeRowSorterListener(RowSorterListener rowSorterListener) {
        this.listenerList.remove(RowSorterListener.class, rowSorterListener);
    }

    protected void fireSortOrderChanged() {
        fireRowSorterChanged(new RowSorterEvent(this));
    }

    protected void fireRowSorterChanged(int[] iArr) {
        fireRowSorterChanged(new RowSorterEvent(this, RowSorterEvent.Type.SORTED, iArr));
    }

    void fireRowSorterChanged(RowSorterEvent rowSorterEvent) {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == RowSorterListener.class) {
                ((RowSorterListener) listenerList[length + 1]).sorterChanged(rowSorterEvent);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/RowSorter$SortKey.class */
    public static class SortKey {
        private int column;
        private SortOrder sortOrder;

        public SortKey(int i2, SortOrder sortOrder) {
            if (sortOrder == null) {
                throw new IllegalArgumentException("sort order must be non-null");
            }
            this.column = i2;
            this.sortOrder = sortOrder;
        }

        public final int getColumn() {
            return this.column;
        }

        public final SortOrder getSortOrder() {
            return this.sortOrder;
        }

        public int hashCode() {
            return (37 * ((37 * 17) + this.column)) + this.sortOrder.hashCode();
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            return (obj instanceof SortKey) && ((SortKey) obj).column == this.column && ((SortKey) obj).sortOrder == this.sortOrder;
        }
    }
}
