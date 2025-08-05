package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ButtonBehavior;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.StackPane;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/CheckBoxSkin.class */
public class CheckBoxSkin extends LabeledSkinBase<CheckBox, ButtonBehavior<CheckBox>> {
    private final StackPane box;
    private StackPane innerbox;

    public CheckBoxSkin(CheckBox checkbox) {
        super(checkbox, new ButtonBehavior(checkbox));
        this.box = new StackPane();
        this.box.getStyleClass().setAll("box");
        this.innerbox = new StackPane();
        this.innerbox.getStyleClass().setAll("mark");
        this.innerbox.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        this.box.getChildren().add(this.innerbox);
        updateChildren();
    }

    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase
    protected void updateChildren() {
        super.updateChildren();
        if (this.box != null) {
            getChildren().add(this.box);
        }
    }

    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, javafx.scene.control.SkinBase
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return super.computeMinWidth(height, topInset, rightInset, bottomInset, leftInset) + snapSize(this.box.minWidth(-1.0d));
    }

    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, javafx.scene.control.SkinBase
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return Math.max(super.computeMinHeight(width - this.box.minWidth(-1.0d), topInset, rightInset, bottomInset, leftInset), topInset + this.box.minHeight(-1.0d) + bottomInset);
    }

    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, javafx.scene.control.SkinBase
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return super.computePrefWidth(height, topInset, rightInset, bottomInset, leftInset) + snapSize(this.box.prefWidth(-1.0d));
    }

    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, javafx.scene.control.SkinBase
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return Math.max(super.computePrefHeight(width - this.box.prefWidth(-1.0d), topInset, rightInset, bottomInset, leftInset), topInset + this.box.prefHeight(-1.0d) + bottomInset);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, javafx.scene.control.SkinBase
    protected void layoutChildren(double x2, double y2, double w2, double h2) {
        CheckBox checkBox = (CheckBox) getSkinnable();
        double boxWidth = snapSize(this.box.prefWidth(-1.0d));
        double boxHeight = snapSize(this.box.prefHeight(-1.0d));
        double computeWidth = Math.max(checkBox.prefWidth(-1.0d), checkBox.minWidth(-1.0d));
        double labelWidth = Math.min(computeWidth - boxWidth, w2 - snapSize(boxWidth));
        double labelHeight = Math.min(checkBox.prefHeight(labelWidth), h2);
        double maxHeight = Math.max(boxHeight, labelHeight);
        double xOffset = Utils.computeXOffset(w2, labelWidth + boxWidth, checkBox.getAlignment().getHpos()) + x2;
        double yOffset = Utils.computeYOffset(h2, maxHeight, checkBox.getAlignment().getVpos()) + x2;
        layoutLabelInArea(xOffset + boxWidth, yOffset, labelWidth, maxHeight, checkBox.getAlignment());
        this.box.resize(boxWidth, boxHeight);
        positionInArea(this.box, xOffset, yOffset, boxWidth, maxHeight, 0.0d, checkBox.getAlignment().getHpos(), checkBox.getAlignment().getVpos());
    }
}
