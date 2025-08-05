package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.CellBehaviorBase;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.control.IndexedCell;
import javafx.scene.shape.Rectangle;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/TableCellSkinBase.class */
public abstract class TableCellSkinBase<C extends IndexedCell, B extends CellBehaviorBase<C>> extends CellSkinBase<C, B> {
    static final String DEFER_TO_PARENT_PREF_WIDTH = "deferToParentPrefWidth";
    boolean isDeferToParentForPrefWidth;
    private InvalidationListener columnWidthListener;
    private WeakInvalidationListener weakColumnWidthListener;

    protected abstract ReadOnlyDoubleProperty columnWidthProperty();

    protected abstract BooleanProperty columnVisibleProperty();

    public TableCellSkinBase(C control, B behavior) {
        super(control, behavior);
        this.isDeferToParentForPrefWidth = false;
        this.columnWidthListener = valueModel -> {
            getSkinnable().requestLayout();
        };
        this.weakColumnWidthListener = new WeakInvalidationListener(this.columnWidthListener);
    }

    protected void init(C control) {
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(control.widthProperty());
        clip.heightProperty().bind(control.heightProperty());
        getSkinnable().setClip(clip);
        ReadOnlyDoubleProperty columnWidthProperty = columnWidthProperty();
        if (columnWidthProperty != null) {
            columnWidthProperty.addListener(this.weakColumnWidthListener);
        }
        registerChangeListener(control.visibleProperty(), "VISIBLE");
        if (control.getProperties().containsKey(DEFER_TO_PARENT_PREF_WIDTH)) {
            this.isDeferToParentForPrefWidth = true;
        }
    }

    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, com.sun.javafx.scene.control.skin.BehaviorSkinBase
    protected void handleControlPropertyChanged(String p2) {
        super.handleControlPropertyChanged(p2);
        if ("VISIBLE".equals(p2)) {
            getSkinnable().setVisible(columnVisibleProperty().get());
        }
    }

    @Override // com.sun.javafx.scene.control.skin.BehaviorSkinBase, javafx.scene.control.SkinBase, javafx.scene.control.Skin
    public void dispose() {
        ReadOnlyDoubleProperty columnWidthProperty = columnWidthProperty();
        if (columnWidthProperty != null) {
            columnWidthProperty.removeListener(this.weakColumnWidthListener);
        }
        super.dispose();
    }

    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, javafx.scene.control.SkinBase
    protected void layoutChildren(double x2, double y2, double w2, double h2) {
        layoutLabelInArea(x2, y2, w2, h2 - getSkinnable().getPadding().getBottom());
    }

    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, javafx.scene.control.SkinBase
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        if (this.isDeferToParentForPrefWidth) {
            return super.computePrefWidth(height, topInset, rightInset, bottomInset, leftInset);
        }
        return columnWidthProperty().get();
    }
}
