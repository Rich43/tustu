package de.muntjak.tinylookandfeel.table;

import javax.swing.JTable;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/table/SortableTableData.class */
public interface SortableTableData {
    public static final int SORT_ASCENDING = 1;
    public static final int SORT_DESCENDING = 2;

    boolean isColumnSortable(int i2);

    boolean supportsMultiColumnSort();

    void sortColumns(int[] iArr, int[] iArr2, JTable jTable);
}
