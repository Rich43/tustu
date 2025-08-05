package com.sun.javafx.scene.control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TreeTableColumn;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/TableColumnComparatorBase.class */
public abstract class TableColumnComparatorBase<S, T> implements Comparator<S> {
    private final List<? extends TableColumnBase> columns;

    public abstract boolean isSortable(TableColumnBase<S, T> tableColumnBase);

    public abstract int doCompare(TableColumnBase<S, T> tableColumnBase, T t2, T t3);

    public TableColumnComparatorBase(TableColumnBase<S, T>... columns) {
        this((List<? extends TableColumnBase>) Arrays.asList(columns));
    }

    public TableColumnComparatorBase(List<? extends TableColumnBase> columns) {
        this.columns = new ArrayList(columns);
    }

    public List<? extends TableColumnBase> getColumns() {
        return Collections.unmodifiableList(this.columns);
    }

    @Override // java.util.Comparator
    public int compare(S o1, S o2) {
        for (TableColumnBase tc : this.columns) {
            if (isSortable(tc)) {
                T value1 = tc.getCellData((TableColumnBase) o1);
                T value2 = tc.getCellData((TableColumnBase) o2);
                int result = doCompare(tc, value1, value2);
                if (result != 0) {
                    return result;
                }
            }
        }
        return 0;
    }

    public int hashCode() {
        int hash = (59 * 7) + (this.columns != null ? this.columns.hashCode() : 0);
        return hash;
    }

    @Override // java.util.Comparator
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TableColumnComparatorBase other = (TableColumnComparatorBase) obj;
        if (this.columns == other.columns) {
            return true;
        }
        if (this.columns == null || !this.columns.equals(other.columns)) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "TableColumnComparatorBase [ columns: " + ((Object) getColumns()) + "] ";
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/TableColumnComparatorBase$TableColumnComparator.class */
    public static final class TableColumnComparator<S, T> extends TableColumnComparatorBase<S, T> {
        public TableColumnComparator(TableColumn<S, T>... columns) {
            super((List<? extends TableColumnBase>) Arrays.asList(columns));
        }

        public TableColumnComparator(List<TableColumn<S, T>> columns) {
            super(columns);
        }

        @Override // com.sun.javafx.scene.control.TableColumnComparatorBase
        public boolean isSortable(TableColumnBase<S, T> tcb) {
            TableColumn<S, T> tc = (TableColumn) tcb;
            return tc.getSortType() != null && tc.isSortable();
        }

        @Override // com.sun.javafx.scene.control.TableColumnComparatorBase
        public int doCompare(TableColumnBase<S, T> tcb, T value1, T value2) {
            TableColumn<S, T> tc = (TableColumn) tcb;
            Comparator<T> c2 = tc.getComparator();
            switch (tc.getSortType()) {
                case ASCENDING:
                    return c2.compare(value1, value2);
                case DESCENDING:
                    return c2.compare(value2, value1);
                default:
                    return 0;
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/TableColumnComparatorBase$TreeTableColumnComparator.class */
    public static final class TreeTableColumnComparator<S, T> extends TableColumnComparatorBase<S, T> {
        public TreeTableColumnComparator(TreeTableColumn<S, T>... columns) {
            super((List<? extends TableColumnBase>) Arrays.asList(columns));
        }

        public TreeTableColumnComparator(List<TreeTableColumn<S, T>> columns) {
            super(columns);
        }

        @Override // com.sun.javafx.scene.control.TableColumnComparatorBase
        public boolean isSortable(TableColumnBase<S, T> tcb) {
            TreeTableColumn<S, T> tc = (TreeTableColumn) tcb;
            return tc.getSortType() != null && tc.isSortable();
        }

        @Override // com.sun.javafx.scene.control.TableColumnComparatorBase
        public int doCompare(TableColumnBase<S, T> tcb, T value1, T value2) {
            TreeTableColumn<S, T> tc = (TreeTableColumn) tcb;
            Comparator<T> c2 = tc.getComparator();
            switch (tc.getSortType()) {
                case ASCENDING:
                    return c2.compare(value1, value2);
                case DESCENDING:
                    return c2.compare(value2, value1);
                default:
                    return 0;
            }
        }
    }
}
