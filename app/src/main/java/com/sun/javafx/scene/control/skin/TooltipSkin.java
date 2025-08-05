package com.sun.javafx.scene.control.skin;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.control.Tooltip;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/TooltipSkin.class */
public class TooltipSkin implements Skin<Tooltip> {
    private Label tipLabel = new Label();
    private Tooltip tooltip;

    public TooltipSkin(Tooltip t2) {
        this.tooltip = t2;
        this.tipLabel.contentDisplayProperty().bind(t2.contentDisplayProperty());
        this.tipLabel.fontProperty().bind(t2.fontProperty());
        this.tipLabel.graphicProperty().bind(t2.graphicProperty());
        this.tipLabel.graphicTextGapProperty().bind(t2.graphicTextGapProperty());
        this.tipLabel.textAlignmentProperty().bind(t2.textAlignmentProperty());
        this.tipLabel.textOverrunProperty().bind(t2.textOverrunProperty());
        this.tipLabel.textProperty().bind(t2.textProperty());
        this.tipLabel.wrapTextProperty().bind(t2.wrapTextProperty());
        this.tipLabel.minWidthProperty().bind(t2.minWidthProperty());
        this.tipLabel.prefWidthProperty().bind(t2.prefWidthProperty());
        this.tipLabel.maxWidthProperty().bind(t2.maxWidthProperty());
        this.tipLabel.minHeightProperty().bind(t2.minHeightProperty());
        this.tipLabel.prefHeightProperty().bind(t2.prefHeightProperty());
        this.tipLabel.maxHeightProperty().bind(t2.maxHeightProperty());
        this.tipLabel.getStyleClass().setAll(t2.getStyleClass());
        this.tipLabel.setStyle(t2.getStyle());
        this.tipLabel.setId(t2.getId());
    }

    @Override // javafx.scene.control.Skin
    public Tooltip getSkinnable() {
        return this.tooltip;
    }

    @Override // javafx.scene.control.Skin
    public Node getNode() {
        return this.tipLabel;
    }

    @Override // javafx.scene.control.Skin
    public void dispose() {
        this.tooltip = null;
        this.tipLabel = null;
    }
}
