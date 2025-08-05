package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.TreeTableCellBehavior;
import java.util.Map;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/TreeTableCellSkin.class */
public class TreeTableCellSkin<S, T> extends TableCellSkinBase<TreeTableCell<S, T>, TreeTableCellBehavior<S, T>> {
    private final TreeTableCell<S, T> treeTableCell;
    private final TreeTableColumn<S, T> tableColumn;

    public TreeTableCellSkin(TreeTableCell<S, T> treeTableCell) {
        super(treeTableCell, new TreeTableCellBehavior(treeTableCell));
        this.treeTableCell = treeTableCell;
        this.tableColumn = treeTableCell.getTableColumn();
        super.init(treeTableCell);
    }

    @Override // com.sun.javafx.scene.control.skin.TableCellSkinBase
    protected BooleanProperty columnVisibleProperty() {
        return this.tableColumn.visibleProperty();
    }

    @Override // com.sun.javafx.scene.control.skin.TableCellSkinBase
    protected ReadOnlyDoubleProperty columnWidthProperty() {
        return this.tableColumn.widthProperty();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase
    protected double leftLabelPadding() {
        TreeTableView<S> treeTable;
        TreeItem<?> treeItem;
        double leftPadding = super.leftLabelPadding();
        double height = getCellSize();
        TreeTableCell<S, T> cell = (TreeTableCell) getSkinnable();
        TreeTableColumn<S, T> tableColumn = cell.getTableColumn();
        if (tableColumn != null && (treeTable = cell.getTreeTableView()) != null) {
            int columnIndex = treeTable.getVisibleLeafIndex(tableColumn);
            TreeTableColumn<S, ?> treeColumn = treeTable.getTreeColumn();
            if ((treeColumn == null && columnIndex != 0) || (treeColumn != null && !tableColumn.equals(treeColumn))) {
                return leftPadding;
            }
            TreeTableRow<S> treeTableRow = cell.getTreeTableRow();
            if (treeTableRow != null && (treeItem = treeTableRow.getTreeItem()) != null) {
                int nodeLevel = treeTable.getTreeItemLevel(treeItem);
                if (!treeTable.isShowRoot()) {
                    nodeLevel--;
                }
                double indentPerLevel = 10.0d;
                if (treeTableRow.getSkin() instanceof TreeTableRowSkin) {
                    indentPerLevel = ((TreeTableRowSkin) treeTableRow.getSkin()).getIndentationPerLevel();
                }
                double leftPadding2 = leftPadding + (nodeLevel * indentPerLevel);
                Map<Control, Double> mdwp = TableRowSkinBase.maxDisclosureWidthMap;
                double leftPadding3 = leftPadding2 + (mdwp.containsKey(treeTable) ? mdwp.get(treeTable).doubleValue() : 0.0d);
                Node graphic = treeItem.getGraphic();
                return leftPadding3 + (graphic == null ? 0.0d : graphic.prefWidth(height));
            }
            return leftPadding;
        }
        return leftPadding;
    }

    @Override // com.sun.javafx.scene.control.skin.TableCellSkinBase, com.sun.javafx.scene.control.skin.LabeledSkinBase, javafx.scene.control.SkinBase
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        if (this.isDeferToParentForPrefWidth) {
            return super.computePrefWidth(height, topInset, rightInset, bottomInset, leftInset);
        }
        return columnWidthProperty().get();
    }
}
