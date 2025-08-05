package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.WeakListChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.FocusModel;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.Label;
import javafx.scene.control.ResizeFeaturesBase;
import javafx.scene.control.ScrollToEvent;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableFocusModel;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/TableViewSkinBase.class */
public abstract class TableViewSkinBase<M, S, C extends Control, B extends BehaviorBase<C>, I extends IndexedCell<M>, TC extends TableColumnBase<S, ?>> extends VirtualContainerBase<C, B, I> {
    public static final String REFRESH = "tableRefreshKey";
    public static final String RECREATE = "tableRecreateKey";
    private boolean contentWidthDirty;
    private Region columnReorderLine;
    private Region columnReorderOverlay;
    private TableHeaderRow tableHeaderRow;
    private Callback<C, I> rowFactory;
    private StackPane placeholderRegion;
    private Label placeholderLabel;
    private int visibleColCount;
    protected boolean needCellsRebuilt;
    protected boolean needCellsRecreated;
    protected boolean needCellsReconfigured;
    private int itemCount;
    protected boolean forceCellRecreate;
    private MapChangeListener<Object, Object> propertiesMapListener;
    private ListChangeListener<S> rowCountListener;
    private ListChangeListener<TC> visibleLeafColumnsListener;
    private InvalidationListener widthListener;
    private InvalidationListener itemsChangeListener;
    private WeakListChangeListener<S> weakRowCountListener;
    private WeakListChangeListener<TC> weakVisibleLeafColumnsListener;
    private WeakInvalidationListener weakWidthListener;
    private WeakInvalidationListener weakItemsChangeListener;
    private static final double GOLDEN_RATIO_MULTIPLIER = 0.618033987d;
    private static final String EMPTY_TABLE_TEXT = ControlResources.getString("TableView.noContent");
    private static final String NO_COLUMNS_TEXT = ControlResources.getString("TableView.noColumns");
    private static final boolean IS_PANNABLE = ((Boolean) AccessController.doPrivileged(() -> {
        return Boolean.valueOf(Boolean.getBoolean("com.sun.javafx.scene.control.skin.TableViewSkin.pannable"));
    })).booleanValue();

    protected abstract TableSelectionModel<S> getSelectionModel();

    protected abstract TableFocusModel<S, TC> getFocusModel();

    protected abstract TablePositionBase<? extends TC> getFocusedCell();

    protected abstract ObservableList<? extends TC> getVisibleLeafColumns();

    protected abstract int getVisibleLeafIndex(TC tc);

    protected abstract TC getVisibleLeafColumn(int i2);

    protected abstract ObservableList<TC> getColumns();

    protected abstract ObservableList<TC> getSortOrder();

    protected abstract ObjectProperty<ObservableList<S>> itemsProperty();

    protected abstract ObjectProperty<Callback<C, I>> rowFactoryProperty();

    protected abstract ObjectProperty<Node> placeholderProperty();

    protected abstract BooleanProperty tableMenuButtonVisibleProperty();

    protected abstract ObjectProperty<Callback<ResizeFeaturesBase, Boolean>> columnResizePolicyProperty();

    protected abstract boolean resizeColumn(TC tc, double d2);

    protected abstract void resizeColumnToFitContent(TC tc, int i2);

    protected abstract void edit(int i2, TC tc);

    public TableViewSkinBase(C control, B behavior) {
        super(control, behavior);
        this.contentWidthDirty = true;
        this.needCellsRebuilt = true;
        this.needCellsRecreated = true;
        this.needCellsReconfigured = false;
        this.itemCount = -1;
        this.forceCellRecreate = false;
        this.propertiesMapListener = c2 -> {
            if (c2.wasAdded()) {
                if (REFRESH.equals(c2.getKey())) {
                    refreshView();
                    getSkinnable().getProperties().remove(REFRESH);
                } else if (RECREATE.equals(c2.getKey())) {
                    this.forceCellRecreate = true;
                    refreshView();
                    getSkinnable().getProperties().remove(RECREATE);
                }
            }
        };
        this.rowCountListener = c3 -> {
            while (true) {
                if (!c3.next()) {
                    break;
                }
                if (c3.wasReplaced()) {
                    this.itemCount = 0;
                    break;
                } else if (c3.getRemovedSize() == this.itemCount) {
                    this.itemCount = 0;
                    break;
                }
            }
            if (getSkinnable() instanceof TableView) {
                edit(-1, null);
            }
            this.rowCountDirty = true;
            getSkinnable().requestLayout();
        };
        this.visibleLeafColumnsListener = c4 -> {
            updateVisibleColumnCount();
            while (c4.next()) {
                updateVisibleLeafColumnWidthListeners(c4.getAddedSubList(), c4.getRemoved());
            }
        };
        this.widthListener = observable -> {
            this.needCellsReconfigured = true;
            if (getSkinnable() != null) {
                getSkinnable().requestLayout();
            }
        };
        this.weakRowCountListener = new WeakListChangeListener<>(this.rowCountListener);
        this.weakVisibleLeafColumnsListener = new WeakListChangeListener<>(this.visibleLeafColumnsListener);
        this.weakWidthListener = new WeakInvalidationListener(this.widthListener);
    }

    protected void init(C control) {
        this.flow.setPannable(IS_PANNABLE);
        this.flow.setCreateCell(flow1 -> {
            return createCell();
        });
        InvalidationListener hbarValueListener = valueModel -> {
            horizontalScroll();
        };
        this.flow.getHbar().valueProperty().addListener(hbarValueListener);
        this.flow.getHbar().setUnitIncrement(15.0d);
        this.flow.getHbar().setBlockIncrement(80.0d);
        this.columnReorderLine = new Region();
        this.columnReorderLine.getStyleClass().setAll("column-resize-line");
        this.columnReorderLine.setManaged(false);
        this.columnReorderLine.setVisible(false);
        this.columnReorderOverlay = new Region();
        this.columnReorderOverlay.getStyleClass().setAll("column-overlay");
        this.columnReorderOverlay.setVisible(false);
        this.columnReorderOverlay.setManaged(false);
        this.tableHeaderRow = createTableHeaderRow();
        this.tableHeaderRow.setFocusTraversable(false);
        getChildren().addAll(this.tableHeaderRow, this.flow, this.columnReorderOverlay, this.columnReorderLine);
        updateVisibleColumnCount();
        updateVisibleLeafColumnWidthListeners(getVisibleLeafColumns(), FXCollections.emptyObservableList());
        this.tableHeaderRow.reorderingProperty().addListener(valueModel2 -> {
            getSkinnable().requestLayout();
        });
        getVisibleLeafColumns().addListener(this.weakVisibleLeafColumnsListener);
        updateTableItems(null, itemsProperty().get());
        this.itemsChangeListener = new InvalidationListener() { // from class: com.sun.javafx.scene.control.skin.TableViewSkinBase.1
            private WeakReference<ObservableList<S>> weakItemsRef;

            {
                this.weakItemsRef = new WeakReference<>(TableViewSkinBase.this.itemsProperty().get());
            }

            @Override // javafx.beans.InvalidationListener
            public void invalidated(Observable observable) {
                ObservableList<S> oldItems = this.weakItemsRef.get();
                this.weakItemsRef = new WeakReference<>(TableViewSkinBase.this.itemsProperty().get());
                TableViewSkinBase.this.updateTableItems(oldItems, TableViewSkinBase.this.itemsProperty().get());
            }
        };
        this.weakItemsChangeListener = new WeakInvalidationListener(this.itemsChangeListener);
        itemsProperty().addListener(this.weakItemsChangeListener);
        ObservableMap<Object, Object> properties = control.getProperties();
        properties.remove(REFRESH);
        properties.remove(RECREATE);
        properties.addListener(this.propertiesMapListener);
        control.addEventHandler(ScrollToEvent.scrollToColumn(), event -> {
            scrollHorizontally((TableColumnBase) event.getScrollTarget());
        });
        InvalidationListener widthObserver = valueModel3 -> {
            this.contentWidthDirty = true;
            getSkinnable().requestLayout();
        };
        this.flow.widthProperty().addListener(widthObserver);
        this.flow.getVbar().widthProperty().addListener(widthObserver);
        registerChangeListener(rowFactoryProperty(), "ROW_FACTORY");
        registerChangeListener(placeholderProperty(), "PLACEHOLDER");
        registerChangeListener(control.widthProperty(), "WIDTH");
        registerChangeListener(this.flow.getVbar().visibleProperty(), "VBAR_VISIBLE");
    }

    @Override // com.sun.javafx.scene.control.skin.BehaviorSkinBase
    protected void handleControlPropertyChanged(String p2) {
        super.handleControlPropertyChanged(p2);
        if ("ROW_FACTORY".equals(p2)) {
            Callback<C, I> oldFactory = this.rowFactory;
            this.rowFactory = rowFactoryProperty().get();
            if (oldFactory != this.rowFactory) {
                this.needCellsRebuilt = true;
                getSkinnable().requestLayout();
                return;
            }
            return;
        }
        if ("PLACEHOLDER".equals(p2)) {
            updatePlaceholderRegionVisibility();
        } else if ("VBAR_VISIBLE".equals(p2)) {
            updateContentWidth();
        }
    }

    @Override // com.sun.javafx.scene.control.skin.BehaviorSkinBase, javafx.scene.control.SkinBase, javafx.scene.control.Skin
    public void dispose() {
        getVisibleLeafColumns().removeListener(this.weakVisibleLeafColumnsListener);
        itemsProperty().removeListener(this.weakItemsChangeListener);
        getSkinnable().getProperties().removeListener(this.propertiesMapListener);
        updateTableItems(itemsProperty().get(), null);
        super.dispose();
    }

    protected TableHeaderRow createTableHeaderRow() {
        return new TableHeaderRow(this);
    }

    public TableHeaderRow getTableHeaderRow() {
        return this.tableHeaderRow;
    }

    public Region getColumnReorderLine() {
        return this.columnReorderLine;
    }

    public int onScrollPageDown(boolean isFocusDriven) {
        boolean isSelected;
        TableSelectionModel<S> sm = getSelectionModel();
        if (sm == null) {
            return -1;
        }
        int itemCount = getItemCount();
        IndexedCell lastVisibleCellWithinViewPort = this.flow.getLastVisibleCellWithinViewPort();
        if (lastVisibleCellWithinViewPort == null) {
            return -1;
        }
        int lastVisibleCellIndex = lastVisibleCellWithinViewPort.getIndex();
        int lastVisibleCellIndex2 = lastVisibleCellIndex >= itemCount ? itemCount - 1 : lastVisibleCellIndex;
        if (isFocusDriven) {
            isSelected = lastVisibleCellWithinViewPort.isFocused() || isCellFocused(lastVisibleCellIndex2);
        } else {
            isSelected = lastVisibleCellWithinViewPort.isSelected() || isCellSelected(lastVisibleCellIndex2);
        }
        if (isSelected) {
            boolean isLeadIndex = isLeadIndex(isFocusDriven, lastVisibleCellIndex2);
            if (isLeadIndex) {
                this.flow.showAsFirst(lastVisibleCellWithinViewPort);
                IndexedCell lastVisibleCellWithinViewPort2 = this.flow.getLastVisibleCellWithinViewPort();
                lastVisibleCellWithinViewPort = lastVisibleCellWithinViewPort2 == null ? lastVisibleCellWithinViewPort : lastVisibleCellWithinViewPort2;
            }
        }
        int newSelectionIndex = lastVisibleCellWithinViewPort.getIndex();
        int newSelectionIndex2 = newSelectionIndex >= itemCount ? itemCount - 1 : newSelectionIndex;
        this.flow.show(newSelectionIndex2);
        return newSelectionIndex2;
    }

    public int onScrollPageUp(boolean isFocusDriven) {
        boolean isSelected;
        IndexedCell firstVisibleCellWithinViewPort = this.flow.getFirstVisibleCellWithinViewPort();
        if (firstVisibleCellWithinViewPort == null) {
            return -1;
        }
        int firstVisibleCellIndex = firstVisibleCellWithinViewPort.getIndex();
        if (isFocusDriven) {
            isSelected = firstVisibleCellWithinViewPort.isFocused() || isCellFocused(firstVisibleCellIndex);
        } else {
            isSelected = firstVisibleCellWithinViewPort.isSelected() || isCellSelected(firstVisibleCellIndex);
        }
        if (isSelected) {
            boolean isLeadIndex = isLeadIndex(isFocusDriven, firstVisibleCellIndex);
            if (isLeadIndex) {
                this.flow.showAsLast(firstVisibleCellWithinViewPort);
                IndexedCell firstVisibleCellWithinViewPort2 = this.flow.getFirstVisibleCellWithinViewPort();
                firstVisibleCellWithinViewPort = firstVisibleCellWithinViewPort2 == null ? firstVisibleCellWithinViewPort : firstVisibleCellWithinViewPort2;
            }
        }
        int newSelectionIndex = firstVisibleCellWithinViewPort.getIndex();
        this.flow.show(newSelectionIndex);
        return newSelectionIndex;
    }

    private boolean isLeadIndex(boolean isFocusDriven, int index) {
        TableSelectionModel<S> sm = getSelectionModel();
        FocusModel<S> fm = getFocusModel();
        return (isFocusDriven && fm.getFocusedIndex() == index) || (!isFocusDriven && sm.getSelectedIndex() == index);
    }

    boolean isColumnPartiallyOrFullyVisible(TC col) {
        if (col == null || !col.isVisible()) {
            return false;
        }
        double scrollX = this.flow.getHbar().getValue();
        double start = 0.0d;
        ObservableList<? extends TC> visibleLeafColumns = getVisibleLeafColumns();
        int max = visibleLeafColumns.size();
        for (int i2 = 0; i2 < max; i2++) {
            TableColumnBase<S, ?> c2 = visibleLeafColumns.get(i2);
            if (c2.equals(col)) {
                break;
            }
            start += c2.getWidth();
        }
        double end = start + col.getWidth();
        Insets padding = getSkinnable().getPadding();
        double headerWidth = (getSkinnable().getWidth() - padding.getLeft()) + padding.getRight();
        return (start >= scrollX || end > scrollX) && (start < headerWidth + scrollX || end <= headerWidth + scrollX);
    }

    protected void horizontalScroll() {
        this.tableHeaderRow.updateScrollX();
    }

    @Override // com.sun.javafx.scene.control.skin.VirtualContainerBase
    protected void updateRowCount() {
        updatePlaceholderRegionVisibility();
        int oldCount = this.itemCount;
        int newCount = getItemCount();
        this.itemCount = newCount;
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

    protected void onFocusPreviousCell() {
        TableFocusModel<S, TC> fm = getFocusModel();
        if (fm == null) {
            return;
        }
        this.flow.show(fm.getFocusedIndex());
    }

    protected void onFocusNextCell() {
        TableFocusModel<S, TC> fm = getFocusModel();
        if (fm == null) {
            return;
        }
        this.flow.show(fm.getFocusedIndex());
    }

    protected void onSelectPreviousCell() {
        SelectionModel<S> sm = getSelectionModel();
        if (sm == null) {
            return;
        }
        this.flow.show(sm.getSelectedIndex());
    }

    protected void onSelectNextCell() {
        SelectionModel<S> sm = getSelectionModel();
        if (sm == null) {
            return;
        }
        this.flow.show(sm.getSelectedIndex());
    }

    protected void onSelectLeftCell() {
        scrollHorizontally();
    }

    protected void onSelectRightCell() {
        scrollHorizontally();
    }

    protected void onMoveToFirstCell() {
        this.flow.show(0);
        this.flow.setPosition(0.0d);
    }

    protected void onMoveToLastCell() {
        int endPos = getItemCount();
        this.flow.show(endPos);
        this.flow.setPosition(1.0d);
    }

    public void updateTableItems(ObservableList<S> oldList, ObservableList<S> newList) {
        if (oldList != null) {
            oldList.removeListener(this.weakRowCountListener);
        }
        if (newList != null) {
            newList.addListener(this.weakRowCountListener);
        }
        this.rowCountDirty = true;
        getSkinnable().requestLayout();
    }

    private void checkContentWidthState() {
        if (this.contentWidthDirty || getItemCount() == 0) {
            updateContentWidth();
            this.contentWidthDirty = false;
        }
    }

    @Override // javafx.scene.control.SkinBase
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return 400.0d;
    }

    @Override // javafx.scene.control.SkinBase
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        double prefHeight = computePrefHeight(-1.0d, topInset, rightInset, bottomInset, leftInset);
        List<? extends TC> cols = getVisibleLeafColumns();
        if (cols == null || cols.isEmpty()) {
            return prefHeight * GOLDEN_RATIO_MULTIPLIER;
        }
        double pw = leftInset + rightInset;
        int max = cols.size();
        for (int i2 = 0; i2 < max; i2++) {
            TC tc = cols.get(i2);
            pw += Math.max(tc.getPrefWidth(), tc.getMinWidth());
        }
        return Math.max(pw, prefHeight * GOLDEN_RATIO_MULTIPLIER);
    }

    @Override // com.sun.javafx.scene.control.skin.VirtualContainerBase, javafx.scene.control.SkinBase
    protected void layoutChildren(double x2, double y2, double w2, double h2) {
        C table = getSkinnable();
        if (table == null) {
            return;
        }
        super.layoutChildren(x2, y2, w2, h2);
        if (this.needCellsRecreated) {
            this.flow.recreateCells();
        } else if (this.needCellsRebuilt) {
            this.flow.rebuildCells();
        } else if (this.needCellsReconfigured) {
            this.flow.reconfigureCells();
        }
        this.needCellsRebuilt = false;
        this.needCellsRecreated = false;
        this.needCellsReconfigured = false;
        double baselineOffset = table.getLayoutBounds().getHeight() / 2.0d;
        double tableHeaderRowHeight = this.tableHeaderRow.prefHeight(-1.0d);
        layoutInArea(this.tableHeaderRow, x2, y2, w2, tableHeaderRowHeight, baselineOffset, HPos.CENTER, VPos.CENTER);
        double y3 = y2 + tableHeaderRowHeight;
        double flowHeight = Math.floor(h2 - tableHeaderRowHeight);
        if (getItemCount() == 0 || this.visibleColCount == 0) {
            layoutInArea(this.placeholderRegion, x2, y3, w2, flowHeight, baselineOffset, HPos.CENTER, VPos.CENTER);
        } else {
            layoutInArea(this.flow, x2, y3, w2, flowHeight, baselineOffset, HPos.CENTER, VPos.CENTER);
        }
        if (this.tableHeaderRow.getReorderingRegion() != null) {
            TableColumnHeader reorderingColumnHeader = this.tableHeaderRow.getReorderingRegion();
            TableColumnBase reorderingColumn = reorderingColumnHeader.getTableColumn();
            if (reorderingColumn != null) {
                Node n2 = this.tableHeaderRow.getReorderingRegion();
                double minX = this.tableHeaderRow.sceneToLocal(n2.localToScene(n2.getBoundsInLocal())).getMinX();
                double overlayWidth = reorderingColumnHeader.getWidth();
                if (minX < 0.0d) {
                    overlayWidth += minX;
                }
                double minX2 = minX < 0.0d ? 0.0d : minX;
                if (minX2 + overlayWidth > w2) {
                    overlayWidth = w2 - minX2;
                    if (this.flow.getVbar().isVisible()) {
                        overlayWidth -= this.flow.getVbar().getWidth() - 1.0d;
                    }
                }
                double contentAreaHeight = flowHeight;
                if (this.flow.getHbar().isVisible()) {
                    contentAreaHeight -= this.flow.getHbar().getHeight();
                }
                this.columnReorderOverlay.resize(overlayWidth, contentAreaHeight);
                this.columnReorderOverlay.setLayoutX(minX2);
                this.columnReorderOverlay.setLayoutY(this.tableHeaderRow.getHeight());
            }
            double cw = this.columnReorderLine.snappedLeftInset() + this.columnReorderLine.snappedRightInset();
            double lineHeight = h2 - (this.flow.getHbar().isVisible() ? this.flow.getHbar().getHeight() - 1.0d : 0.0d);
            this.columnReorderLine.resizeRelocate(0.0d, this.columnReorderLine.snappedTopInset(), cw, lineHeight);
        }
        this.columnReorderLine.setVisible(this.tableHeaderRow.isReordering());
        this.columnReorderOverlay.setVisible(this.tableHeaderRow.isReordering());
        checkContentWidthState();
    }

    private void updateVisibleColumnCount() {
        this.visibleColCount = getVisibleLeafColumns().size();
        updatePlaceholderRegionVisibility();
        this.needCellsRebuilt = true;
        getSkinnable().requestLayout();
    }

    private void updateVisibleLeafColumnWidthListeners(List<? extends TC> added, List<? extends TC> removed) {
        int max = removed.size();
        for (int i2 = 0; i2 < max; i2++) {
            TC tc = removed.get(i2);
            tc.widthProperty().removeListener(this.weakWidthListener);
        }
        int max2 = added.size();
        for (int i3 = 0; i3 < max2; i3++) {
            TC tc2 = added.get(i3);
            tc2.widthProperty().addListener(this.weakWidthListener);
        }
        this.needCellsRebuilt = true;
        getSkinnable().requestLayout();
    }

    protected final void updatePlaceholderRegionVisibility() {
        boolean visible = this.visibleColCount == 0 || getItemCount() == 0;
        if (visible) {
            if (this.placeholderRegion == null) {
                this.placeholderRegion = new StackPane();
                this.placeholderRegion.getStyleClass().setAll("placeholder");
                getChildren().add(this.placeholderRegion);
            }
            Node placeholderNode = placeholderProperty().get();
            if (placeholderNode == null) {
                if (this.placeholderLabel == null) {
                    this.placeholderLabel = new Label();
                }
                String s2 = this.visibleColCount == 0 ? NO_COLUMNS_TEXT : EMPTY_TABLE_TEXT;
                this.placeholderLabel.setText(s2);
                this.placeholderRegion.getChildren().setAll(this.placeholderLabel);
            } else {
                this.placeholderRegion.getChildren().setAll(placeholderNode);
            }
        }
        this.flow.setVisible(!visible);
        if (this.placeholderRegion != null) {
            this.placeholderRegion.setVisible(visible);
        }
    }

    private void updateContentWidth() {
        double contentWidth = this.flow.getWidth();
        if (this.flow.getVbar().isVisible()) {
            contentWidth -= this.flow.getVbar().getWidth();
        }
        if (contentWidth <= 0.0d) {
            Control c2 = getSkinnable();
            contentWidth = c2.getWidth() - (snappedLeftInset() + snappedRightInset());
        }
        getSkinnable().getProperties().put("TableView.contentWidth", Double.valueOf(Math.floor(Math.max(0.0d, contentWidth))));
    }

    private void refreshView() {
        this.rowCountDirty = true;
        Control c2 = getSkinnable();
        if (c2 != null) {
            c2.requestLayout();
        }
    }

    protected void scrollHorizontally() {
        TableFocusModel<S, TC> fm = getFocusModel();
        if (fm == null) {
            return;
        }
        scrollHorizontally(getFocusedCell().getTableColumn());
    }

    protected void scrollHorizontally(TC col) {
        double newPos;
        if (col == null || !col.isVisible()) {
            return;
        }
        Control control = getSkinnable();
        TableColumnHeader header = this.tableHeaderRow.getColumnHeaderFor(col);
        if (header == null || header.getWidth() <= 0.0d) {
            Platform.runLater(() -> {
                scrollHorizontally(col);
            });
            return;
        }
        double start = 0.0d;
        for (TC c2 : getVisibleLeafColumns()) {
            if (c2.equals(col)) {
                break;
            } else {
                start += c2.getWidth();
            }
        }
        double end = start + col.getWidth();
        double headerWidth = (control.getWidth() - snappedLeftInset()) - snappedRightInset();
        double pos = this.flow.getHbar().getValue();
        double max = this.flow.getHbar().getMax();
        if (start >= pos || start < 0.0d) {
            double delta = (start < 0.0d || end > headerWidth) ? start - pos : 0.0d;
            newPos = pos + delta > max ? max : pos + delta;
        } else {
            newPos = start;
        }
        this.flow.getHbar().setValue(newPos);
    }

    private boolean isCellSelected(int row) {
        TableSelectionModel<S> sm = getSelectionModel();
        if (sm == null || !sm.isCellSelectionEnabled()) {
            return false;
        }
        int columnCount = getVisibleLeafColumns().size();
        for (int col = 0; col < columnCount; col++) {
            if (sm.isSelected(row, getVisibleLeafColumn(col))) {
                return true;
            }
        }
        return false;
    }

    private boolean isCellFocused(int row) {
        TableFocusModel<S, TC> fm = getFocusModel();
        if (fm == null) {
            return false;
        }
        int columnCount = getVisibleLeafColumns().size();
        for (int col = 0; col < columnCount; col++) {
            if (fm.isFocused(row, getVisibleLeafColumn(col))) {
                return true;
            }
        }
        return false;
    }

    @Override // javafx.scene.control.SkinBase
    protected Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case FOCUS_ITEM:
                int focusedIndex = getFocusModel().getFocusedIndex();
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
            case CELL_AT_ROW_COLUMN:
                int rowIndex = ((Integer) parameters[0]).intValue();
                return this.flow.getPrivateCell(rowIndex);
            case COLUMN_AT_INDEX:
                int index = ((Integer) parameters[0]).intValue();
                return getTableHeaderRow().getColumnHeaderFor(getVisibleLeafColumn(index));
            case HEADER:
                return getTableHeaderRow();
            case VERTICAL_SCROLLBAR:
                return this.flow.getVbar();
            case HORIZONTAL_SCROLLBAR:
                return this.flow.getHbar();
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }
}
