package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.DateCellBehavior;
import javafx.scene.control.DateCell;
import javafx.scene.text.Text;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/DateCellSkin.class */
public class DateCellSkin extends CellSkinBase<DateCell, DateCellBehavior> {
    public DateCellSkin(DateCell control) {
        super(control, new DateCellBehavior(control));
        control.setMaxWidth(Double.MAX_VALUE);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase
    protected void updateChildren() {
        super.updateChildren();
        Text secondaryText = (Text) ((DateCell) getSkinnable()).getProperties().get("DateCell.secondaryText");
        if (secondaryText != null) {
            secondaryText.setManaged(false);
            getChildren().add(secondaryText);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, javafx.scene.control.SkinBase
    protected void layoutChildren(double x2, double y2, double w2, double h2) {
        super.layoutChildren(x2, y2, w2, h2);
        Text secondaryText = (Text) ((DateCell) getSkinnable()).getProperties().get("DateCell.secondaryText");
        if (secondaryText != null) {
            double textX = ((x2 + w2) - rightLabelPadding()) - secondaryText.getLayoutBounds().getWidth();
            double textY = ((y2 + h2) - bottomLabelPadding()) - secondaryText.getLayoutBounds().getHeight();
            secondaryText.relocate(snapPosition(textX), snapPosition(textY));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private double cellSize() {
        double cellSize = getCellSize();
        Text secondaryText = (Text) ((DateCell) getSkinnable()).getProperties().get("DateCell.secondaryText");
        if (secondaryText != null && cellSize == 24.0d) {
            cellSize = 36.0d;
        }
        return cellSize;
    }

    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, javafx.scene.control.SkinBase
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        double pref = super.computePrefWidth(height, topInset, rightInset, bottomInset, leftInset);
        return snapSize(Math.max(pref, cellSize()));
    }

    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, javafx.scene.control.SkinBase
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        double pref = super.computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
        return snapSize(Math.max(pref, cellSize()));
    }
}
