package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ToggleButtonBehavior;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.StackPane;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/RadioButtonSkin.class */
public class RadioButtonSkin extends LabeledSkinBase<RadioButton, ToggleButtonBehavior<RadioButton>> {
    private StackPane radio;

    public RadioButtonSkin(RadioButton radioButton) {
        super(radioButton, new ToggleButtonBehavior(radioButton));
        this.radio = createRadio();
        updateChildren();
    }

    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase
    protected void updateChildren() {
        super.updateChildren();
        if (this.radio != null) {
            getChildren().add(this.radio);
        }
    }

    private static StackPane createRadio() {
        StackPane radio = new StackPane();
        radio.getStyleClass().setAll("radio");
        radio.setSnapToPixel(false);
        StackPane region = new StackPane();
        region.getStyleClass().setAll("dot");
        radio.getChildren().clear();
        radio.getChildren().addAll(region);
        return radio;
    }

    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, javafx.scene.control.SkinBase
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return super.computeMinWidth(height, topInset, rightInset, bottomInset, leftInset) + snapSize(this.radio.minWidth(-1.0d));
    }

    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, javafx.scene.control.SkinBase
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return Math.max(snapSize(super.computeMinHeight(width - this.radio.minWidth(-1.0d), topInset, rightInset, bottomInset, leftInset)), topInset + this.radio.minHeight(-1.0d) + bottomInset);
    }

    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, javafx.scene.control.SkinBase
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return super.computePrefWidth(height, topInset, rightInset, bottomInset, leftInset) + snapSize(this.radio.prefWidth(-1.0d));
    }

    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, javafx.scene.control.SkinBase
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return Math.max(snapSize(super.computePrefHeight(width - this.radio.prefWidth(-1.0d), topInset, rightInset, bottomInset, leftInset)), topInset + this.radio.prefHeight(-1.0d) + bottomInset);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, javafx.scene.control.SkinBase
    protected void layoutChildren(double x2, double y2, double w2, double h2) {
        RadioButton radioButton = (RadioButton) getSkinnable();
        double radioWidth = this.radio.prefWidth(-1.0d);
        double radioHeight = this.radio.prefHeight(-1.0d);
        double computeWidth = Math.max(radioButton.prefWidth(-1.0d), radioButton.minWidth(-1.0d));
        double labelWidth = Math.min(computeWidth - radioWidth, w2 - snapSize(radioWidth));
        double labelHeight = Math.min(radioButton.prefHeight(labelWidth), h2);
        double maxHeight = Math.max(radioHeight, labelHeight);
        double xOffset = Utils.computeXOffset(w2, labelWidth + radioWidth, radioButton.getAlignment().getHpos()) + x2;
        double yOffset = Utils.computeYOffset(h2, maxHeight, radioButton.getAlignment().getVpos()) + y2;
        layoutLabelInArea(xOffset + radioWidth, yOffset, labelWidth, maxHeight, radioButton.getAlignment());
        this.radio.resize(snapSize(radioWidth), snapSize(radioHeight));
        positionInArea(this.radio, xOffset, yOffset, radioWidth, maxHeight, 0.0d, radioButton.getAlignment().getHpos(), radioButton.getAlignment().getVpos());
    }
}
