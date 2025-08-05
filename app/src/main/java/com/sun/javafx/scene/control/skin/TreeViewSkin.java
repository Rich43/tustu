package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.TreeViewBehavior;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.event.WeakEventHandler;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.control.FocusModel;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/TreeViewSkin.class */
public class TreeViewSkin<T> extends VirtualContainerBase<TreeView<T>, TreeViewBehavior<T>, TreeCell<T>> {
    public static final String RECREATE = "treeRecreateKey";
    private static final boolean IS_PANNABLE = ((Boolean) AccessController.doPrivileged(() -> {
        return Boolean.valueOf(Boolean.getBoolean("com.sun.javafx.scene.control.skin.TreeViewSkin.pannable"));
    })).booleanValue();
    private boolean needCellsRebuilt;
    private boolean needCellsReconfigured;
    private MapChangeListener<Object, Object> propertiesMapListener;
    private EventHandler<TreeItem.TreeModificationEvent<T>> rootListener;
    private WeakEventHandler<TreeItem.TreeModificationEvent<T>> weakRootListener;
    private WeakReference<TreeItem<T>> weakRoot;

    /* JADX WARN: Multi-variable type inference failed */
    public TreeViewSkin(TreeView treeView) {
        super(treeView, new TreeViewBehavior(treeView));
        this.needCellsRebuilt = true;
        this.needCellsReconfigured = false;
        this.propertiesMapListener = c2 -> {
            if (c2.wasAdded() && RECREATE.equals(c2.getKey())) {
                this.needCellsRebuilt = true;
                ((TreeView) getSkinnable()).requestLayout();
                ((TreeView) getSkinnable()).getProperties().remove(RECREATE);
            }
        };
        this.rootListener = e2 -> {
            if (e2.wasAdded() && e2.wasRemoved() && e2.getAddedSize() == e2.getRemovedSize()) {
                this.rowCountDirty = true;
                ((TreeView) getSkinnable()).requestLayout();
            } else if (e2.getEventType().equals(TreeItem.valueChangedEvent())) {
                this.needCellsRebuilt = true;
                ((TreeView) getSkinnable()).requestLayout();
            } else {
                EventType<?> eventType = e2.getEventType();
                while (true) {
                    EventType<?> eventType2 = eventType;
                    if (eventType2 == null) {
                        break;
                    }
                    if (eventType2.equals(TreeItem.expandedItemCountChangeEvent())) {
                        this.rowCountDirty = true;
                        ((TreeView) getSkinnable()).requestLayout();
                        break;
                    }
                    eventType = eventType2.getSuperType();
                }
            }
            ((TreeView) getSkinnable()).edit(null);
        };
        this.flow.setPannable(IS_PANNABLE);
        this.flow.setCreateCell(flow1 -> {
            return createCell();
        });
        this.flow.setFixedCellSize(treeView.getFixedCellSize());
        getChildren().add(this.flow);
        setRoot(((TreeView) getSkinnable()).getRoot());
        EventHandler<MouseEvent> ml = event -> {
            if (treeView.getEditingItem() != null) {
                treeView.edit(null);
            }
            if (treeView.isFocusTraversable()) {
                treeView.requestFocus();
            }
        };
        this.flow.getVbar().addEventFilter(MouseEvent.MOUSE_PRESSED, ml);
        this.flow.getHbar().addEventFilter(MouseEvent.MOUSE_PRESSED, ml);
        ObservableMap<Object, Object> properties = treeView.getProperties();
        properties.remove(RECREATE);
        properties.addListener(this.propertiesMapListener);
        ((TreeViewBehavior) getBehavior()).setOnFocusPreviousRow(() -> {
            onFocusPreviousCell();
        });
        ((TreeViewBehavior) getBehavior()).setOnFocusNextRow(() -> {
            onFocusNextCell();
        });
        ((TreeViewBehavior) getBehavior()).setOnMoveToFirstCell(() -> {
            onMoveToFirstCell();
        });
        ((TreeViewBehavior) getBehavior()).setOnMoveToLastCell(() -> {
            onMoveToLastCell();
        });
        ((TreeViewBehavior) getBehavior()).setOnScrollPageDown(isFocusDriven -> {
            return Integer.valueOf(onScrollPageDown(isFocusDriven.booleanValue()));
        });
        ((TreeViewBehavior) getBehavior()).setOnScrollPageUp(isFocusDriven2 -> {
            return Integer.valueOf(onScrollPageUp(isFocusDriven2.booleanValue()));
        });
        ((TreeViewBehavior) getBehavior()).setOnSelectPreviousRow(() -> {
            onSelectPreviousCell();
        });
        ((TreeViewBehavior) getBehavior()).setOnSelectNextRow(() -> {
            onSelectNextCell();
        });
        registerChangeListener(treeView.rootProperty(), Logger.ROOT_LOGGER_NAME);
        registerChangeListener(treeView.showRootProperty(), "SHOW_ROOT");
        registerChangeListener(treeView.cellFactoryProperty(), "CELL_FACTORY");
        registerChangeListener(treeView.fixedCellSizeProperty(), "FIXED_CELL_SIZE");
        updateRowCount();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.BehaviorSkinBase
    protected void handleControlPropertyChanged(String p2) {
        super.handleControlPropertyChanged(p2);
        if (Logger.ROOT_LOGGER_NAME.equals(p2)) {
            setRoot(((TreeView) getSkinnable()).getRoot());
            return;
        }
        if ("SHOW_ROOT".equals(p2)) {
            if (!((TreeView) getSkinnable()).isShowRoot() && getRoot() != null) {
                getRoot().setExpanded(true);
            }
            updateRowCount();
            return;
        }
        if ("CELL_FACTORY".equals(p2)) {
            this.flow.recreateCells();
        } else if ("FIXED_CELL_SIZE".equals(p2)) {
            this.flow.setFixedCellSize(((TreeView) getSkinnable()).getFixedCellSize());
        }
    }

    private TreeItem<T> getRoot() {
        if (this.weakRoot == null) {
            return null;
        }
        return this.weakRoot.get();
    }

    private void setRoot(TreeItem<T> newRoot) {
        if (getRoot() != null && this.weakRootListener != null) {
            getRoot().removeEventHandler(TreeItem.treeNotificationEvent(), this.weakRootListener);
        }
        this.weakRoot = new WeakReference<>(newRoot);
        if (getRoot() != null) {
            this.weakRootListener = new WeakEventHandler<>(this.rootListener);
            getRoot().addEventHandler(TreeItem.treeNotificationEvent(), this.weakRootListener);
        }
        updateRowCount();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.VirtualContainerBase
    public int getItemCount() {
        return ((TreeView) getSkinnable()).getExpandedItemCount();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.VirtualContainerBase
    protected void updateRowCount() {
        int newCount = getItemCount();
        this.flow.setCellCount(newCount);
        this.needCellsRebuilt = true;
        ((TreeView) getSkinnable()).requestLayout();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.VirtualContainerBase
    public TreeCell<T> createCell() {
        TreeCell<T> cell;
        if (((TreeView) getSkinnable()).getCellFactory() != null) {
            cell = (TreeCell) ((TreeView) getSkinnable()).getCellFactory().call(getSkinnable());
        } else {
            cell = createDefaultCellImpl();
        }
        if (cell.getDisclosureNode() == null) {
            StackPane disclosureNode = new StackPane();
            disclosureNode.getStyleClass().setAll("tree-disclosure-node");
            StackPane disclosureNodeArrow = new StackPane();
            disclosureNodeArrow.getStyleClass().setAll("arrow");
            disclosureNode.getChildren().add(disclosureNodeArrow);
            cell.setDisclosureNode(disclosureNode);
        }
        cell.updateTreeView((TreeView) getSkinnable());
        return cell;
    }

    private TreeCell<T> createDefaultCellImpl() {
        return new TreeCell<T>() { // from class: com.sun.javafx.scene.control.skin.TreeViewSkin.1
            private HBox hbox;
            private WeakReference<TreeItem<T>> treeItemRef;
            private InvalidationListener treeItemGraphicListener = observable -> {
                updateDisplay(getItem(), isEmpty());
            };
            private InvalidationListener treeItemListener = new InvalidationListener() { // from class: com.sun.javafx.scene.control.skin.TreeViewSkin.1.1
                @Override // javafx.beans.InvalidationListener
                public void invalidated(Observable observable) {
                    TreeItem<T> treeItem = AnonymousClass1.this.treeItemRef == null ? null : (TreeItem) AnonymousClass1.this.treeItemRef.get();
                    if (treeItem != null) {
                        treeItem.graphicProperty().removeListener(AnonymousClass1.this.weakTreeItemGraphicListener);
                    }
                    TreeItem<T> treeItem2 = getTreeItem();
                    if (treeItem2 != null) {
                        treeItem2.graphicProperty().addListener(AnonymousClass1.this.weakTreeItemGraphicListener);
                        AnonymousClass1.this.treeItemRef = new WeakReference(treeItem2);
                    }
                }
            };
            private WeakInvalidationListener weakTreeItemGraphicListener = new WeakInvalidationListener(this.treeItemGraphicListener);
            private WeakInvalidationListener weakTreeItemListener = new WeakInvalidationListener(this.treeItemListener);

            {
                treeItemProperty().addListener(this.weakTreeItemListener);
                if (getTreeItem() != null) {
                    getTreeItem().graphicProperty().addListener(this.weakTreeItemGraphicListener);
                }
            }

            /* JADX WARN: Multi-variable type inference failed */
            private void updateDisplay(T t2, boolean empty) {
                if (t2 == 0 || empty) {
                    this.hbox = null;
                    setText(null);
                    setGraphic(null);
                    return;
                }
                TreeItem<T> treeItem = getTreeItem();
                Node graphic = treeItem == null ? null : treeItem.getGraphic();
                if (graphic != null) {
                    if (t2 instanceof Node) {
                        setText(null);
                        if (this.hbox == null) {
                            this.hbox = new HBox(3.0d);
                        }
                        this.hbox.getChildren().setAll(graphic, (Node) t2);
                        setGraphic(this.hbox);
                        return;
                    }
                    this.hbox = null;
                    setText(t2.toString());
                    setGraphic(graphic);
                    return;
                }
                this.hbox = null;
                if (t2 instanceof Node) {
                    setText(null);
                    setGraphic((Node) t2);
                } else {
                    setText(t2.toString());
                    setGraphic(null);
                }
            }

            @Override // javafx.scene.control.Cell
            public void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                updateDisplay(item, empty);
            }
        };
    }

    @Override // javafx.scene.control.SkinBase
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return computePrefHeight(-1.0d, topInset, rightInset, bottomInset, leftInset) * 0.618033987d;
    }

    @Override // javafx.scene.control.SkinBase
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return 400.0d;
    }

    @Override // com.sun.javafx.scene.control.skin.VirtualContainerBase, javafx.scene.control.SkinBase
    protected void layoutChildren(double x2, double y2, double w2, double h2) {
        super.layoutChildren(x2, y2, w2, h2);
        if (this.needCellsRebuilt) {
            this.flow.rebuildCells();
        } else if (this.needCellsReconfigured) {
            this.flow.reconfigureCells();
        }
        this.needCellsRebuilt = false;
        this.needCellsReconfigured = false;
        this.flow.resizeRelocate(x2, y2, w2, h2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void onFocusPreviousCell() {
        FocusModel<TreeItem<T>> fm = ((TreeView) getSkinnable()).getFocusModel();
        if (fm == null) {
            return;
        }
        this.flow.show(fm.getFocusedIndex());
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void onFocusNextCell() {
        FocusModel<TreeItem<T>> fm = ((TreeView) getSkinnable()).getFocusModel();
        if (fm == null) {
            return;
        }
        this.flow.show(fm.getFocusedIndex());
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void onSelectPreviousCell() {
        int row = ((TreeView) getSkinnable()).getSelectionModel().getSelectedIndex();
        this.flow.show(row);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void onSelectNextCell() {
        int row = ((TreeView) getSkinnable()).getSelectionModel().getSelectedIndex();
        this.flow.show(row);
    }

    private void onMoveToFirstCell() {
        this.flow.show(0);
        this.flow.setPosition(0.0d);
    }

    private void onMoveToLastCell() {
        this.flow.show(getItemCount());
        this.flow.setPosition(1.0d);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public int onScrollPageDown(boolean isFocusDriven) {
        boolean isSelected;
        TreeCell<T> lastVisibleCell = (TreeCell) this.flow.getLastVisibleCellWithinViewPort();
        if (lastVisibleCell == null) {
            return -1;
        }
        SelectionModel<TreeItem<T>> sm = ((TreeView) getSkinnable()).getSelectionModel();
        FocusModel<TreeItem<T>> fm = ((TreeView) getSkinnable()).getFocusModel();
        if (sm == null || fm == null) {
            return -1;
        }
        int lastVisibleCellIndex = lastVisibleCell.getIndex();
        if (isFocusDriven) {
            isSelected = lastVisibleCell.isFocused() || fm.isFocused(lastVisibleCellIndex);
        } else {
            isSelected = lastVisibleCell.isSelected() || sm.isSelected(lastVisibleCellIndex);
        }
        if (isSelected) {
            boolean isLeadIndex = (isFocusDriven && fm.getFocusedIndex() == lastVisibleCellIndex) || (!isFocusDriven && sm.getSelectedIndex() == lastVisibleCellIndex);
            if (isLeadIndex) {
                this.flow.showAsFirst(lastVisibleCell);
                TreeCell<T> newLastVisibleCell = (TreeCell) this.flow.getLastVisibleCellWithinViewPort();
                lastVisibleCell = newLastVisibleCell == null ? lastVisibleCell : newLastVisibleCell;
            }
        }
        int newSelectionIndex = lastVisibleCell.getIndex();
        this.flow.show((VirtualFlow<I>) lastVisibleCell);
        return newSelectionIndex;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public int onScrollPageUp(boolean isFocusDriven) {
        boolean isSelected;
        TreeCell<T> firstVisibleCell = (TreeCell) this.flow.getFirstVisibleCellWithinViewPort();
        if (firstVisibleCell == null) {
            return -1;
        }
        SelectionModel<TreeItem<T>> sm = ((TreeView) getSkinnable()).getSelectionModel();
        FocusModel<TreeItem<T>> fm = ((TreeView) getSkinnable()).getFocusModel();
        if (sm == null || fm == null) {
            return -1;
        }
        int firstVisibleCellIndex = firstVisibleCell.getIndex();
        if (isFocusDriven) {
            isSelected = firstVisibleCell.isFocused() || fm.isFocused(firstVisibleCellIndex);
        } else {
            isSelected = firstVisibleCell.isSelected() || sm.isSelected(firstVisibleCellIndex);
        }
        if (isSelected) {
            boolean isLeadIndex = (isFocusDriven && fm.getFocusedIndex() == firstVisibleCellIndex) || (!isFocusDriven && sm.getSelectedIndex() == firstVisibleCellIndex);
            if (isLeadIndex) {
                this.flow.showAsLast(firstVisibleCell);
                TreeCell<T> newFirstVisibleCell = (TreeCell) this.flow.getFirstVisibleCellWithinViewPort();
                firstVisibleCell = newFirstVisibleCell == null ? firstVisibleCell : newFirstVisibleCell;
            }
        }
        int newSelectionIndex = firstVisibleCell.getIndex();
        this.flow.show((VirtualFlow<I>) firstVisibleCell);
        return newSelectionIndex;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case FOCUS_ITEM:
                FocusModel<?> fm = ((TreeView) getSkinnable()).getFocusModel();
                int focusedIndex = fm.getFocusedIndex();
                if (focusedIndex == -1) {
                    if (getItemCount() > 0) {
                        focusedIndex = 0;
                    } else {
                        return null;
                    }
                }
                return this.flow.getPrivateCell(focusedIndex);
            case ROW_AT_INDEX:
                int rowIndex = ((Integer) parameters[0]).intValue();
                if (rowIndex < 0) {
                    return null;
                }
                return this.flow.getPrivateCell(rowIndex);
            case SELECTED_ITEMS:
                MultipleSelectionModel<TreeItem<T>> sm = ((TreeView) getSkinnable()).getSelectionModel();
                ObservableList<Integer> indices = sm.getSelectedIndices();
                List<Node> selection = new ArrayList<>(indices.size());
                Iterator<Integer> it = indices.iterator();
                while (it.hasNext()) {
                    int i2 = it.next().intValue();
                    TreeCell<T> row = (TreeCell) this.flow.getPrivateCell(i2);
                    if (row != null) {
                        selection.add(row);
                    }
                }
                return FXCollections.observableArrayList(selection);
            case VERTICAL_SCROLLBAR:
                return this.flow.getVbar();
            case HORIZONTAL_SCROLLBAR:
                return this.flow.getHbar();
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected void executeAccessibleAction(AccessibleAction action, Object... parameters) {
        MultipleSelectionModel<TreeItem<T>> sm;
        switch (action) {
            case SHOW_ITEM:
                Node item = (Node) parameters[0];
                if (item instanceof TreeCell) {
                    TreeCell<T> cell = (TreeCell) item;
                    this.flow.show(cell.getIndex());
                    break;
                }
                break;
            case SET_SELECTED_ITEMS:
                ObservableList<Node> items = (ObservableList) parameters[0];
                if (items != null && (sm = ((TreeView) getSkinnable()).getSelectionModel()) != null) {
                    sm.clearSelection();
                    for (Node item2 : items) {
                        if (item2 instanceof TreeCell) {
                            TreeCell<T> cell2 = (TreeCell) item2;
                            sm.select(cell2.getIndex());
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
}
