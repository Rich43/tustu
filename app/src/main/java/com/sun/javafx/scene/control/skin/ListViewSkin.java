package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ListViewBehavior;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.WeakListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.control.FocusModel;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionModel;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ListViewSkin.class */
public class ListViewSkin<T> extends VirtualContainerBase<ListView<T>, ListViewBehavior<T>, ListCell<T>> {
    public static final String RECREATE = "listRecreateKey";
    private StackPane placeholderRegion;
    private Node placeholderNode;
    private static final String EMPTY_LIST_TEXT = ControlResources.getString("ListView.noContent");
    private static final boolean IS_PANNABLE = ((Boolean) AccessController.doPrivileged(() -> {
        return Boolean.valueOf(Boolean.getBoolean("com.sun.javafx.scene.control.skin.ListViewSkin.pannable"));
    })).booleanValue();
    private ObservableList<T> listViewItems;
    private final InvalidationListener itemsChangeListener;
    private MapChangeListener<Object, Object> propertiesMapListener;
    private final ListChangeListener<T> listViewItemsListener;
    private final WeakListChangeListener<T> weakListViewItemsListener;
    private int itemCount;
    private boolean needCellsRebuilt;
    private boolean needCellsReconfigured;

    /* JADX WARN: Multi-variable type inference failed */
    public ListViewSkin(ListView<T> listView) {
        super(listView, new ListViewBehavior(listView));
        this.itemsChangeListener = observable -> {
            updateListViewItems();
        };
        this.propertiesMapListener = c2 -> {
            if (c2.wasAdded() && RECREATE.equals(c2.getKey())) {
                this.needCellsRebuilt = true;
                ((ListView) getSkinnable()).requestLayout();
                ((ListView) getSkinnable()).getProperties().remove(RECREATE);
            }
        };
        this.listViewItemsListener = new ListChangeListener<T>() { // from class: com.sun.javafx.scene.control.skin.ListViewSkin.1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // javafx.collections.ListChangeListener
            public void onChanged(ListChangeListener.Change<? extends T> c3) {
                while (true) {
                    if (!c3.next()) {
                        break;
                    }
                    if (!c3.wasReplaced()) {
                        if (c3.getRemovedSize() == ListViewSkin.this.itemCount) {
                            ListViewSkin.this.itemCount = 0;
                            break;
                        }
                    } else {
                        for (int i2 = c3.getFrom(); i2 < c3.getTo(); i2++) {
                            ListViewSkin.this.flow.setCellDirty(i2);
                        }
                    }
                }
                ((ListView) ListViewSkin.this.getSkinnable()).edit(-1);
                ListViewSkin.this.rowCountDirty = true;
                ((ListView) ListViewSkin.this.getSkinnable()).requestLayout();
            }
        };
        this.weakListViewItemsListener = new WeakListChangeListener<>(this.listViewItemsListener);
        this.itemCount = -1;
        this.needCellsRebuilt = true;
        this.needCellsReconfigured = false;
        updateListViewItems();
        this.flow.setId("virtual-flow");
        this.flow.setPannable(IS_PANNABLE);
        this.flow.setVertical(((ListView) getSkinnable()).getOrientation() == Orientation.VERTICAL);
        this.flow.setCreateCell(flow1 -> {
            return createCell();
        });
        this.flow.setFixedCellSize(listView.getFixedCellSize());
        getChildren().add(this.flow);
        EventHandler<MouseEvent> ml = event -> {
            if (listView.getEditingIndex() > -1) {
                listView.edit(-1);
            }
            if (listView.isFocusTraversable()) {
                listView.requestFocus();
            }
        };
        this.flow.getVbar().addEventFilter(MouseEvent.MOUSE_PRESSED, ml);
        this.flow.getHbar().addEventFilter(MouseEvent.MOUSE_PRESSED, ml);
        updateRowCount();
        listView.itemsProperty().addListener(new WeakInvalidationListener(this.itemsChangeListener));
        ObservableMap<Object, Object> properties = listView.getProperties();
        properties.remove(RECREATE);
        properties.addListener(this.propertiesMapListener);
        ((ListViewBehavior) getBehavior()).setOnFocusPreviousRow(() -> {
            onFocusPreviousCell();
        });
        ((ListViewBehavior) getBehavior()).setOnFocusNextRow(() -> {
            onFocusNextCell();
        });
        ((ListViewBehavior) getBehavior()).setOnMoveToFirstCell(() -> {
            onMoveToFirstCell();
        });
        ((ListViewBehavior) getBehavior()).setOnMoveToLastCell(() -> {
            onMoveToLastCell();
        });
        ((ListViewBehavior) getBehavior()).setOnScrollPageDown(isFocusDriven -> {
            return Integer.valueOf(onScrollPageDown(isFocusDriven.booleanValue()));
        });
        ((ListViewBehavior) getBehavior()).setOnScrollPageUp(isFocusDriven2 -> {
            return Integer.valueOf(onScrollPageUp(isFocusDriven2.booleanValue()));
        });
        ((ListViewBehavior) getBehavior()).setOnSelectPreviousRow(() -> {
            onSelectPreviousCell();
        });
        ((ListViewBehavior) getBehavior()).setOnSelectNextRow(() -> {
            onSelectNextCell();
        });
        registerChangeListener(listView.itemsProperty(), "ITEMS");
        registerChangeListener(listView.orientationProperty(), "ORIENTATION");
        registerChangeListener(listView.cellFactoryProperty(), "CELL_FACTORY");
        registerChangeListener(listView.parentProperty(), "PARENT");
        registerChangeListener(listView.placeholderProperty(), "PLACEHOLDER");
        registerChangeListener(listView.fixedCellSizeProperty(), "FIXED_CELL_SIZE");
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.BehaviorSkinBase
    protected void handleControlPropertyChanged(String p2) {
        super.handleControlPropertyChanged(p2);
        if ("ITEMS".equals(p2)) {
            updateListViewItems();
            return;
        }
        if ("ORIENTATION".equals(p2)) {
            this.flow.setVertical(((ListView) getSkinnable()).getOrientation() == Orientation.VERTICAL);
            return;
        }
        if ("CELL_FACTORY".equals(p2)) {
            this.flow.recreateCells();
            return;
        }
        if ("PARENT".equals(p2)) {
            if (((ListView) getSkinnable()).getParent() != null && ((ListView) getSkinnable()).isVisible()) {
                ((ListView) getSkinnable()).requestLayout();
                return;
            }
            return;
        }
        if ("PLACEHOLDER".equals(p2)) {
            updatePlaceholderRegionVisibility();
        } else if ("FIXED_CELL_SIZE".equals(p2)) {
            this.flow.setFixedCellSize(((ListView) getSkinnable()).getFixedCellSize());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void updateListViewItems() {
        if (this.listViewItems != null) {
            this.listViewItems.removeListener(this.weakListViewItemsListener);
        }
        this.listViewItems = ((ListView) getSkinnable()).getItems();
        if (this.listViewItems != null) {
            this.listViewItems.addListener(this.weakListViewItemsListener);
        }
        this.rowCountDirty = true;
        ((ListView) getSkinnable()).requestLayout();
    }

    @Override // com.sun.javafx.scene.control.skin.VirtualContainerBase
    public int getItemCount() {
        return this.itemCount;
    }

    @Override // com.sun.javafx.scene.control.skin.VirtualContainerBase
    protected void updateRowCount() {
        if (this.flow == null) {
            return;
        }
        int oldCount = this.itemCount;
        int newCount = this.listViewItems == null ? 0 : this.listViewItems.size();
        this.itemCount = newCount;
        this.flow.setCellCount(newCount);
        updatePlaceholderRegionVisibility();
        if (newCount != oldCount) {
            this.needCellsRebuilt = true;
        } else {
            this.needCellsReconfigured = true;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected final void updatePlaceholderRegionVisibility() {
        boolean visible = getItemCount() == 0;
        if (visible) {
            this.placeholderNode = ((ListView) getSkinnable()).getPlaceholder();
            if (this.placeholderNode == null && EMPTY_LIST_TEXT != null && !EMPTY_LIST_TEXT.isEmpty()) {
                this.placeholderNode = new Label();
                ((Label) this.placeholderNode).setText(EMPTY_LIST_TEXT);
            }
            if (this.placeholderNode != null) {
                if (this.placeholderRegion == null) {
                    this.placeholderRegion = new StackPane();
                    this.placeholderRegion.getStyleClass().setAll("placeholder");
                    getChildren().add(this.placeholderRegion);
                }
                this.placeholderRegion.getChildren().setAll(this.placeholderNode);
            }
        }
        this.flow.setVisible(!visible);
        if (this.placeholderRegion != null) {
            this.placeholderRegion.setVisible(visible);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.VirtualContainerBase
    public ListCell<T> createCell() {
        ListCell<T> cell;
        if (((ListView) getSkinnable()).getCellFactory() != null) {
            cell = (ListCell) ((ListView) getSkinnable()).getCellFactory().call(getSkinnable());
        } else {
            cell = createDefaultCellImpl();
        }
        cell.updateListView((ListView) getSkinnable());
        return cell;
    }

    private static <T> ListCell<T> createDefaultCellImpl() {
        return new ListCell<T>() { // from class: com.sun.javafx.scene.control.skin.ListViewSkin.2
            /* JADX WARN: Multi-variable type inference failed */
            @Override // javafx.scene.control.Cell
            public void updateItem(T t2, boolean empty) {
                super.updateItem(t2, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                    return;
                }
                if (t2 instanceof Node) {
                    setText(null);
                    Node currentNode = getGraphic();
                    Node newNode = (Node) t2;
                    if (currentNode == null || !currentNode.equals(newNode)) {
                        setGraphic(newNode);
                        return;
                    }
                    return;
                }
                setText(t2 == 0 ? FXMLLoader.NULL_KEYWORD : t2.toString());
                setGraphic(null);
            }
        };
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
        if (getItemCount() == 0) {
            if (this.placeholderRegion != null) {
                this.placeholderRegion.setVisible(w2 > 0.0d && h2 > 0.0d);
                this.placeholderRegion.resizeRelocate(x2, y2, w2, h2);
                return;
            }
            return;
        }
        this.flow.resizeRelocate(x2, y2, w2, h2);
    }

    @Override // javafx.scene.control.SkinBase
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        checkState();
        if (getItemCount() == 0) {
            if (this.placeholderRegion == null) {
                updatePlaceholderRegionVisibility();
            }
            if (this.placeholderRegion != null) {
                return this.placeholderRegion.prefWidth(height) + leftInset + rightInset;
            }
        }
        return computePrefHeight(-1.0d, topInset, rightInset, bottomInset, leftInset) * 0.618033987d;
    }

    @Override // javafx.scene.control.SkinBase
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return 400.0d;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void onFocusPreviousCell() {
        FocusModel<T> fm = ((ListView) getSkinnable()).getFocusModel();
        if (fm == null) {
            return;
        }
        this.flow.show(fm.getFocusedIndex());
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void onFocusNextCell() {
        FocusModel<T> fm = ((ListView) getSkinnable()).getFocusModel();
        if (fm == null) {
            return;
        }
        this.flow.show(fm.getFocusedIndex());
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void onSelectPreviousCell() {
        SelectionModel<T> sm = ((ListView) getSkinnable()).getSelectionModel();
        if (sm == null) {
            return;
        }
        int pos = sm.getSelectedIndex();
        this.flow.show(pos);
        IndexedCell<T> cell = this.flow.getFirstVisibleCell();
        if (cell == null || pos < cell.getIndex()) {
            this.flow.setPosition(pos / getItemCount());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void onSelectNextCell() {
        SelectionModel<T> sm = ((ListView) getSkinnable()).getSelectionModel();
        if (sm == null) {
            return;
        }
        int pos = sm.getSelectedIndex();
        this.flow.show(pos);
        ListCell<T> cell = (ListCell) this.flow.getLastVisibleCell();
        if (cell == null || cell.getIndex() < pos) {
            this.flow.setPosition(pos / getItemCount());
        }
    }

    private void onMoveToFirstCell() {
        this.flow.show(0);
        this.flow.setPosition(0.0d);
    }

    private void onMoveToLastCell() {
        int endPos = getItemCount() - 1;
        this.flow.show(endPos);
        this.flow.setPosition(1.0d);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private int onScrollPageDown(boolean isFocusDriven) {
        boolean isSelected;
        ListCell<T> lastVisibleCell = (ListCell) this.flow.getLastVisibleCellWithinViewPort();
        if (lastVisibleCell == null) {
            return -1;
        }
        SelectionModel<T> sm = ((ListView) getSkinnable()).getSelectionModel();
        FocusModel<T> fm = ((ListView) getSkinnable()).getFocusModel();
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
                ListCell<T> newLastVisibleCell = (ListCell) this.flow.getLastVisibleCellWithinViewPort();
                lastVisibleCell = newLastVisibleCell == null ? lastVisibleCell : newLastVisibleCell;
            }
        }
        int newSelectionIndex = lastVisibleCell.getIndex();
        this.flow.show((VirtualFlow<I>) lastVisibleCell);
        return newSelectionIndex;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private int onScrollPageUp(boolean isFocusDriven) {
        boolean isSelected;
        ListCell<T> firstVisibleCell = (ListCell) this.flow.getFirstVisibleCellWithinViewPort();
        if (firstVisibleCell == null) {
            return -1;
        }
        SelectionModel<T> sm = ((ListView) getSkinnable()).getSelectionModel();
        FocusModel<T> fm = ((ListView) getSkinnable()).getFocusModel();
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
                ListCell<T> newFirstVisibleCell = (ListCell) this.flow.getFirstVisibleCellWithinViewPort();
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
                int focusedIndex = ((ListView) getSkinnable()).getFocusModel().getFocusedIndex();
                if (focusedIndex == -1) {
                    if (this.placeholderRegion != null && this.placeholderRegion.isVisible()) {
                        return this.placeholderRegion.getChildren().get(0);
                    }
                    if (getItemCount() > 0) {
                        focusedIndex = 0;
                    } else {
                        return null;
                    }
                }
                return this.flow.getPrivateCell(focusedIndex);
            case ITEM_COUNT:
                return Integer.valueOf(getItemCount());
            case ITEM_AT_INDEX:
                Integer rowIndex = (Integer) parameters[0];
                if (rowIndex != null && 0 <= rowIndex.intValue() && rowIndex.intValue() < getItemCount()) {
                    return this.flow.getPrivateCell(rowIndex.intValue());
                }
                return null;
            case SELECTED_ITEMS:
                MultipleSelectionModel<T> sm = ((ListView) getSkinnable()).getSelectionModel();
                ObservableList<Integer> indices = sm.getSelectedIndices();
                List<Node> selection = new ArrayList<>(indices.size());
                Iterator<Integer> it = indices.iterator();
                while (it.hasNext()) {
                    int i2 = it.next().intValue();
                    ListCell<T> row = (ListCell) this.flow.getPrivateCell(i2);
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
        MultipleSelectionModel<T> sm;
        switch (action) {
            case SHOW_ITEM:
                Node item = (Node) parameters[0];
                if (item instanceof ListCell) {
                    ListCell<T> cell = (ListCell) item;
                    this.flow.show(cell.getIndex());
                    break;
                }
                break;
            case SET_SELECTED_ITEMS:
                ObservableList<Node> items = (ObservableList) parameters[0];
                if (items != null && (sm = ((ListView) getSkinnable()).getSelectionModel()) != null) {
                    sm.clearSelection();
                    for (Node item2 : items) {
                        if (item2 instanceof ListCell) {
                            ListCell<T> cell2 = (ListCell) item2;
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
