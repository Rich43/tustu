package com.sun.javafx.scene.control;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TreeTableColumn;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/TableColumnSortTypeWrapper.class */
public class TableColumnSortTypeWrapper {
    public static boolean isAscending(TableColumnBase<?, ?> column) {
        String sortTypeName = getSortTypeName(column);
        return "ASCENDING".equals(sortTypeName);
    }

    public static boolean isDescending(TableColumnBase<?, ?> column) {
        String sortTypeName = getSortTypeName(column);
        return "DESCENDING".equals(sortTypeName);
    }

    public static void setSortType(TableColumnBase<?, ?> column, TableColumn.SortType sortType) {
        if (column instanceof TableColumn) {
            ((TableColumn) column).setSortType(sortType);
            return;
        }
        if (column instanceof TreeTableColumn) {
            TreeTableColumn tc = (TreeTableColumn) column;
            if (sortType == TableColumn.SortType.ASCENDING) {
                tc.setSortType(TreeTableColumn.SortType.ASCENDING);
            } else if (sortType == TableColumn.SortType.DESCENDING) {
                tc.setSortType(TreeTableColumn.SortType.DESCENDING);
            } else if (sortType == null) {
                tc.setSortType(null);
            }
        }
    }

    public static String getSortTypeName(TableColumnBase<?, ?> column) {
        if (column instanceof TableColumn) {
            TableColumn tc = (TableColumn) column;
            TableColumn.SortType st = tc.getSortType();
            if (st == null) {
                return null;
            }
            return st.name();
        }
        if (column instanceof TreeTableColumn) {
            TreeTableColumn tc2 = (TreeTableColumn) column;
            TreeTableColumn.SortType st2 = tc2.getSortType();
            if (st2 == null) {
                return null;
            }
            return st2.name();
        }
        return null;
    }

    public static ObservableValue getSortTypeProperty(TableColumnBase<?, ?> column) {
        if (column instanceof TableColumn) {
            return ((TableColumn) column).sortTypeProperty();
        }
        if (column instanceof TreeTableColumn) {
            return ((TreeTableColumn) column).sortTypeProperty();
        }
        return null;
    }
}
