package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ListCellBehavior;
import javafx.geometry.Orientation;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ListCellSkin.class */
public class ListCellSkin<T> extends CellSkinBase<ListCell<T>, ListCellBehavior<T>> {
    private double fixedCellSize;
    private boolean fixedCellSizeEnabled;

    public ListCellSkin(ListCell<T> control) {
        super(control, new ListCellBehavior(control));
        this.fixedCellSize = control.getListView().getFixedCellSize();
        this.fixedCellSizeEnabled = this.fixedCellSize > 0.0d;
        registerChangeListener(control.getListView().fixedCellSizeProperty(), "FIXED_CELL_SIZE");
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, com.sun.javafx.scene.control.skin.BehaviorSkinBase
    protected void handleControlPropertyChanged(String p2) {
        super.handleControlPropertyChanged(p2);
        if ("FIXED_CELL_SIZE".equals(p2)) {
            this.fixedCellSize = ((ListCell) getSkinnable()).getListView().getFixedCellSize();
            this.fixedCellSizeEnabled = this.fixedCellSize > 0.0d;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, javafx.scene.control.SkinBase
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        double pref = super.computePrefWidth(height, topInset, rightInset, bottomInset, leftInset);
        ListView<T> listView = ((ListCell) getSkinnable()).getListView();
        if (listView == null) {
            return 0.0d;
        }
        return listView.getOrientation() == Orientation.VERTICAL ? pref : Math.max(pref, getCellSize());
    }

    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, javafx.scene.control.SkinBase
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        if (this.fixedCellSizeEnabled) {
            return this.fixedCellSize;
        }
        double cellSize = getCellSize();
        double prefHeight = cellSize == 24.0d ? super.computePrefHeight(width, topInset, rightInset, bottomInset, leftInset) : cellSize;
        return prefHeight;
    }

    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, javafx.scene.control.SkinBase
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        if (this.fixedCellSizeEnabled) {
            return this.fixedCellSize;
        }
        return super.computeMinHeight(width, topInset, rightInset, bottomInset, leftInset);
    }

    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, javafx.scene.control.SkinBase
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        if (this.fixedCellSizeEnabled) {
            return this.fixedCellSize;
        }
        return super.computeMaxHeight(width, topInset, rightInset, bottomInset, leftInset);
    }
}
