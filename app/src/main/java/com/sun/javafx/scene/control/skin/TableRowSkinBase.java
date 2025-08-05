package com.sun.javafx.scene.control.skin;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.scene.control.behavior.CellBehaviorBase;
import com.sun.javafx.tk.Toolkit;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import javafx.animation.FadeTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.css.StyleOrigin;
import javafx.css.StyleableObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Control;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.TableColumnBase;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/TableRowSkinBase.class */
public abstract class TableRowSkinBase<T, C extends IndexedCell, B extends CellBehaviorBase<C>, R extends IndexedCell> extends CellSkinBase<C, B> {
    private static boolean IS_STUB_TOOLKIT = Toolkit.getToolkit().toString().contains("StubToolkit");
    private static boolean DO_ANIMATIONS;
    private static final Duration FADE_DURATION;
    static final Map<Control, Double> maxDisclosureWidthMap;
    private static final int DEFAULT_FULL_REFRESH_COUNTER = 100;
    protected WeakHashMap<TableColumnBase, Reference<R>> cellsMap;
    protected final List<R> cells;
    private int fullRefreshCounter;
    protected boolean isDirty;
    protected boolean updateCells;
    private double fixedCellSize;
    private boolean fixedCellSizeEnabled;
    private ListChangeListener<TableColumnBase> visibleLeafColumnsListener;
    private WeakListChangeListener<TableColumnBase> weakVisibleLeafColumnsListener;

    protected abstract ObjectProperty<Node> graphicProperty();

    protected abstract Control getVirtualFlowOwner();

    protected abstract ObservableList<? extends TableColumnBase> getVisibleLeafColumns();

    protected abstract void updateCell(R r2, C c2);

    protected abstract DoubleProperty fixedCellSizeProperty();

    protected abstract boolean isColumnPartiallyOrFullyVisible(TableColumnBase tableColumnBase);

    protected abstract R getCell(TableColumnBase tableColumnBase);

    protected abstract TableColumnBase<T, ?> getTableColumnBase(R r2);

    static {
        DO_ANIMATIONS = (IS_STUB_TOOLKIT || PlatformUtil.isEmbedded()) ? false : true;
        FADE_DURATION = Duration.millis(200.0d);
        maxDisclosureWidthMap = new WeakHashMap();
    }

    public TableRowSkinBase(C control, B behavior) {
        super(control, behavior);
        this.cells = new ArrayList();
        this.fullRefreshCounter = 100;
        this.isDirty = false;
        this.updateCells = false;
        this.visibleLeafColumnsListener = c2 -> {
            this.isDirty = true;
            getSkinnable().requestLayout();
        };
        this.weakVisibleLeafColumnsListener = new WeakListChangeListener<>(this.visibleLeafColumnsListener);
    }

    protected void init(C control) {
        getSkinnable().setPickOnBounds(false);
        recreateCells();
        updateCells(true);
        getVisibleLeafColumns().addListener(this.weakVisibleLeafColumnsListener);
        control.itemProperty().addListener(o2 -> {
            requestCellUpdate();
        });
        registerChangeListener(control.indexProperty(), "INDEX");
        if (fixedCellSizeProperty() != null) {
            registerChangeListener(fixedCellSizeProperty(), "FIXED_CELL_SIZE");
            this.fixedCellSize = fixedCellSizeProperty().get();
            this.fixedCellSizeEnabled = this.fixedCellSize > 0.0d;
        }
    }

    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, com.sun.javafx.scene.control.skin.BehaviorSkinBase
    protected void handleControlPropertyChanged(String p2) {
        super.handleControlPropertyChanged(p2);
        if ("INDEX".equals(p2)) {
            if (getSkinnable().isEmpty()) {
                requestCellUpdate();
            }
        } else if ("FIXED_CELL_SIZE".equals(p2)) {
            this.fixedCellSize = fixedCellSizeProperty().get();
            this.fixedCellSizeEnabled = this.fixedCellSize > 0.0d;
        }
    }

    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, javafx.scene.control.SkinBase
    protected void layoutChildren(double x2, double y2, double w2, double h2) {
        double height;
        double width;
        checkState();
        if (this.cellsMap.isEmpty()) {
            return;
        }
        ObservableList<? extends TableColumnBase> visibleLeafColumns = getVisibleLeafColumns();
        if (visibleLeafColumns.isEmpty()) {
            super.layoutChildren(x2, y2, w2, h2);
            return;
        }
        C control = getSkinnable();
        double leftMargin = 0.0d;
        double disclosureWidth = 0.0d;
        boolean indentationRequired = isIndentationRequired();
        boolean disclosureVisible = isDisclosureNodeVisible();
        int indentationColumnIndex = 0;
        Node disclosureNode = null;
        if (indentationRequired) {
            TableColumnBase<?, ?> treeColumn = getTreeColumn();
            int indentationColumnIndex2 = treeColumn == null ? 0 : visibleLeafColumns.indexOf(treeColumn);
            indentationColumnIndex = indentationColumnIndex2 < 0 ? 0 : indentationColumnIndex2;
            int indentationLevel = getIndentationLevel(control);
            if (!isShowRoot()) {
                indentationLevel--;
            }
            double indentationPerLevel = getIndentationPerLevel();
            leftMargin = indentationLevel * indentationPerLevel;
            Control c2 = getVirtualFlowOwner();
            double defaultDisclosureWidth = maxDisclosureWidthMap.containsKey(c2) ? maxDisclosureWidthMap.get(c2).doubleValue() : 0.0d;
            disclosureWidth = defaultDisclosureWidth;
            disclosureNode = getDisclosureNode();
            if (disclosureNode != null) {
                disclosureNode.setVisible(disclosureVisible);
                if (disclosureVisible) {
                    disclosureWidth = disclosureNode.prefWidth(h2);
                    if (disclosureWidth > defaultDisclosureWidth) {
                        maxDisclosureWidthMap.put(c2, Double.valueOf(disclosureWidth));
                        VirtualFlow<C> flow = getVirtualFlow();
                        getSkinnable().getIndex();
                        for (int i2 = 0; i2 < flow.cells.size(); i2++) {
                            IndexedCell indexedCell = (IndexedCell) flow.cells.get(i2);
                            if (indexedCell != null && !indexedCell.isEmpty()) {
                                indexedCell.requestLayout();
                                indexedCell.layout();
                            }
                        }
                    }
                }
            }
        }
        double verticalPadding = snappedTopInset() + snappedBottomInset();
        double horizontalPadding = snappedLeftInset() + snappedRightInset();
        double controlHeight = control.getHeight();
        int index = control.getIndex();
        if (index < 0) {
            return;
        }
        int max = this.cells.size();
        for (int column = 0; column < max; column++) {
            R tableCell = this.cells.get(column);
            TableColumnBase<T, ?> tableColumn = getTableColumnBase(tableCell);
            boolean isVisible = true;
            if (this.fixedCellSizeEnabled) {
                isVisible = isColumnPartiallyOrFullyVisible(tableColumn);
                height = this.fixedCellSize;
            } else {
                double height2 = Math.max(controlHeight, tableCell.prefHeight(-1.0d));
                height = snapSize(height2) - snapSize(verticalPadding);
            }
            if (isVisible) {
                if (this.fixedCellSizeEnabled && tableCell.getParent() == null) {
                    getChildren().add(tableCell);
                }
                width = snapSize(tableCell.prefWidth(-1.0d)) - snapSize(horizontalPadding);
                boolean centreContent = h2 <= 24.0d;
                StyleOrigin origin = ((StyleableObjectProperty) tableCell.alignmentProperty()).getStyleOrigin();
                if (!centreContent && origin == null) {
                    tableCell.setAlignment(Pos.TOP_LEFT);
                }
                if (indentationRequired && column == indentationColumnIndex) {
                    if (disclosureVisible) {
                        double ph = disclosureNode.prefHeight(disclosureWidth);
                        if (width > 0.0d && width < disclosureWidth + leftMargin) {
                            fadeOut(disclosureNode);
                        } else {
                            fadeIn(disclosureNode);
                            disclosureNode.resize(disclosureWidth, ph);
                            disclosureNode.relocate(x2 + leftMargin, centreContent ? (h2 / 2.0d) - (ph / 2.0d) : y2 + tableCell.getPadding().getTop());
                            disclosureNode.toFront();
                        }
                    }
                    ObjectProperty<Node> graphicProperty = graphicProperty();
                    Node graphic = graphicProperty == null ? null : graphicProperty.get();
                    if (graphic != null) {
                        double graphicWidth = graphic.prefWidth(-1.0d) + 3.0d;
                        double ph2 = graphic.prefHeight(graphicWidth);
                        if (width > 0.0d && width < disclosureWidth + leftMargin + graphicWidth) {
                            fadeOut(graphic);
                        } else {
                            fadeIn(graphic);
                            graphic.relocate(x2 + leftMargin + disclosureWidth, centreContent ? (h2 / 2.0d) - (ph2 / 2.0d) : y2 + tableCell.getPadding().getTop());
                            graphic.toFront();
                        }
                    }
                }
                tableCell.resize(width, height);
                tableCell.relocate(x2, snappedTopInset());
                tableCell.requestLayout();
            } else {
                if (this.fixedCellSizeEnabled) {
                    getChildren().remove(tableCell);
                }
                width = snapSize(tableCell.prefWidth(-1.0d)) - snapSize(horizontalPadding);
            }
            x2 += width;
        }
    }

    protected int getIndentationLevel(C control) {
        return 0;
    }

    protected double getIndentationPerLevel() {
        return 0.0d;
    }

    protected boolean isIndentationRequired() {
        return false;
    }

    protected TableColumnBase getTreeColumn() {
        return null;
    }

    protected Node getDisclosureNode() {
        return null;
    }

    protected boolean isDisclosureNodeVisible() {
        return false;
    }

    protected boolean isShowRoot() {
        return true;
    }

    protected TableColumnBase<T, ?> getVisibleLeafColumn(int column) {
        List<? extends TableColumnBase> visibleLeafColumns = getVisibleLeafColumns();
        if (column < 0 || column >= visibleLeafColumns.size()) {
            return null;
        }
        return visibleLeafColumns.get(column);
    }

    protected void updateCells(boolean z2) {
        if (z2) {
            if (this.fullRefreshCounter == 0) {
                recreateCells();
            }
            this.fullRefreshCounter--;
        }
        boolean zIsEmpty = this.cells.isEmpty();
        this.cells.clear();
        C skinnable = getSkinnable();
        int index = skinnable.getIndex();
        ObservableList<? extends TableColumnBase> visibleLeafColumns = getVisibleLeafColumns();
        int size = visibleLeafColumns.size();
        for (int i2 = 0; i2 < size; i2++) {
            TableColumnBase tableColumnBase = visibleLeafColumns.get(i2);
            IndexedCell indexedCellCreateCell = null;
            if (this.cellsMap.containsKey(tableColumnBase)) {
                indexedCellCreateCell = this.cellsMap.get(tableColumnBase).get();
                if (indexedCellCreateCell == null) {
                    this.cellsMap.remove(tableColumnBase);
                }
            }
            if (indexedCellCreateCell == null) {
                indexedCellCreateCell = createCell(tableColumnBase);
            }
            updateCell(indexedCellCreateCell, skinnable);
            indexedCellCreateCell.updateIndex(index);
            this.cells.add(indexedCellCreateCell);
        }
        if (this.fixedCellSizeEnabled) {
            return;
        }
        if (z2 || zIsEmpty) {
            getChildren().setAll(this.cells);
        }
    }

    private VirtualFlow<C> getVirtualFlow() {
        Parent skinnable = getSkinnable();
        while (true) {
            Parent p2 = skinnable;
            if (p2 != null) {
                if (p2 instanceof VirtualFlow) {
                    return (VirtualFlow) p2;
                }
                skinnable = p2.getParent();
            } else {
                return null;
            }
        }
    }

    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, javafx.scene.control.SkinBase
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        double prefWidth = 0.0d;
        List<? extends TableColumnBase> visibleLeafColumns = getVisibleLeafColumns();
        int max = visibleLeafColumns.size();
        for (int i2 = 0; i2 < max; i2++) {
            prefWidth += visibleLeafColumns.get(i2).getWidth();
        }
        return prefWidth;
    }

    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, javafx.scene.control.SkinBase
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        if (this.fixedCellSizeEnabled) {
            return this.fixedCellSize;
        }
        checkState();
        if (getCellSize() < 24.0d) {
            return getCellSize();
        }
        double prefHeight = 0.0d;
        int count = this.cells.size();
        for (int i2 = 0; i2 < count; i2++) {
            R tableCell = this.cells.get(i2);
            prefHeight = Math.max(prefHeight, tableCell.prefHeight(-1.0d));
        }
        double ph = Math.max(prefHeight, Math.max(getCellSize(), getSkinnable().minHeight(-1.0d)));
        return ph;
    }

    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, javafx.scene.control.SkinBase
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        if (this.fixedCellSizeEnabled) {
            return this.fixedCellSize;
        }
        checkState();
        if (getCellSize() < 24.0d) {
            return getCellSize();
        }
        double minHeight = 0.0d;
        int count = this.cells.size();
        for (int i2 = 0; i2 < count; i2++) {
            R tableCell = this.cells.get(i2);
            minHeight = Math.max(minHeight, tableCell.minHeight(-1.0d));
        }
        return minHeight;
    }

    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, javafx.scene.control.SkinBase
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        if (this.fixedCellSizeEnabled) {
            return this.fixedCellSize;
        }
        return super.computeMaxHeight(width, topInset, rightInset, bottomInset, leftInset);
    }

    protected final void checkState() {
        if (this.isDirty) {
            updateCells(true);
            this.isDirty = false;
        } else if (this.updateCells) {
            updateCells(false);
            this.updateCells = false;
        }
    }

    private void requestCellUpdate() {
        this.updateCells = true;
        getSkinnable().requestLayout();
        int newIndex = getSkinnable().getIndex();
        int max = this.cells.size();
        for (int i2 = 0; i2 < max; i2++) {
            this.cells.get(i2).updateIndex(newIndex);
        }
    }

    private void recreateCells() {
        if (this.cellsMap != null) {
            Collection<Reference<R>> cells = this.cellsMap.values();
            for (Reference<R> cellRef : cells) {
                R cell = cellRef.get();
                if (cell != null) {
                    cell.updateIndex(-1);
                    cell.getSkin().dispose();
                    cell.setSkin(null);
                }
            }
            this.cellsMap.clear();
        }
        ObservableList<? extends TableColumnBase> columns = getVisibleLeafColumns();
        this.cellsMap = new WeakHashMap<>(columns.size());
        this.fullRefreshCounter = 100;
        getChildren().clear();
        for (TableColumnBase col : columns) {
            if (!this.cellsMap.containsKey(col)) {
                createCell(col);
            }
        }
    }

    private R createCell(TableColumnBase tableColumnBase) {
        R r2 = (R) getCell(tableColumnBase);
        this.cellsMap.put(tableColumnBase, new WeakReference(r2));
        return r2;
    }

    private void fadeOut(Node node) {
        if (node.getOpacity() < 1.0d) {
            return;
        }
        if (!DO_ANIMATIONS) {
            node.setOpacity(0.0d);
            return;
        }
        FadeTransition fader = new FadeTransition(FADE_DURATION, node);
        fader.setToValue(0.0d);
        fader.play();
    }

    private void fadeIn(Node node) {
        if (node.getOpacity() > 0.0d) {
            return;
        }
        if (!DO_ANIMATIONS) {
            node.setOpacity(1.0d);
            return;
        }
        FadeTransition fader = new FadeTransition(FADE_DURATION, node);
        fader.setToValue(1.0d);
        fader.play();
    }
}
