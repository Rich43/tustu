package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.skin.resources.ControlResources;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.WeakListChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/TableHeaderRow.class */
public class TableHeaderRow extends StackPane {
    private static final String MENU_SEPARATOR = ControlResources.getString("TableView.nestedColumnControlMenuSeparator");
    private final VirtualFlow flow;
    private final TableViewSkinBase tableSkin;
    private double scrollX;
    private double tableWidth;
    private Rectangle clip;
    private TableColumnHeader reorderingRegion;
    private StackPane dragHeader;
    private final NestedTableColumnHeader header;
    private Region filler;
    private Pane cornerRegion;
    private ContextMenu columnPopupMenu;
    private Map<TableColumnBase, CheckMenuItem> columnMenuItems = new HashMap();
    private final Label dragHeaderLabel = new Label();
    private BooleanProperty reordering = new SimpleBooleanProperty(this, "reordering", false) { // from class: com.sun.javafx.scene.control.skin.TableHeaderRow.1
        @Override // javafx.beans.property.BooleanPropertyBase
        protected void invalidated() {
            double height;
            TableColumnHeader r2 = TableHeaderRow.this.getReorderingRegion();
            if (r2 != null) {
                if (r2.getNestedColumnHeader() != null) {
                    height = r2.getNestedColumnHeader().getHeight();
                } else {
                    height = TableHeaderRow.this.getReorderingRegion().getHeight();
                }
                double dragHeaderHeight = height;
                TableHeaderRow.this.dragHeader.resize(TableHeaderRow.this.dragHeader.getWidth(), dragHeaderHeight);
                TableHeaderRow.this.dragHeader.setTranslateY(TableHeaderRow.this.getHeight() - dragHeaderHeight);
            }
            TableHeaderRow.this.dragHeader.setVisible(TableHeaderRow.this.isReordering());
        }
    };
    private InvalidationListener tableWidthListener = valueModel -> {
        updateTableWidth();
    };
    private InvalidationListener tablePaddingListener = valueModel -> {
        updateTableWidth();
    };
    private ListChangeListener visibleLeafColumnsListener = new ListChangeListener<TableColumn<?, ?>>() { // from class: com.sun.javafx.scene.control.skin.TableHeaderRow.3
        @Override // javafx.collections.ListChangeListener
        public void onChanged(ListChangeListener.Change<? extends TableColumn<?, ?>> c2) {
            TableHeaderRow.this.header.setHeadersNeedUpdate();
        }
    };
    private final ListChangeListener tableColumnsListener = c2 -> {
        while (c2.next()) {
            updateTableColumnListeners(c2.getAddedSubList(), c2.getRemoved());
        }
    };
    private final InvalidationListener columnTextListener = observable -> {
        TableColumnBase<?, ?> column = (TableColumnBase) ((StringProperty) observable).getBean();
        CheckMenuItem menuItem = this.columnMenuItems.get(column);
        if (menuItem != null) {
            menuItem.setText(getText(column.getText(), column));
        }
    };
    private final WeakInvalidationListener weakTableWidthListener = new WeakInvalidationListener(this.tableWidthListener);
    private final WeakInvalidationListener weakTablePaddingListener = new WeakInvalidationListener(this.tablePaddingListener);
    private final WeakListChangeListener weakVisibleLeafColumnsListener = new WeakListChangeListener(this.visibleLeafColumnsListener);
    private final WeakListChangeListener weakTableColumnsListener = new WeakListChangeListener(this.tableColumnsListener);
    private final WeakInvalidationListener weakColumnTextListener = new WeakInvalidationListener(this.columnTextListener);

    /* JADX WARN: Type inference failed for: r0v29, types: [javafx.scene.control.Control] */
    /* JADX WARN: Type inference failed for: r0v33, types: [javafx.scene.control.Control] */
    public TableHeaderRow(TableViewSkinBase skin) {
        this.tableSkin = skin;
        this.flow = skin.flow;
        getStyleClass().setAll("column-header-background");
        this.clip = new Rectangle();
        this.clip.setSmooth(false);
        this.clip.heightProperty().bind(heightProperty());
        setClip(this.clip);
        updateTableWidth();
        this.tableSkin.getSkinnable().widthProperty().addListener(this.weakTableWidthListener);
        this.tableSkin.getSkinnable().paddingProperty().addListener(this.weakTablePaddingListener);
        skin.getVisibleLeafColumns().addListener(this.weakVisibleLeafColumnsListener);
        this.columnPopupMenu = new ContextMenu();
        updateTableColumnListeners(this.tableSkin.getColumns(), Collections.emptyList());
        this.tableSkin.getVisibleLeafColumns().addListener(this.weakTableColumnsListener);
        this.tableSkin.getColumns().addListener(this.weakTableColumnsListener);
        this.dragHeader = new StackPane();
        this.dragHeader.setVisible(false);
        this.dragHeader.getStyleClass().setAll("column-drag-header");
        this.dragHeader.setManaged(false);
        this.dragHeader.getChildren().add(this.dragHeaderLabel);
        this.header = createRootHeader();
        this.header.setFocusTraversable(false);
        this.header.setTableHeaderRow(this);
        this.filler = new Region();
        this.filler.getStyleClass().setAll("filler");
        setOnMousePressed(e2 -> {
            skin.getSkinnable().requestFocus();
        });
        final StackPane image = new StackPane();
        image.setSnapToPixel(false);
        image.getStyleClass().setAll("show-hide-column-image");
        this.cornerRegion = new StackPane() { // from class: com.sun.javafx.scene.control.skin.TableHeaderRow.2
            @Override // javafx.scene.layout.StackPane, javafx.scene.Parent
            protected void layoutChildren() {
                double imageWidth = image.snappedLeftInset() + image.snappedRightInset();
                double imageHeight = image.snappedTopInset() + image.snappedBottomInset();
                image.resize(imageWidth, imageHeight);
                positionInArea(image, 0.0d, 0.0d, getWidth(), getHeight() - 3.0d, 0.0d, HPos.CENTER, VPos.CENTER);
            }
        };
        this.cornerRegion.getStyleClass().setAll("show-hide-columns-button");
        this.cornerRegion.getChildren().addAll(image);
        this.cornerRegion.setVisible(this.tableSkin.tableMenuButtonVisibleProperty().get());
        this.tableSkin.tableMenuButtonVisibleProperty().addListener(valueModel -> {
            this.cornerRegion.setVisible(this.tableSkin.tableMenuButtonVisibleProperty().get());
            requestLayout();
        });
        this.cornerRegion.setOnMousePressed(me -> {
            this.columnPopupMenu.show(this.cornerRegion, Side.BOTTOM, 0.0d, 0.0d);
            me.consume();
        });
        getChildren().addAll(this.filler, this.header, this.cornerRegion, this.dragHeader);
    }

    /* JADX WARN: Type inference failed for: r0v14, types: [javafx.scene.control.Control] */
    @Override // javafx.scene.layout.StackPane, javafx.scene.Parent
    protected void layoutChildren() {
        double x2 = this.scrollX;
        double headerWidth = snapSize(this.header.prefWidth(-1.0d));
        double prefHeight = (getHeight() - snappedTopInset()) - snappedBottomInset();
        double cornerWidth = snapSize(this.flow.getVbar().prefWidth(-1.0d));
        this.header.resizeRelocate(x2, snappedTopInset(), headerWidth, prefHeight);
        ?? skinnable = this.tableSkin.getSkinnable();
        if (skinnable == 0) {
            return;
        }
        double controlInsets = skinnable.snappedLeftInset() + skinnable.snappedRightInset();
        double fillerWidth = (((this.tableWidth - headerWidth) + this.filler.getInsets().getLeft()) - controlInsets) - (this.tableSkin.tableMenuButtonVisibleProperty().get() ? cornerWidth : 0.0d);
        this.filler.setVisible(fillerWidth > 0.0d);
        if (fillerWidth > 0.0d) {
            this.filler.resizeRelocate(x2 + headerWidth, snappedTopInset(), fillerWidth, prefHeight);
        }
        this.cornerRegion.resizeRelocate(this.tableWidth - cornerWidth, snappedTopInset(), cornerWidth, prefHeight);
    }

    @Override // javafx.scene.layout.StackPane, javafx.scene.layout.Region, javafx.scene.Parent
    protected double computePrefWidth(double height) {
        return this.header.prefWidth(height);
    }

    @Override // javafx.scene.layout.StackPane, javafx.scene.layout.Region, javafx.scene.Parent
    protected double computeMinHeight(double width) {
        return computePrefHeight(width);
    }

    @Override // javafx.scene.layout.StackPane, javafx.scene.layout.Region, javafx.scene.Parent
    protected double computePrefHeight(double width) {
        double headerPrefHeight = this.header.prefHeight(width);
        return snappedTopInset() + (headerPrefHeight == 0.0d ? 24.0d : headerPrefHeight) + snappedBottomInset();
    }

    protected NestedTableColumnHeader createRootHeader() {
        return new NestedTableColumnHeader(this.tableSkin, null);
    }

    protected TableViewSkinBase<?, ?, ?, ?, ?, ?> getTableSkin() {
        return this.tableSkin;
    }

    protected void updateScrollX() {
        this.scrollX = this.flow.getHbar().isVisible() ? -this.flow.getHbar().getValue() : 0.0d;
        requestLayout();
        layout();
    }

    public final void setReordering(boolean value) {
        this.reordering.set(value);
    }

    public final boolean isReordering() {
        return this.reordering.get();
    }

    public final BooleanProperty reorderingProperty() {
        return this.reordering;
    }

    public TableColumnHeader getReorderingRegion() {
        return this.reorderingRegion;
    }

    public void setReorderingColumn(TableColumnBase rc) {
        this.dragHeaderLabel.setText(rc == null ? "" : rc.getText());
    }

    public void setReorderingRegion(TableColumnHeader reorderingRegion) {
        this.reorderingRegion = reorderingRegion;
        if (reorderingRegion != null) {
            this.dragHeader.resize(reorderingRegion.getWidth(), this.dragHeader.getHeight());
        }
    }

    public void setDragHeaderX(double dragHeaderX) {
        this.dragHeader.setTranslateX(dragHeaderX);
    }

    public NestedTableColumnHeader getRootHeader() {
        return this.header;
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [javafx.scene.control.Control] */
    protected void updateTableWidth() {
        ?? skinnable = this.tableSkin.getSkinnable();
        if (skinnable == 0) {
            this.tableWidth = 0.0d;
        } else {
            Insets insets = skinnable.getInsets() == null ? Insets.EMPTY : skinnable.getInsets();
            double padding = snapSize(insets.getLeft()) + snapSize(insets.getRight());
            this.tableWidth = snapSize(skinnable.getWidth()) - padding;
        }
        this.clip.setWidth(this.tableWidth);
    }

    public TableColumnHeader getColumnHeaderFor(TableColumnBase<?, ?> col) {
        if (col == null) {
            return null;
        }
        List<TableColumnBase<?, ?>> columnChain = new ArrayList<>();
        columnChain.add(col);
        TableColumnBase<?, ?> parentColumn = col.getParentColumn();
        while (true) {
            TableColumnBase<?, ?> parent = parentColumn;
            if (parent == null) {
                break;
            }
            columnChain.add(0, parent);
            parentColumn = parent.getParentColumn();
        }
        TableColumnHeader currentHeader = getRootHeader();
        for (int depth = 0; depth < columnChain.size(); depth++) {
            TableColumnBase<?, ?> column = columnChain.get(depth);
            currentHeader = getColumnHeaderFor(column, currentHeader);
        }
        return currentHeader;
    }

    public TableColumnHeader getColumnHeaderFor(TableColumnBase<?, ?> col, TableColumnHeader currentHeader) {
        if (currentHeader instanceof NestedTableColumnHeader) {
            List<TableColumnHeader> headers = ((NestedTableColumnHeader) currentHeader).getColumnHeaders();
            for (int i2 = 0; i2 < headers.size(); i2++) {
                TableColumnHeader header = headers.get(i2);
                if (header.getTableColumn() == col) {
                    return header;
                }
            }
            return null;
        }
        return null;
    }

    private void updateTableColumnListeners(List<? extends TableColumnBase<?, ?>> added, List<? extends TableColumnBase<?, ?>> removed) {
        for (TableColumnBase tc : removed) {
            remove(tc);
        }
        rebuildColumnMenu();
    }

    private void remove(TableColumnBase<?, ?> col) {
        if (col == null) {
            return;
        }
        CheckMenuItem item = this.columnMenuItems.remove(col);
        if (item != null) {
            col.textProperty().removeListener(this.weakColumnTextListener);
            item.selectedProperty().unbindBidirectional(col.visibleProperty());
            this.columnPopupMenu.getItems().remove(item);
        }
        if (!col.getColumns().isEmpty()) {
            for (TableColumnBase tc : col.getColumns()) {
                remove(tc);
            }
        }
    }

    private void rebuildColumnMenu() {
        this.columnPopupMenu.getItems().clear();
        for (TableColumnBase<?, ?> col : getTableSkin().getColumns()) {
            if (col.getColumns().isEmpty()) {
                createMenuItem(col);
            } else {
                List<TableColumnBase<?, ?>> leafColumns = getLeafColumns(col);
                for (TableColumnBase<?, ?> _col : leafColumns) {
                    createMenuItem(_col);
                }
            }
        }
    }

    private List<TableColumnBase<?, ?>> getLeafColumns(TableColumnBase<?, ?> col) {
        List<TableColumnBase<?, ?>> leafColumns = new ArrayList<>();
        for (TableColumnBase<?, ?> _col : col.getColumns()) {
            if (_col.getColumns().isEmpty()) {
                leafColumns.add(_col);
            } else {
                leafColumns.addAll(getLeafColumns(_col));
            }
        }
        return leafColumns;
    }

    private void createMenuItem(TableColumnBase<?, ?> col) {
        CheckMenuItem item = this.columnMenuItems.get(col);
        if (item == null) {
            item = new CheckMenuItem();
            this.columnMenuItems.put(col, item);
        }
        item.setText(getText(col.getText(), col));
        col.textProperty().addListener(this.weakColumnTextListener);
        item.selectedProperty().bindBidirectional(col.visibleProperty());
        this.columnPopupMenu.getItems().add(item);
    }

    private String getText(String text, TableColumnBase col) {
        String s2 = text;
        TableColumnBase parentColumn = col.getParentColumn();
        while (true) {
            TableColumnBase parentCol = parentColumn;
            if (parentCol != null) {
                if (isColumnVisibleInHeader(parentCol, this.tableSkin.getColumns())) {
                    s2 = parentCol.getText() + MENU_SEPARATOR + s2;
                }
                parentColumn = parentCol.getParentColumn();
            } else {
                return s2;
            }
        }
    }

    private boolean isColumnVisibleInHeader(TableColumnBase col, List columns) {
        if (col == null) {
            return false;
        }
        for (int i2 = 0; i2 < columns.size(); i2++) {
            TableColumnBase column = (TableColumnBase) columns.get(i2);
            if (col.equals(column)) {
                return true;
            }
            if (!column.getColumns().isEmpty()) {
                boolean isVisible = isColumnVisibleInHeader(col, column.getColumns());
                if (isVisible) {
                    return true;
                }
            }
        }
        return false;
    }
}
