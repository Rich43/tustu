package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.MultiplePropertyChangeListenerHandler;
import com.sun.javafx.scene.control.TableColumnSortTypeWrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/TableColumnHeader.class */
public class TableColumnHeader extends Region {
    static final double DEFAULT_COLUMN_WIDTH = 80.0d;
    private double dragOffset;
    private final TableViewSkinBase<?, ?, ?, ?, ?, TableColumnBase<?, ?>> skin;
    private NestedTableColumnHeader nestedColumnHeader;
    private final TableColumnBase<?, ?> column;
    private TableHeaderRow tableHeaderRow;
    private NestedTableColumnHeader parentHeader;
    Label label;
    private Region arrow;
    private Label sortOrderLabel;
    private HBox sortOrderDots;
    private Node sortArrow;
    private boolean isSortColumn;
    private int newColumnPos;
    protected final Region columnReorderLine;
    protected final MultiplePropertyChangeListenerHandler changeListenerHandler;
    private DoubleProperty size;
    private static final EventHandler<MouseEvent> mousePressedHandler = me -> {
        TableColumnHeader header = (TableColumnHeader) me.getSource();
        header.getTableViewSkin().getSkinnable().requestFocus();
        if (me.isPrimaryButtonDown() && header.isColumnReorderingEnabled()) {
            header.columnReorderingStarted(me.getX());
        }
        me.consume();
    };
    private static final EventHandler<MouseEvent> mouseDraggedHandler = me -> {
        TableColumnHeader header = (TableColumnHeader) me.getSource();
        if (me.isPrimaryButtonDown() && header.isColumnReorderingEnabled()) {
            header.columnReordering(me.getSceneX(), me.getSceneY());
        }
        me.consume();
    };
    private static final EventHandler<MouseEvent> mouseReleasedHandler = me -> {
        if (me.isPopupTrigger()) {
            return;
        }
        TableColumnHeader header = (TableColumnHeader) me.getSource();
        TableColumnBase tableColumn = header.getTableColumn();
        ContextMenu menu = tableColumn.getContextMenu();
        if (menu == null || !menu.isShowing()) {
            if (header.getTableHeaderRow().isReordering() && header.isColumnReorderingEnabled()) {
                header.columnReorderingComplete();
            } else if (me.isStillSincePress()) {
                header.sortColumn(me.isShiftDown());
            }
            me.consume();
        }
    };
    private static final EventHandler<ContextMenuEvent> contextMenuRequestedHandler = me -> {
        TableColumnHeader header = (TableColumnHeader) me.getSource();
        TableColumnBase tableColumn = header.getTableColumn();
        ContextMenu menu = tableColumn.getContextMenu();
        if (menu != null) {
            menu.show(header, me.getScreenX(), me.getScreenY());
            me.consume();
        }
    };
    private static final PseudoClass PSEUDO_CLASS_LAST_VISIBLE = PseudoClass.getPseudoClass("last-visible");
    private boolean autoSizeComplete = false;
    int sortPos = -1;
    private boolean isSizeDirty = false;
    boolean isLastVisibleColumn = false;
    int columnIndex = -1;
    private ListChangeListener<TableColumnBase<?, ?>> sortOrderListener = c2 -> {
        updateSortPosition();
    };
    private ListChangeListener<TableColumnBase<?, ?>> visibleLeafColumnsListener = c2 -> {
        updateColumnIndex();
        updateSortPosition();
    };
    private ListChangeListener<String> styleClassListener = c2 -> {
        updateStyleClass();
    };
    private WeakListChangeListener<TableColumnBase<?, ?>> weakSortOrderListener = new WeakListChangeListener<>(this.sortOrderListener);
    private final WeakListChangeListener<TableColumnBase<?, ?>> weakVisibleLeafColumnsListener = new WeakListChangeListener<>(this.visibleLeafColumnsListener);
    private final WeakListChangeListener<String> weakStyleClassListener = new WeakListChangeListener<>(this.styleClassListener);

    public TableColumnHeader(TableViewSkinBase skin, TableColumnBase tc) {
        this.skin = skin;
        this.column = tc;
        this.columnReorderLine = skin.getColumnReorderLine();
        setFocusTraversable(false);
        updateColumnIndex();
        initUI();
        this.changeListenerHandler = new MultiplePropertyChangeListenerHandler(p2 -> {
            handlePropertyChanged(p2);
            return null;
        });
        this.changeListenerHandler.registerChangeListener(sceneProperty(), "SCENE");
        if (this.column != null && skin != null) {
            updateSortPosition();
            skin.getSortOrder().addListener(this.weakSortOrderListener);
            skin.getVisibleLeafColumns().addListener(this.weakVisibleLeafColumnsListener);
        }
        if (this.column != null) {
            this.changeListenerHandler.registerChangeListener(this.column.idProperty(), "TABLE_COLUMN_ID");
            this.changeListenerHandler.registerChangeListener(this.column.styleProperty(), "TABLE_COLUMN_STYLE");
            this.changeListenerHandler.registerChangeListener(this.column.widthProperty(), "TABLE_COLUMN_WIDTH");
            this.changeListenerHandler.registerChangeListener(this.column.visibleProperty(), "TABLE_COLUMN_VISIBLE");
            this.changeListenerHandler.registerChangeListener(this.column.sortNodeProperty(), "TABLE_COLUMN_SORT_NODE");
            this.changeListenerHandler.registerChangeListener(this.column.sortableProperty(), "TABLE_COLUMN_SORTABLE");
            this.changeListenerHandler.registerChangeListener(this.column.textProperty(), "TABLE_COLUMN_TEXT");
            this.changeListenerHandler.registerChangeListener(this.column.graphicProperty(), "TABLE_COLUMN_GRAPHIC");
            this.column.getStyleClass().addListener(this.weakStyleClassListener);
            setId(this.column.getId());
            setStyle(this.column.getStyle());
            updateStyleClass();
            setAccessibleRole(AccessibleRole.TABLE_COLUMN);
        }
    }

    private double getSize() {
        if (this.size == null) {
            return 20.0d;
        }
        return this.size.doubleValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public DoubleProperty sizeProperty() {
        if (this.size == null) {
            this.size = new StyleableDoubleProperty(20.0d) { // from class: com.sun.javafx.scene.control.skin.TableColumnHeader.1
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    double value = get();
                    if (value <= 0.0d) {
                        if (isBound()) {
                            unbind();
                        }
                        set(20.0d);
                        throw new IllegalArgumentException("Size cannot be 0 or negative");
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return TableColumnHeader.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "size";
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.SIZE;
                }
            };
        }
        return this.size;
    }

    protected void handlePropertyChanged(String p2) {
        if ("SCENE".equals(p2)) {
            updateScene();
            return;
        }
        if ("TABLE_COLUMN_VISIBLE".equals(p2)) {
            setVisible(getTableColumn().isVisible());
            return;
        }
        if ("TABLE_COLUMN_WIDTH".equals(p2)) {
            this.isSizeDirty = true;
            requestLayout();
            return;
        }
        if ("TABLE_COLUMN_ID".equals(p2)) {
            setId(this.column.getId());
            return;
        }
        if ("TABLE_COLUMN_STYLE".equals(p2)) {
            setStyle(this.column.getStyle());
            return;
        }
        if ("TABLE_COLUMN_SORT_TYPE".equals(p2)) {
            updateSortGrid();
            if (this.arrow != null) {
                this.arrow.setRotate(TableColumnSortTypeWrapper.isAscending(this.column) ? 180.0d : 0.0d);
                return;
            }
            return;
        }
        if ("TABLE_COLUMN_SORT_NODE".equals(p2)) {
            updateSortGrid();
            return;
        }
        if ("TABLE_COLUMN_SORTABLE".equals(p2)) {
            if (this.skin.getSortOrder().contains(getTableColumn())) {
                NestedTableColumnHeader root = getTableHeaderRow().getRootHeader();
                updateAllHeaders(root);
                return;
            }
            return;
        }
        if ("TABLE_COLUMN_TEXT".equals(p2)) {
            this.label.setText(this.column.getText());
        } else if ("TABLE_COLUMN_GRAPHIC".equals(p2)) {
            this.label.setGraphic(this.column.getGraphic());
        }
    }

    protected TableViewSkinBase<?, ?, ?, ?, ?, TableColumnBase<?, ?>> getTableViewSkin() {
        return this.skin;
    }

    NestedTableColumnHeader getNestedColumnHeader() {
        return this.nestedColumnHeader;
    }

    void setNestedColumnHeader(NestedTableColumnHeader nch) {
        this.nestedColumnHeader = nch;
    }

    public TableColumnBase getTableColumn() {
        return this.column;
    }

    TableHeaderRow getTableHeaderRow() {
        return this.tableHeaderRow;
    }

    void setTableHeaderRow(TableHeaderRow thr) {
        this.tableHeaderRow = thr;
    }

    NestedTableColumnHeader getParentHeader() {
        return this.parentHeader;
    }

    void setParentHeader(NestedTableColumnHeader ph) {
        this.parentHeader = ph;
    }

    @Override // javafx.scene.Parent
    protected void layoutChildren() {
        if (this.isSizeDirty) {
            resize(getTableColumn().getWidth(), getHeight());
            this.isSizeDirty = false;
        }
        double sortWidth = 0.0d;
        double w2 = snapSize(getWidth()) - (snappedLeftInset() + snappedRightInset());
        double h2 = getHeight() - (snappedTopInset() + snappedBottomInset());
        if (this.arrow != null) {
            this.arrow.setMaxSize(this.arrow.prefWidth(-1.0d), this.arrow.prefHeight(-1.0d));
        }
        if (this.sortArrow != null && this.sortArrow.isVisible()) {
            sortWidth = this.sortArrow.prefWidth(-1.0d);
            double x2 = w2 - sortWidth;
            this.sortArrow.resize(sortWidth, this.sortArrow.prefHeight(-1.0d));
            positionInArea(this.sortArrow, x2, snappedTopInset(), sortWidth, h2, 0.0d, HPos.CENTER, VPos.CENTER);
        }
        if (this.label != null) {
            double labelWidth = w2 - sortWidth;
            this.label.resizeRelocate(snappedLeftInset(), 0.0d, labelWidth, getHeight());
        }
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computePrefWidth(double height) {
        if (getNestedColumnHeader() != null) {
            double width = getNestedColumnHeader().prefWidth(height);
            if (this.column != null) {
                this.column.impl_setWidth(width);
            }
            return width;
        }
        if (this.column != null && this.column.isVisible()) {
            return this.column.getWidth();
        }
        return 0.0d;
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computeMinHeight(double width) {
        if (this.label == null) {
            return 0.0d;
        }
        return this.label.minHeight(width);
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computePrefHeight(double width) {
        if (getTableColumn() == null) {
            return 0.0d;
        }
        return Math.max(getSize(), this.label.prefHeight(-1.0d));
    }

    private void updateAllHeaders(TableColumnHeader header) {
        if (header instanceof NestedTableColumnHeader) {
            List<TableColumnHeader> children = ((NestedTableColumnHeader) header).getColumnHeaders();
            for (int i2 = 0; i2 < children.size(); i2++) {
                updateAllHeaders(children.get(i2));
            }
            return;
        }
        header.updateSortPosition();
    }

    private void updateStyleClass() {
        getStyleClass().setAll("column-header");
        getStyleClass().addAll(this.column.getStyleClass());
    }

    private void updateScene() {
        if (this.autoSizeComplete || getTableColumn() == null || getTableColumn().getWidth() != DEFAULT_COLUMN_WIDTH || getScene() == null) {
            return;
        }
        doColumnAutoSize(getTableColumn(), 30);
        this.autoSizeComplete = true;
    }

    void dispose() {
        TableViewSkinBase skin = getTableViewSkin();
        if (skin != null) {
            skin.getVisibleLeafColumns().removeListener(this.weakVisibleLeafColumnsListener);
            skin.getSortOrder().removeListener(this.weakSortOrderListener);
        }
        this.changeListenerHandler.dispose();
    }

    private boolean isSortingEnabled() {
        return true;
    }

    private boolean isColumnReorderingEnabled() {
        return !BehaviorSkinBase.IS_TOUCH_SUPPORTED && getTableViewSkin().getVisibleLeafColumns().size() > 1;
    }

    private void initUI() {
        if (this.column == null) {
            return;
        }
        setOnMousePressed(mousePressedHandler);
        setOnMouseDragged(mouseDraggedHandler);
        setOnDragDetected(event -> {
            event.consume();
        });
        setOnContextMenuRequested(contextMenuRequestedHandler);
        setOnMouseReleased(mouseReleasedHandler);
        this.label = new Label();
        this.label.setText(this.column.getText());
        this.label.setGraphic(this.column.getGraphic());
        this.label.setVisible(this.column.isVisible());
        if (isSortingEnabled()) {
            updateSortGrid();
        }
    }

    private void doColumnAutoSize(TableColumnBase<?, ?> column, int cellsToMeasure) {
        double prefWidth = column.getPrefWidth();
        if (prefWidth == DEFAULT_COLUMN_WIDTH) {
            getTableViewSkin().resizeColumnToFitContent(column, cellsToMeasure);
        }
    }

    private void updateSortPosition() {
        this.sortPos = !this.column.isSortable() ? -1 : getSortPosition();
        updateSortGrid();
    }

    private void updateSortGrid() {
        Node _sortArrow;
        if (this instanceof NestedTableColumnHeader) {
            return;
        }
        getChildren().clear();
        getChildren().add(this.label);
        if (isSortingEnabled()) {
            this.isSortColumn = this.sortPos != -1;
            if (!this.isSortColumn) {
                if (this.sortArrow != null) {
                    this.sortArrow.setVisible(false);
                    return;
                }
                return;
            }
            int visibleLeafIndex = this.skin.getVisibleLeafIndex(getTableColumn());
            if (visibleLeafIndex == -1) {
                return;
            }
            int sortColumnCount = getVisibleSortOrderColumnCount();
            boolean showSortOrderDots = this.sortPos <= 3 && sortColumnCount > 1;
            if (getTableColumn().getSortNode() != null) {
                _sortArrow = getTableColumn().getSortNode();
                getChildren().add(_sortArrow);
            } else {
                GridPane sortArrowGrid = new GridPane();
                _sortArrow = sortArrowGrid;
                sortArrowGrid.setPadding(new Insets(0.0d, 3.0d, 0.0d, 0.0d));
                getChildren().add(sortArrowGrid);
                if (this.arrow == null) {
                    this.arrow = new Region();
                    this.arrow.getStyleClass().setAll("arrow");
                    this.arrow.setVisible(true);
                    this.arrow.setRotate(TableColumnSortTypeWrapper.isAscending(this.column) ? 180.0d : 0.0d);
                    this.changeListenerHandler.registerChangeListener(TableColumnSortTypeWrapper.getSortTypeProperty(this.column), "TABLE_COLUMN_SORT_TYPE");
                }
                this.arrow.setVisible(this.isSortColumn);
                if (this.sortPos > 2) {
                    if (this.sortOrderLabel == null) {
                        this.sortOrderLabel = new Label();
                        this.sortOrderLabel.getStyleClass().add("sort-order");
                    }
                    this.sortOrderLabel.setText("" + (this.sortPos + 1));
                    this.sortOrderLabel.setVisible(sortColumnCount > 1);
                    sortArrowGrid.add(this.arrow, 1, 1);
                    GridPane.setHgrow(this.arrow, Priority.NEVER);
                    GridPane.setVgrow(this.arrow, Priority.NEVER);
                    sortArrowGrid.add(this.sortOrderLabel, 2, 1);
                } else if (showSortOrderDots) {
                    if (this.sortOrderDots == null) {
                        this.sortOrderDots = new HBox(0.0d);
                        this.sortOrderDots.getStyleClass().add("sort-order-dots-container");
                    }
                    boolean isAscending = TableColumnSortTypeWrapper.isAscending(this.column);
                    int arrowRow = isAscending ? 1 : 2;
                    int dotsRow = isAscending ? 2 : 1;
                    sortArrowGrid.add(this.arrow, 1, arrowRow);
                    GridPane.setHalignment(this.arrow, HPos.CENTER);
                    sortArrowGrid.add(this.sortOrderDots, 1, dotsRow);
                    updateSortOrderDots(this.sortPos);
                } else {
                    sortArrowGrid.add(this.arrow, 1, 1);
                    GridPane.setHgrow(this.arrow, Priority.NEVER);
                    GridPane.setVgrow(this.arrow, Priority.ALWAYS);
                }
            }
            this.sortArrow = _sortArrow;
            if (this.sortArrow != null) {
                this.sortArrow.setVisible(this.isSortColumn);
            }
            requestLayout();
        }
    }

    private void updateSortOrderDots(int sortPos) {
        double arrowWidth = this.arrow.prefWidth(-1.0d);
        this.sortOrderDots.getChildren().clear();
        for (int i2 = 0; i2 <= sortPos; i2++) {
            Region r2 = new Region();
            r2.getStyleClass().add("sort-order-dot");
            String sortTypeName = TableColumnSortTypeWrapper.getSortTypeName(this.column);
            if (sortTypeName != null && !sortTypeName.isEmpty()) {
                r2.getStyleClass().add(sortTypeName.toLowerCase(Locale.ROOT));
            }
            this.sortOrderDots.getChildren().add(r2);
            if (i2 < sortPos) {
                Region spacer = new Region();
                double rp = sortPos == 1 ? 1.0d : 1.0d;
                double lp = sortPos == 1 ? 1.0d : 0.0d;
                spacer.setPadding(new Insets(0.0d, rp, 0.0d, lp));
                this.sortOrderDots.getChildren().add(spacer);
            }
        }
        this.sortOrderDots.setAlignment(Pos.TOP_CENTER);
        this.sortOrderDots.setMaxWidth(arrowWidth);
    }

    void moveColumn(TableColumnBase column, int newColumnPos) {
        if (column == null || newColumnPos < 0) {
            return;
        }
        ObservableList<TableColumnBase<?, ?>> columns = getColumns(column);
        int columnsCount = columns.size();
        int currentPos = columns.indexOf(column);
        int actualNewColumnPos = newColumnPos;
        for (int i2 = 0; i2 <= actualNewColumnPos && i2 < columnsCount; i2++) {
            actualNewColumnPos += columns.get(i2).isVisible() ? 0 : 1;
        }
        if (actualNewColumnPos >= columnsCount) {
            actualNewColumnPos = columnsCount - 1;
        } else if (actualNewColumnPos < 0) {
            actualNewColumnPos = 0;
        }
        if (actualNewColumnPos == currentPos) {
            return;
        }
        List<TableColumnBase<?, ?>> tempList = new ArrayList<>(columns);
        tempList.remove(column);
        tempList.add(actualNewColumnPos, column);
        columns.setAll(tempList);
    }

    private ObservableList<TableColumnBase<?, ?>> getColumns(TableColumnBase column) {
        if (column.getParentColumn() == null) {
            return getTableViewSkin().getColumns();
        }
        return column.getParentColumn().getColumns();
    }

    private int getIndex(TableColumnBase<?, ?> column) {
        if (column == null) {
            return -1;
        }
        ObservableList<? extends TableColumnBase<?, ?>> columns = getColumns(column);
        int index = -1;
        for (int i2 = 0; i2 < columns.size(); i2++) {
            TableColumnBase<?, ?> _column = columns.get(i2);
            if (_column.isVisible()) {
                index++;
                if (column.equals(_column)) {
                    break;
                }
            }
        }
        return index;
    }

    private void updateColumnIndex() {
        TableViewSkinBase skin = getTableViewSkin();
        TableColumnBase tc = getTableColumn();
        this.columnIndex = (skin == null || tc == null) ? -1 : skin.getVisibleLeafIndex(tc);
        this.isLastVisibleColumn = (getTableColumn() == null || this.columnIndex == -1 || this.columnIndex != getTableViewSkin().getVisibleLeafColumns().size() - 1) ? false : true;
        pseudoClassStateChanged(PSEUDO_CLASS_LAST_VISIBLE, this.isLastVisibleColumn);
    }

    private void sortColumn(boolean addColumn) {
        if (isSortingEnabled() && this.column != null && this.column.getColumns().size() == 0 && this.column.getComparator() != null && this.column.isSortable()) {
            ObservableList<TC> sortOrder = getTableViewSkin().getSortOrder();
            if (addColumn) {
                if (!this.isSortColumn) {
                    TableColumnSortTypeWrapper.setSortType(this.column, TableColumn.SortType.ASCENDING);
                    sortOrder.add(this.column);
                    return;
                } else {
                    if (TableColumnSortTypeWrapper.isAscending(this.column)) {
                        TableColumnSortTypeWrapper.setSortType(this.column, TableColumn.SortType.DESCENDING);
                        return;
                    }
                    int i2 = sortOrder.indexOf(this.column);
                    if (i2 != -1) {
                        sortOrder.remove(i2);
                        return;
                    }
                    return;
                }
            }
            if (this.isSortColumn && sortOrder.size() == 1) {
                if (TableColumnSortTypeWrapper.isAscending(this.column)) {
                    TableColumnSortTypeWrapper.setSortType(this.column, TableColumn.SortType.DESCENDING);
                    return;
                } else {
                    sortOrder.remove(this.column);
                    return;
                }
            }
            if (this.isSortColumn) {
                if (TableColumnSortTypeWrapper.isAscending(this.column)) {
                    TableColumnSortTypeWrapper.setSortType(this.column, TableColumn.SortType.DESCENDING);
                } else if (TableColumnSortTypeWrapper.isDescending(this.column)) {
                    TableColumnSortTypeWrapper.setSortType(this.column, TableColumn.SortType.ASCENDING);
                }
                List<TableColumnBase<?, ?>> sortOrderCopy = new ArrayList<>(sortOrder);
                sortOrderCopy.remove(this.column);
                sortOrderCopy.add(0, this.column);
                sortOrder.setAll((TC[]) new TableColumnBase[]{this.column});
                return;
            }
            TableColumnSortTypeWrapper.setSortType(this.column, TableColumn.SortType.ASCENDING);
            sortOrder.setAll((TC[]) new TableColumnBase[]{this.column});
        }
    }

    private int getSortPosition() {
        if (this.column == null) {
            return -1;
        }
        List<TableColumnBase> sortOrder = getVisibleSortOrderColumns();
        int pos = 0;
        for (int i2 = 0; i2 < sortOrder.size(); i2++) {
            TableColumnBase _tc = sortOrder.get(i2);
            if (this.column.equals(_tc)) {
                return pos;
            }
            pos++;
        }
        return -1;
    }

    private List<TableColumnBase> getVisibleSortOrderColumns() {
        List sortOrder = getTableViewSkin().getSortOrder();
        List<TableColumnBase> visibleSortOrderColumns = new ArrayList<>();
        for (int i2 = 0; i2 < sortOrder.size(); i2++) {
            TableColumnBase _tc = (TableColumnBase) sortOrder.get(i2);
            if (_tc != null && _tc.isSortable() && _tc.isVisible()) {
                visibleSortOrderColumns.add(_tc);
            }
        }
        return visibleSortOrderColumns;
    }

    private int getVisibleSortOrderColumnCount() {
        return getVisibleSortOrderColumns().size();
    }

    void columnReorderingStarted(double dragOffset) {
        if (this.column.impl_isReorderable()) {
            this.dragOffset = dragOffset;
            getTableHeaderRow().setReorderingColumn(this.column);
            getTableHeaderRow().setReorderingRegion(this);
        }
    }

    /* JADX WARN: Type inference failed for: r0v12, types: [javafx.scene.control.Control] */
    /* JADX WARN: Type inference failed for: r1v26, types: [javafx.scene.control.Control] */
    void columnReordering(double sceneX, double sceneY) {
        if (this.column.impl_isReorderable()) {
            getTableHeaderRow().setReordering(true);
            TableColumnHeader hoverHeader = null;
            double x2 = getParentHeader().sceneToLocal(sceneX, sceneY).getX();
            double dragX = getTableViewSkin().getSkinnable().sceneToLocal(sceneX, sceneY).getX() - this.dragOffset;
            getTableHeaderRow().setDragHeaderX(dragX);
            double startX = 0.0d;
            double endX = 0.0d;
            double headersWidth = 0.0d;
            this.newColumnPos = 0;
            Iterator<TableColumnHeader> it = getParentHeader().getColumnHeaders().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                TableColumnHeader header = it.next();
                if (header.isVisible()) {
                    double headerWidth = header.prefWidth(-1.0d);
                    headersWidth += headerWidth;
                    startX = header.getBoundsInParent().getMinX();
                    endX = startX + headerWidth;
                    if (x2 >= startX && x2 < endX) {
                        hoverHeader = header;
                        break;
                    }
                    this.newColumnPos++;
                }
            }
            if (hoverHeader == null) {
                this.newColumnPos = x2 > headersWidth ? getParentHeader().getColumns().size() - 1 : 0;
                return;
            }
            double midPoint = startX + ((endX - startX) / 2.0d);
            boolean beforeMidPoint = x2 <= midPoint;
            int currentPos = getIndex(this.column);
            this.newColumnPos += (this.newColumnPos <= currentPos || !beforeMidPoint) ? (this.newColumnPos >= currentPos || beforeMidPoint) ? 0 : 1 : -1;
            double lineX = getTableHeaderRow().sceneToLocal(hoverHeader.localToScene(hoverHeader.getBoundsInLocal())).getMinX() + (beforeMidPoint ? 0.0d : hoverHeader.getWidth());
            if (lineX >= -0.5d && lineX <= getTableViewSkin().getSkinnable().getWidth()) {
                this.columnReorderLine.setTranslateX(lineX);
                this.columnReorderLine.setVisible(true);
            }
            getTableHeaderRow().setReordering(true);
        }
    }

    void columnReorderingComplete() {
        if (this.column.impl_isReorderable()) {
            moveColumn(getTableColumn(), this.newColumnPos);
            this.columnReorderLine.setTranslateX(0.0d);
            this.columnReorderLine.setLayoutX(0.0d);
            this.newColumnPos = 0;
            getTableHeaderRow().setReordering(false);
            this.columnReorderLine.setVisible(false);
            getTableHeaderRow().setReorderingColumn(null);
            getTableHeaderRow().setReorderingRegion(null);
            this.dragOffset = 0.0d;
        }
    }

    double getDragRectHeight() {
        return getHeight();
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/TableColumnHeader$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<TableColumnHeader, Number> SIZE = new CssMetaData<TableColumnHeader, Number>("-fx-size", SizeConverter.getInstance(), Double.valueOf(20.0d)) { // from class: com.sun.javafx.scene.control.skin.TableColumnHeader.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(TableColumnHeader n2) {
                return n2.size == null || !n2.size.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(TableColumnHeader n2) {
                return (StyleableProperty) n2.sizeProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Region.getClassCssMetaData());
            styleables.add(SIZE);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Node, javafx.css.Styleable
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }

    @Override // javafx.scene.Parent, javafx.scene.Node
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case INDEX:
                return Integer.valueOf(getIndex(this.column));
            case TEXT:
                if (this.column != null) {
                    return this.column.getText();
                }
                return null;
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }
}
