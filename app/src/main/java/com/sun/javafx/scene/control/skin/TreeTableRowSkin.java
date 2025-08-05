package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.MultiplePropertyChangeListenerHandler;
import com.sun.javafx.scene.control.behavior.TreeTableRowBehavior;
import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTablePosition;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/TreeTableRowSkin.class */
public class TreeTableRowSkin<T> extends TableRowSkinBase<TreeItem<T>, TreeTableRow<T>, TreeTableRowBehavior<T>, TreeTableCell<T, ?>> {
    private SimpleObjectProperty<ObservableList<TreeItem<T>>> itemsProperty;
    private TreeItem<?> treeItem;
    private boolean disclosureNodeDirty;
    private Node graphic;
    private TreeTableViewSkin treeTableViewSkin;
    private boolean childrenDirty;
    private MultiplePropertyChangeListenerHandler treeItemListener;
    private DoubleProperty indent;

    public TreeTableRowSkin(TreeTableRow<T> control) {
        super(control, new TreeTableRowBehavior(control));
        this.disclosureNodeDirty = true;
        this.childrenDirty = false;
        this.treeItemListener = new MultiplePropertyChangeListenerHandler(p2 -> {
            if ("GRAPHIC".equals(p2)) {
                this.disclosureNodeDirty = true;
                ((TreeTableRow) getSkinnable()).requestLayout();
                return null;
            }
            return null;
        });
        this.indent = null;
        super.init(control);
        updateTreeItem();
        updateTableViewSkin();
        registerChangeListener(control.treeTableViewProperty(), "TREE_TABLE_VIEW");
        registerChangeListener(control.indexProperty(), "INDEX");
        registerChangeListener(control.treeItemProperty(), "TREE_ITEM");
        registerChangeListener(control.getTreeTableView().treeColumnProperty(), "TREE_COLUMN");
    }

    public final void setIndent(double value) {
        indentProperty().set(value);
    }

    public final double getIndent() {
        if (this.indent == null) {
            return 10.0d;
        }
        return this.indent.get();
    }

    public final DoubleProperty indentProperty() {
        if (this.indent == null) {
            this.indent = new StyleableDoubleProperty(10.0d) { // from class: com.sun.javafx.scene.control.skin.TreeTableRowSkin.1
                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return TreeTableRowSkin.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "indent";
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.INDENT;
                }
            };
        }
        return this.indent;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.TableRowSkinBase, com.sun.javafx.scene.control.skin.LabeledSkinBase, com.sun.javafx.scene.control.skin.BehaviorSkinBase
    protected void handleControlPropertyChanged(String p2) {
        super.handleControlPropertyChanged(p2);
        if ("TREE_ABLE_VIEW".equals(p2)) {
            updateTableViewSkin();
            return;
        }
        if ("INDEX".equals(p2)) {
            this.updateCells = true;
            return;
        }
        if ("TREE_ITEM".equals(p2)) {
            updateTreeItem();
            this.isDirty = true;
        } else if ("TREE_COLUMN".equals(p2)) {
            this.isDirty = true;
            ((TreeTableRow) getSkinnable()).requestLayout();
        }
    }

    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase
    protected void updateChildren() {
        super.updateChildren();
        updateDisclosureNodeAndGraphic();
        if (this.childrenDirty) {
            this.childrenDirty = false;
            if (this.cells.isEmpty()) {
                getChildren().clear();
            } else {
                getChildren().addAll(this.cells);
            }
        }
    }

    @Override // com.sun.javafx.scene.control.skin.TableRowSkinBase, com.sun.javafx.scene.control.skin.LabeledSkinBase, javafx.scene.control.SkinBase
    protected void layoutChildren(double x2, double y2, double w2, double h2) {
        if (this.disclosureNodeDirty) {
            updateDisclosureNodeAndGraphic();
            this.disclosureNodeDirty = false;
        }
        Node disclosureNode = getDisclosureNode();
        if (disclosureNode != null && disclosureNode.getScene() == null) {
            updateDisclosureNodeAndGraphic();
        }
        super.layoutChildren(x2, y2, w2, h2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.skin.TableRowSkinBase
    public TreeTableCell<T, ?> getCell(TableColumnBase tcb) {
        TreeTableColumn tableColumn = (TreeTableColumn) tcb;
        TreeTableCell cell = tableColumn.getCellFactory().call(tableColumn);
        cell.updateTreeTableColumn(tableColumn);
        cell.updateTreeTableView(tableColumn.getTreeTableView());
        return cell;
    }

    @Override // com.sun.javafx.scene.control.skin.TableRowSkinBase
    protected void updateCells(boolean resetChildren) {
        super.updateCells(resetChildren);
        if (resetChildren) {
            this.childrenDirty = true;
            updateChildren();
        }
    }

    @Override // com.sun.javafx.scene.control.skin.TableRowSkinBase
    protected boolean isIndentationRequired() {
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.TableRowSkinBase
    protected TableColumnBase getTreeColumn() {
        return ((TreeTableRow) getSkinnable()).getTreeTableView().getTreeColumn();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.skin.TableRowSkinBase
    public int getIndentationLevel(TreeTableRow<T> control) {
        return control.getTreeTableView().getTreeItemLevel(control.getTreeItem());
    }

    @Override // com.sun.javafx.scene.control.skin.TableRowSkinBase
    protected double getIndentationPerLevel() {
        return getIndent();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.TableRowSkinBase
    protected Node getDisclosureNode() {
        return ((TreeTableRow) getSkinnable()).getDisclosureNode();
    }

    @Override // com.sun.javafx.scene.control.skin.TableRowSkinBase
    protected boolean isDisclosureNodeVisible() {
        return (getDisclosureNode() == null || this.treeItem == null || this.treeItem.isLeaf()) ? false : true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.TableRowSkinBase
    protected boolean isShowRoot() {
        return ((TreeTableRow) getSkinnable()).getTreeTableView().isShowRoot();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.TableRowSkinBase
    protected ObservableList<TreeTableColumn<T, ?>> getVisibleLeafColumns() {
        return ((TreeTableRow) getSkinnable()).getTreeTableView().getVisibleLeafColumns();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.skin.TableRowSkinBase
    public void updateCell(TreeTableCell<T, ?> cell, TreeTableRow<T> row) {
        cell.updateTreeTableRow(row);
    }

    @Override // com.sun.javafx.scene.control.skin.TableRowSkinBase
    protected boolean isColumnPartiallyOrFullyVisible(TableColumnBase tc) {
        if (this.treeTableViewSkin == null) {
            return false;
        }
        return this.treeTableViewSkin.isColumnPartiallyOrFullyVisible(tc);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.skin.TableRowSkinBase
    public TreeTableColumn<T, ?> getTableColumnBase(TreeTableCell cell) {
        return cell.getTableColumn();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.TableRowSkinBase
    protected ObjectProperty<Node> graphicProperty() {
        TreeTableRow<T> treeTableRow = (TreeTableRow) getSkinnable();
        if (treeTableRow == null || this.treeItem == null) {
            return null;
        }
        return this.treeItem.graphicProperty();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.TableRowSkinBase
    protected Control getVirtualFlowOwner() {
        return ((TreeTableRow) getSkinnable()).getTreeTableView();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.TableRowSkinBase
    protected DoubleProperty fixedCellSizeProperty() {
        return ((TreeTableRow) getSkinnable()).getTreeTableView().fixedCellSizeProperty();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void updateTreeItem() {
        if (this.treeItem != null) {
            this.treeItemListener.unregisterChangeListener(this.treeItem.expandedProperty());
            this.treeItemListener.unregisterChangeListener(this.treeItem.graphicProperty());
        }
        this.treeItem = ((TreeTableRow) getSkinnable()).getTreeItem();
        if (this.treeItem != null) {
            this.treeItemListener.registerChangeListener(this.treeItem.graphicProperty(), "GRAPHIC");
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void updateDisclosureNodeAndGraphic() {
        if (((TreeTableRow) getSkinnable()).isEmpty()) {
            return;
        }
        ObjectProperty<Node> graphicProperty = graphicProperty();
        Node newGraphic = graphicProperty == null ? null : graphicProperty.get();
        if (newGraphic != null) {
            if (newGraphic != this.graphic) {
                getChildren().remove(this.graphic);
            }
            if (!getChildren().contains(newGraphic)) {
                getChildren().add(newGraphic);
                this.graphic = newGraphic;
            }
        }
        Node disclosureNode = ((TreeTableRow) getSkinnable()).getDisclosureNode();
        if (disclosureNode != null) {
            boolean disclosureVisible = (this.treeItem == null || this.treeItem.isLeaf()) ? false : true;
            disclosureNode.setVisible(disclosureVisible);
            if (!disclosureVisible) {
                getChildren().remove(disclosureNode);
            } else if (disclosureNode.getParent() == null) {
                getChildren().add(disclosureNode);
                disclosureNode.toFront();
            } else {
                disclosureNode.toBack();
            }
            if (disclosureNode.getScene() != null) {
                disclosureNode.applyCss();
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void updateTableViewSkin() {
        TreeTableView<T> tableView = ((TreeTableRow) getSkinnable()).getTreeTableView();
        if (tableView.getSkin() instanceof TreeTableViewSkin) {
            this.treeTableViewSkin = (TreeTableViewSkin) tableView.getSkin();
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/TreeTableRowSkin$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<TreeTableRow<?>, Number> INDENT = new CssMetaData<TreeTableRow<?>, Number>("-fx-indent", SizeConverter.getInstance(), Double.valueOf(10.0d)) { // from class: com.sun.javafx.scene.control.skin.TreeTableRowSkin.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(TreeTableRow<?> n2) {
                DoubleProperty p2 = ((TreeTableRowSkin) n2.getSkin()).indentProperty();
                return p2 == null || !p2.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(TreeTableRow<?> n2) {
                TreeTableRowSkin<?> skin = (TreeTableRowSkin) n2.getSkin();
                return (StyleableProperty) skin.indentProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(CellSkinBase.getClassCssMetaData());
            styleables.add(INDENT);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override // com.sun.javafx.scene.control.skin.CellSkinBase, javafx.scene.control.SkinBase
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, javafx.scene.control.SkinBase
    protected Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        TreeTableView<T> treeTableView = ((TreeTableRow) getSkinnable()).getTreeTableView();
        switch (attribute) {
            case SELECTED_ITEMS:
                List<Node> selection = new ArrayList<>();
                int index = ((TreeTableRow) getSkinnable()).getIndex();
                Iterator<TreeTablePosition<T, ?>> it = treeTableView.getSelectionModel().getSelectedCells().iterator();
                if (it.hasNext()) {
                    TreeTablePosition<T, ?> pos = it.next();
                    if (pos.getRow() == index) {
                        Object column = pos.getTableColumn();
                        if (column == null) {
                            column = treeTableView.getVisibleLeafColumn(0);
                        }
                        TreeTableCell<T, ?> cell = (TreeTableCell) ((Reference) this.cellsMap.get(column)).get();
                        if (cell != null) {
                            selection.add(cell);
                        }
                    }
                    return FXCollections.observableArrayList(selection);
                }
                break;
            case CELL_AT_ROW_COLUMN:
                break;
            case FOCUS_ITEM:
                TreeTableView.TreeTableViewFocusModel<T> fm = treeTableView.getFocusModel();
                TreeTablePosition<T, ?> focusedCell = fm.getFocusedCell();
                Object column2 = focusedCell.getTableColumn();
                if (column2 == null) {
                    column2 = treeTableView.getVisibleLeafColumn(0);
                }
                if (this.cellsMap.containsKey(column2)) {
                    return ((Reference) this.cellsMap.get(column2)).get();
                }
                return null;
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
        int colIndex = ((Integer) parameters[1]).intValue();
        Object column3 = treeTableView.getVisibleLeafColumn(colIndex);
        if (this.cellsMap.containsKey(column3)) {
            return ((Reference) this.cellsMap.get(column3)).get();
        }
        return null;
    }
}
