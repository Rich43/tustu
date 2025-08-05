package com.sun.javafx.scene.control.skin;

import com.sun.javafx.collections.NonIterableChange;
import com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList;
import com.sun.javafx.scene.control.behavior.TreeTableViewBehavior;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.event.WeakEventHandler;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.control.ResizeFeaturesBase;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTablePosition;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import org.slf4j.Logger;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/TreeTableViewSkin.class */
public class TreeTableViewSkin<S> extends TableViewSkinBase<S, TreeItem<S>, TreeTableView<S>, TreeTableViewBehavior<S>, TreeTableRow<S>, TreeTableColumn<S, ?>> {
    private TreeTableViewBackingList<S> tableBackingList;
    private ObjectProperty<ObservableList<TreeItem<S>>> tableBackingListProperty;
    private TreeTableView<S> treeTableView;
    private WeakReference<TreeItem<S>> weakRootRef;
    private EventHandler<TreeItem.TreeModificationEvent<S>> rootListener;
    private WeakEventHandler<TreeItem.TreeModificationEvent<S>> weakRootListener;

    /* JADX WARN: Multi-variable type inference failed */
    public TreeTableViewSkin(TreeTableView<S> treeTableView) {
        super(treeTableView, new TreeTableViewBehavior(treeTableView));
        this.rootListener = e2 -> {
            if (e2.wasAdded() && e2.wasRemoved() && e2.getAddedSize() == e2.getRemovedSize()) {
                this.rowCountDirty = true;
                ((TreeTableView) getSkinnable()).requestLayout();
            } else if (e2.getEventType().equals(TreeItem.valueChangedEvent())) {
                this.needCellsRebuilt = true;
                ((TreeTableView) getSkinnable()).requestLayout();
            } else {
                EventType<?> eventType = e2.getEventType();
                while (true) {
                    EventType<?> eventType2 = eventType;
                    if (eventType2 == null) {
                        break;
                    }
                    if (eventType2.equals(TreeItem.expandedItemCountChangeEvent())) {
                        this.rowCountDirty = true;
                        ((TreeTableView) getSkinnable()).requestLayout();
                        break;
                    }
                    eventType = eventType2.getSuperType();
                }
            }
            ((TreeTableView) getSkinnable()).edit(-1, null);
        };
        this.treeTableView = treeTableView;
        this.tableBackingList = new TreeTableViewBackingList<>(treeTableView);
        this.tableBackingListProperty = new SimpleObjectProperty(this.tableBackingList);
        this.flow.setFixedCellSize(treeTableView.getFixedCellSize());
        super.init(treeTableView);
        setRoot(((TreeTableView) getSkinnable()).getRoot());
        EventHandler<MouseEvent> ml = event -> {
            if (treeTableView.getEditingCell() != null) {
                treeTableView.edit(-1, null);
            }
            if (treeTableView.isFocusTraversable()) {
                treeTableView.requestFocus();
            }
        };
        this.flow.getVbar().addEventFilter(MouseEvent.MOUSE_PRESSED, ml);
        this.flow.getHbar().addEventFilter(MouseEvent.MOUSE_PRESSED, ml);
        TreeTableViewBehavior<S> behavior = (TreeTableViewBehavior) getBehavior();
        behavior.setOnFocusPreviousRow(() -> {
            onFocusPreviousCell();
        });
        behavior.setOnFocusNextRow(() -> {
            onFocusNextCell();
        });
        behavior.setOnMoveToFirstCell(() -> {
            onMoveToFirstCell();
        });
        behavior.setOnMoveToLastCell(() -> {
            onMoveToLastCell();
        });
        behavior.setOnScrollPageDown(isFocusDriven -> {
            return Integer.valueOf(onScrollPageDown(isFocusDriven.booleanValue()));
        });
        behavior.setOnScrollPageUp(isFocusDriven2 -> {
            return Integer.valueOf(onScrollPageUp(isFocusDriven2.booleanValue()));
        });
        behavior.setOnSelectPreviousRow(() -> {
            onSelectPreviousCell();
        });
        behavior.setOnSelectNextRow(() -> {
            onSelectNextCell();
        });
        behavior.setOnSelectLeftCell(() -> {
            onSelectLeftCell();
        });
        behavior.setOnSelectRightCell(() -> {
            onSelectRightCell();
        });
        registerChangeListener(treeTableView.rootProperty(), Logger.ROOT_LOGGER_NAME);
        registerChangeListener(treeTableView.showRootProperty(), "SHOW_ROOT");
        registerChangeListener(treeTableView.rowFactoryProperty(), "ROW_FACTORY");
        registerChangeListener(treeTableView.expandedItemCountProperty(), "TREE_ITEM_COUNT");
        registerChangeListener(treeTableView.fixedCellSizeProperty(), "FIXED_CELL_SIZE");
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.TableViewSkinBase, com.sun.javafx.scene.control.skin.BehaviorSkinBase
    protected void handleControlPropertyChanged(String p2) {
        super.handleControlPropertyChanged(p2);
        if (Logger.ROOT_LOGGER_NAME.equals(p2)) {
            ((TreeTableView) getSkinnable()).edit(-1, null);
            setRoot(((TreeTableView) getSkinnable()).getRoot());
            return;
        }
        if ("SHOW_ROOT".equals(p2)) {
            if (!((TreeTableView) getSkinnable()).isShowRoot() && getRoot() != null) {
                getRoot().setExpanded(true);
            }
            updateRowCount();
            return;
        }
        if ("ROW_FACTORY".equals(p2)) {
            this.flow.recreateCells();
        } else if ("TREE_ITEM_COUNT".equals(p2)) {
            this.rowCountDirty = true;
        } else if ("FIXED_CELL_SIZE".equals(p2)) {
            this.flow.setFixedCellSize(((TreeTableView) getSkinnable()).getFixedCellSize());
        }
    }

    private TreeItem<S> getRoot() {
        if (this.weakRootRef == null) {
            return null;
        }
        return this.weakRootRef.get();
    }

    private void setRoot(TreeItem<S> newRoot) {
        if (getRoot() != null && this.weakRootListener != null) {
            getRoot().removeEventHandler(TreeItem.treeNotificationEvent(), this.weakRootListener);
        }
        this.weakRootRef = new WeakReference<>(newRoot);
        if (getRoot() != null) {
            this.weakRootListener = new WeakEventHandler<>(this.rootListener);
            getRoot().addEventHandler(TreeItem.treeNotificationEvent(), this.weakRootListener);
        }
        updateRowCount();
    }

    @Override // com.sun.javafx.scene.control.skin.TableViewSkinBase
    protected ObservableList<TreeTableColumn<S, ?>> getVisibleLeafColumns() {
        return this.treeTableView.getVisibleLeafColumns();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.skin.TableViewSkinBase
    public int getVisibleLeafIndex(TreeTableColumn<S, ?> tc) {
        return this.treeTableView.getVisibleLeafIndex(tc);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.skin.TableViewSkinBase
    public TreeTableColumn<S, ?> getVisibleLeafColumn(int col) {
        return this.treeTableView.getVisibleLeafColumn(col);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.skin.TableViewSkinBase
    public TreeTableView.TreeTableViewFocusModel<S> getFocusModel() {
        return this.treeTableView.getFocusModel();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.skin.TableViewSkinBase
    public TreeTablePosition<S, ?> getFocusedCell() {
        return this.treeTableView.getFocusModel().getFocusedCell();
    }

    @Override // com.sun.javafx.scene.control.skin.TableViewSkinBase
    protected TableSelectionModel<TreeItem<S>> getSelectionModel() {
        return this.treeTableView.getSelectionModel();
    }

    @Override // com.sun.javafx.scene.control.skin.TableViewSkinBase
    protected ObjectProperty<Callback<TreeTableView<S>, TreeTableRow<S>>> rowFactoryProperty() {
        return this.treeTableView.rowFactoryProperty();
    }

    @Override // com.sun.javafx.scene.control.skin.TableViewSkinBase
    protected ObjectProperty<Node> placeholderProperty() {
        return this.treeTableView.placeholderProperty();
    }

    @Override // com.sun.javafx.scene.control.skin.TableViewSkinBase
    protected ObjectProperty<ObservableList<TreeItem<S>>> itemsProperty() {
        return this.tableBackingListProperty;
    }

    @Override // com.sun.javafx.scene.control.skin.TableViewSkinBase
    protected ObservableList<TreeTableColumn<S, ?>> getColumns() {
        return this.treeTableView.getColumns();
    }

    @Override // com.sun.javafx.scene.control.skin.TableViewSkinBase
    protected BooleanProperty tableMenuButtonVisibleProperty() {
        return this.treeTableView.tableMenuButtonVisibleProperty();
    }

    @Override // com.sun.javafx.scene.control.skin.TableViewSkinBase
    protected ObjectProperty<Callback<ResizeFeaturesBase, Boolean>> columnResizePolicyProperty() {
        return this.treeTableView.columnResizePolicyProperty();
    }

    @Override // com.sun.javafx.scene.control.skin.TableViewSkinBase
    protected ObservableList<TreeTableColumn<S, ?>> getSortOrder() {
        return this.treeTableView.getSortOrder();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.skin.TableViewSkinBase
    public boolean resizeColumn(TreeTableColumn<S, ?> tc, double delta) {
        return this.treeTableView.resizeColumn(tc, delta);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.skin.TableViewSkinBase
    public void edit(int index, TreeTableColumn<S, ?> column) {
        this.treeTableView.edit(index, column);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.skin.TableViewSkinBase
    public void resizeColumnToFitContent(TreeTableColumn<S, ?> tc, int maxRows) {
        Callback cellFactory;
        TreeTableCell<S, ?> cell;
        List<?> items = itemsProperty().get();
        if (items == null || items.isEmpty() || (cellFactory = tc.getCellFactory()) == null || (cell = cellFactory.call(tc)) == null) {
            return;
        }
        cell.getProperties().put("deferToParentPrefWidth", Boolean.TRUE);
        double padding = 10.0d;
        Node n2 = cell.getSkin() == null ? null : cell.getSkin().getNode();
        if (n2 instanceof Region) {
            Region r2 = (Region) n2;
            padding = r2.snappedLeftInset() + r2.snappedRightInset();
        }
        TreeTableRow<S> treeTableRow = new TreeTableRow<>();
        treeTableRow.updateTreeTableView(this.treeTableView);
        int rows = maxRows == -1 ? items.size() : Math.min(items.size(), maxRows);
        double maxWidth = 0.0d;
        for (int row = 0; row < rows; row++) {
            treeTableRow.updateIndex(row);
            treeTableRow.updateTreeItem(this.treeTableView.getTreeItem(row));
            cell.updateTreeTableColumn(tc);
            cell.updateTreeTableView(this.treeTableView);
            cell.updateTreeTableRow(treeTableRow);
            cell.updateIndex(row);
            if ((cell.getText() != null && !cell.getText().isEmpty()) || cell.getGraphic() != null) {
                getChildren().add(cell);
                cell.applyCss();
                double w2 = cell.prefWidth(-1.0d);
                maxWidth = Math.max(maxWidth, w2);
                getChildren().remove(cell);
            }
        }
        cell.updateIndex(-1);
        TableColumnHeader header = getTableHeaderRow().getColumnHeaderFor(tc);
        double headerTextWidth = Utils.computeTextWidth(header.label.getFont(), tc.getText(), -1.0d);
        Node graphic = header.label.getGraphic();
        double headerGraphicWidth = graphic == null ? 0.0d : graphic.prefWidth(-1.0d) + header.label.getGraphicTextGap();
        double headerWidth = headerTextWidth + headerGraphicWidth + 10.0d + header.snappedLeftInset() + header.snappedRightInset();
        double maxWidth2 = Math.max(maxWidth, headerWidth) + padding;
        if (this.treeTableView.getColumnResizePolicy() == TreeTableView.CONSTRAINED_RESIZE_POLICY && this.treeTableView.getWidth() > 0.0d) {
            if (maxWidth2 > tc.getMaxWidth()) {
                maxWidth2 = tc.getMaxWidth();
            }
            int size = tc.getColumns().size();
            if (size > 0) {
                resizeColumnToFitContent((TreeTableColumn) tc.getColumns().get(size - 1), maxRows);
                return;
            } else {
                resizeColumn((TreeTableColumn) tc, Math.round(maxWidth2 - tc.getWidth()));
                return;
            }
        }
        tc.impl_setWidth(maxWidth2);
    }

    @Override // com.sun.javafx.scene.control.skin.VirtualContainerBase
    public int getItemCount() {
        return this.treeTableView.getExpandedItemCount();
    }

    @Override // com.sun.javafx.scene.control.skin.VirtualContainerBase
    public TreeTableRow<S> createCell() {
        TreeTableRow<S> cell;
        if (this.treeTableView.getRowFactory() != null) {
            cell = this.treeTableView.getRowFactory().call(this.treeTableView);
        } else {
            cell = new TreeTableRow<>();
        }
        if (cell.getDisclosureNode() == null) {
            StackPane disclosureNode = new StackPane();
            disclosureNode.getStyleClass().setAll("tree-disclosure-node");
            disclosureNode.setMouseTransparent(true);
            StackPane disclosureNodeArrow = new StackPane();
            disclosureNodeArrow.getStyleClass().setAll("arrow");
            disclosureNode.getChildren().add(disclosureNodeArrow);
            cell.setDisclosureNode(disclosureNode);
        }
        cell.updateTreeTableView(this.treeTableView);
        return cell;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.TableViewSkinBase
    protected void horizontalScroll() {
        super.horizontalScroll();
        if (((TreeTableView) getSkinnable()).getFixedCellSize() > 0.0d) {
            this.flow.requestCellLayout();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.TableViewSkinBase, javafx.scene.control.SkinBase
    protected Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case ROW_AT_INDEX:
                int rowIndex = ((Integer) parameters[0]).intValue();
                if (rowIndex < 0) {
                    return null;
                }
                return this.flow.getPrivateCell(rowIndex);
            case SELECTED_ITEMS:
                List<Node> selection = new ArrayList<>();
                TreeTableView.TreeTableViewSelectionModel<S> sm = ((TreeTableView) getSkinnable()).getSelectionModel();
                for (TreeTablePosition<S, ?> pos : sm.getSelectedCells()) {
                    TreeTableRow<S> row = (TreeTableRow) this.flow.getPrivateCell(pos.getRow());
                    if (row != null) {
                        selection.add(row);
                    }
                }
                return FXCollections.observableArrayList(selection);
            case FOCUS_ITEM:
            case CELL_AT_ROW_COLUMN:
            case COLUMN_AT_INDEX:
            case HEADER:
            case VERTICAL_SCROLLBAR:
            case HORIZONTAL_SCROLLBAR:
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected void executeAccessibleAction(AccessibleAction action, Object... parameters) {
        TreeTableView.TreeTableViewSelectionModel<S> sm;
        switch (action) {
            case SHOW_ITEM:
                Node item = (Node) parameters[0];
                if (item instanceof TreeTableCell) {
                    this.flow.show(((TreeTableCell) item).getIndex());
                    break;
                }
                break;
            case SET_SELECTED_ITEMS:
                ObservableList<Node> items = (ObservableList) parameters[0];
                if (items != null && (sm = ((TreeTableView) getSkinnable()).getSelectionModel()) != null) {
                    sm.clearSelection();
                    for (Node item2 : items) {
                        if (item2 instanceof TreeTableCell) {
                            TreeTableCell<S, ?> cell = (TreeTableCell) item2;
                            sm.select(cell.getIndex(), cell.getTableColumn());
                        }
                    }
                    break;
                }
                break;
            default:
                super.executeAccessibleAction(action, parameters);
                break;
        }
    }

    @Override // com.sun.javafx.scene.control.skin.TableViewSkinBase, com.sun.javafx.scene.control.skin.VirtualContainerBase
    protected void updateRowCount() {
        updatePlaceholderRegionVisibility();
        this.tableBackingList.resetSize();
        int oldCount = this.flow.getCellCount();
        int newCount = getItemCount();
        this.flow.setCellCount(newCount);
        if (this.forceCellRecreate) {
            this.needCellsRecreated = true;
            this.forceCellRecreate = false;
        } else if (newCount != oldCount) {
            this.needCellsRebuilt = true;
        } else {
            this.needCellsReconfigured = true;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/TreeTableViewSkin$TreeTableViewBackingList.class */
    private static class TreeTableViewBackingList<S> extends ReadOnlyUnbackedObservableList<TreeItem<S>> {
        private final TreeTableView<S> treeTable;
        private int size = -1;

        TreeTableViewBackingList(TreeTableView<S> treeTable) {
            this.treeTable = treeTable;
        }

        void resetSize() {
            int oldSize = this.size;
            this.size = -1;
            callObservers(new NonIterableChange.GenericAddRemoveChange(0, oldSize, FXCollections.emptyObservableList(), this));
        }

        @Override // com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList, java.util.List
        public TreeItem<S> get(int i2) {
            return this.treeTable.getTreeItem(i2);
        }

        @Override // com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList, java.util.List, java.util.Collection, java.util.Set
        public int size() {
            if (this.size == -1) {
                this.size = this.treeTable.getExpandedItemCount();
            }
            return this.size;
        }
    }
}
