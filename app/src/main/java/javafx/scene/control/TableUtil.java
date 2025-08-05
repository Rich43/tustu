package javafx.scene.control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TreeTableColumn;

/* loaded from: jfxrt.jar:javafx/scene/control/TableUtil.class */
class TableUtil {

    /* loaded from: jfxrt.jar:javafx/scene/control/TableUtil$SortEventType.class */
    enum SortEventType {
        SORT_ORDER_CHANGE,
        COLUMN_SORT_TYPE_CHANGE,
        COLUMN_SORTABLE_CHANGE,
        COLUMN_COMPARATOR_CHANGE
    }

    private TableUtil() {
    }

    static void removeTableColumnListener(List<? extends TableColumnBase> list, InvalidationListener columnVisibleObserver, InvalidationListener columnSortableObserver, InvalidationListener columnSortTypeObserver, InvalidationListener columnComparatorObserver) {
        if (list == null) {
            return;
        }
        for (TableColumnBase col : list) {
            col.visibleProperty().removeListener(columnVisibleObserver);
            col.sortableProperty().removeListener(columnSortableObserver);
            col.comparatorProperty().removeListener(columnComparatorObserver);
            if (col instanceof TableColumn) {
                ((TableColumn) col).sortTypeProperty().removeListener(columnSortTypeObserver);
            } else if (col instanceof TreeTableColumn) {
                ((TreeTableColumn) col).sortTypeProperty().removeListener(columnSortTypeObserver);
            }
            removeTableColumnListener(col.getColumns(), columnVisibleObserver, columnSortableObserver, columnSortTypeObserver, columnComparatorObserver);
        }
    }

    static void addTableColumnListener(List<? extends TableColumnBase> list, InvalidationListener columnVisibleObserver, InvalidationListener columnSortableObserver, InvalidationListener columnSortTypeObserver, InvalidationListener columnComparatorObserver) {
        if (list == null) {
            return;
        }
        for (TableColumnBase col : list) {
            col.visibleProperty().addListener(columnVisibleObserver);
            col.sortableProperty().addListener(columnSortableObserver);
            col.comparatorProperty().addListener(columnComparatorObserver);
            if (col instanceof TableColumn) {
                ((TableColumn) col).sortTypeProperty().addListener(columnSortTypeObserver);
            } else if (col instanceof TreeTableColumn) {
                ((TreeTableColumn) col).sortTypeProperty().addListener(columnSortTypeObserver);
            }
            addTableColumnListener(col.getColumns(), columnVisibleObserver, columnSortableObserver, columnSortTypeObserver, columnComparatorObserver);
        }
    }

    static void removeColumnsListener(List<? extends TableColumnBase> list, ListChangeListener cl) {
        if (list == null) {
            return;
        }
        for (TableColumnBase col : list) {
            col.getColumns().removeListener(cl);
            removeColumnsListener(col.getColumns(), cl);
        }
    }

    static void addColumnsListener(List<? extends TableColumnBase> list, ListChangeListener cl) {
        if (list == null) {
            return;
        }
        for (TableColumnBase col : list) {
            col.getColumns().addListener(cl);
            addColumnsListener(col.getColumns(), cl);
        }
    }

    static void handleSortFailure(ObservableList<? extends TableColumnBase> sortOrder, SortEventType sortEventType, Object... supportInfo) {
        if (sortEventType == SortEventType.COLUMN_SORT_TYPE_CHANGE) {
            TableColumnBase changedColumn = (TableColumnBase) supportInfo[0];
            revertSortType(changedColumn);
            return;
        }
        if (sortEventType == SortEventType.SORT_ORDER_CHANGE) {
            ListChangeListener.Change change = (ListChangeListener.Change) supportInfo[0];
            List toRemove = new ArrayList();
            List toAdd = new ArrayList();
            while (change.next()) {
                if (change.wasAdded()) {
                    toRemove.addAll(change.getAddedSubList());
                }
                if (change.wasRemoved()) {
                    toAdd.addAll(change.getRemoved());
                }
            }
            sortOrder.removeAll(toRemove);
            sortOrder.addAll(toAdd);
            return;
        }
        if (sortEventType != SortEventType.COLUMN_SORTABLE_CHANGE && sortEventType == SortEventType.COLUMN_COMPARATOR_CHANGE) {
        }
    }

    private static void revertSortType(TableColumnBase changedColumn) {
        if (changedColumn instanceof TableColumn) {
            TableColumn tableColumn = (TableColumn) changedColumn;
            TableColumn.SortType sortType = tableColumn.getSortType();
            if (sortType == TableColumn.SortType.ASCENDING) {
                tableColumn.setSortType(null);
                return;
            } else if (sortType == TableColumn.SortType.DESCENDING) {
                tableColumn.setSortType(TableColumn.SortType.ASCENDING);
                return;
            } else {
                if (sortType == null) {
                    tableColumn.setSortType(TableColumn.SortType.DESCENDING);
                    return;
                }
                return;
            }
        }
        if (changedColumn instanceof TreeTableColumn) {
            TreeTableColumn tableColumn2 = (TreeTableColumn) changedColumn;
            TreeTableColumn.SortType sortType2 = tableColumn2.getSortType();
            if (sortType2 == TreeTableColumn.SortType.ASCENDING) {
                tableColumn2.setSortType(null);
            } else if (sortType2 == TreeTableColumn.SortType.DESCENDING) {
                tableColumn2.setSortType(TreeTableColumn.SortType.ASCENDING);
            } else if (sortType2 == null) {
                tableColumn2.setSortType(TreeTableColumn.SortType.DESCENDING);
            }
        }
    }

    static boolean constrainedResize(ResizeFeaturesBase prop, boolean isFirstRun, double tableWidth, List<? extends TableColumnBase<?, ?>> visibleLeafColumns) {
        TableColumnBase<?, ?> leafColumn;
        double dRound;
        TableColumnBase<?, ?> column = prop.getColumn();
        double delta = prop.getDelta().doubleValue();
        double totalLowerBound = 0.0d;
        double totalUpperBound = 0.0d;
        if (tableWidth == 0.0d) {
            return false;
        }
        double colWidth = 0.0d;
        Iterator<? extends TableColumnBase<?, ?>> it = visibleLeafColumns.iterator();
        while (it.hasNext()) {
            colWidth += it.next().getWidth();
        }
        if (Math.abs(colWidth - tableWidth) > 1.0d) {
            boolean z2 = colWidth > tableWidth;
            double target = tableWidth;
            if (isFirstRun) {
                for (TableColumnBase<?, ?> col : visibleLeafColumns) {
                    totalLowerBound += col.getMinWidth();
                    totalUpperBound += col.getMaxWidth();
                }
                double totalUpperBound2 = totalUpperBound == Double.POSITIVE_INFINITY ? Double.MAX_VALUE : totalUpperBound == Double.NEGATIVE_INFINITY ? Double.MIN_VALUE : totalUpperBound;
                for (TableColumnBase col2 : visibleLeafColumns) {
                    double lowerBound = col2.getMinWidth();
                    double upperBound = col2.getMaxWidth();
                    if (Math.abs(totalLowerBound - totalUpperBound2) < 1.0E-7d) {
                        dRound = lowerBound;
                    } else {
                        double f2 = (target - totalLowerBound) / (totalUpperBound2 - totalLowerBound);
                        dRound = Math.round(lowerBound + (f2 * (upperBound - lowerBound)));
                    }
                    double newSize = dRound;
                    double remainder = resize(col2, newSize - col2.getWidth());
                    target -= newSize + remainder;
                    totalLowerBound -= lowerBound;
                    totalUpperBound2 -= upperBound;
                }
            } else {
                double actualDelta = tableWidth - colWidth;
                resizeColumns(visibleLeafColumns, actualDelta);
            }
        }
        if (column == null) {
            return false;
        }
        boolean isShrinking = delta < 0.0d;
        TableColumnBase<?, ?> tableColumnBase = column;
        while (true) {
            leafColumn = tableColumnBase;
            if (leafColumn.getColumns().size() <= 0) {
                break;
            }
            tableColumnBase = leafColumn.getColumns().get(leafColumn.getColumns().size() - 1);
        }
        int colPos = visibleLeafColumns.indexOf(leafColumn);
        int endColPos = visibleLeafColumns.size() - 1;
        double remainingDelta = delta;
        while (endColPos > colPos && remainingDelta != 0.0d) {
            TableColumnBase<?, ?> resizingCol = visibleLeafColumns.get(endColPos);
            endColPos--;
            if (resizingCol.isResizable()) {
                TableColumnBase<?, ?> shrinkingCol = isShrinking ? leafColumn : resizingCol;
                TableColumnBase<?, ?> growingCol = !isShrinking ? leafColumn : resizingCol;
                if (growingCol.getWidth() > growingCol.getPrefWidth()) {
                    List<? extends TableColumnBase> seq = visibleLeafColumns.subList(colPos + 1, endColPos + 1);
                    int i2 = seq.size() - 1;
                    while (true) {
                        if (i2 < 0) {
                            break;
                        }
                        TableColumnBase<?, ?> c2 = seq.get(i2);
                        if (c2.getWidth() < c2.getPrefWidth()) {
                            growingCol = c2;
                            break;
                        }
                        i2--;
                    }
                }
                double sdiff = Math.min(Math.abs(remainingDelta), shrinkingCol.getWidth() - shrinkingCol.getMinWidth());
                resize(shrinkingCol, -sdiff);
                resize(growingCol, sdiff);
                remainingDelta += isShrinking ? sdiff : -sdiff;
            }
        }
        return remainingDelta == 0.0d;
    }

    static double resize(TableColumnBase column, double delta) {
        if (delta == 0.0d) {
            return 0.0d;
        }
        if (!column.isResizable()) {
            return delta;
        }
        boolean isShrinking = delta < 0.0d;
        List<TableColumnBase<?, ?>> resizingChildren = getResizableChildren(column, isShrinking);
        if (resizingChildren.size() > 0) {
            return resizeColumns(resizingChildren, delta);
        }
        double newWidth = column.getWidth() + delta;
        if (newWidth > column.getMaxWidth()) {
            column.impl_setWidth(column.getMaxWidth());
            return newWidth - column.getMaxWidth();
        }
        if (newWidth < column.getMinWidth()) {
            column.impl_setWidth(column.getMinWidth());
            return newWidth - column.getMinWidth();
        }
        column.impl_setWidth(newWidth);
        return 0.0d;
    }

    private static List<TableColumnBase<?, ?>> getResizableChildren(TableColumnBase<?, ?> column, boolean isShrinking) {
        if (column == null || column.getColumns().isEmpty()) {
            return Collections.emptyList();
        }
        List<TableColumnBase<?, ?>> tablecolumns = new ArrayList<>();
        for (TableColumnBase c2 : column.getColumns()) {
            if (c2.isVisible() && c2.isResizable()) {
                if (isShrinking && c2.getWidth() > c2.getMinWidth()) {
                    tablecolumns.add(c2);
                } else if (!isShrinking && c2.getWidth() < c2.getMaxWidth()) {
                    tablecolumns.add(c2);
                }
            }
        }
        return tablecolumns;
    }

    private static double resizeColumns(List<? extends TableColumnBase<?, ?>> columns, double delta) {
        int columnCount = columns.size();
        double colDelta = delta / columnCount;
        double remainingDelta = delta;
        int col = 0;
        boolean isClean = true;
        for (TableColumnBase<?, ?> childCol : columns) {
            col++;
            double leftOverDelta = resize(childCol, colDelta);
            remainingDelta = (remainingDelta - colDelta) + leftOverDelta;
            if (leftOverDelta != 0.0d) {
                isClean = false;
                colDelta = remainingDelta / (columnCount - col);
            }
        }
        if (isClean) {
            return 0.0d;
        }
        return remainingDelta;
    }
}
