package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ComboBoxBaseBehavior;
import java.util.List;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ComboBoxBaseSkin.class */
public abstract class ComboBoxBaseSkin<T> extends BehaviorSkinBase<ComboBoxBase<T>, ComboBoxBaseBehavior<T>> {
    private Node displayNode;
    protected StackPane arrowButton;
    protected Region arrow;
    private ComboBoxMode mode;

    public abstract Node getDisplayNode();

    public abstract void show();

    public abstract void hide();

    protected final ComboBoxMode getMode() {
        return this.mode;
    }

    protected final void setMode(ComboBoxMode value) {
        this.mode = value;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ComboBoxBaseSkin(ComboBoxBase<T> comboBox, ComboBoxBaseBehavior<T> behavior) {
        super(comboBox, behavior);
        this.mode = ComboBoxMode.COMBOBOX;
        this.arrow = new Region();
        this.arrow.setFocusTraversable(false);
        this.arrow.getStyleClass().setAll("arrow");
        this.arrow.setId("arrow");
        this.arrow.setMaxWidth(Double.NEGATIVE_INFINITY);
        this.arrow.setMaxHeight(Double.NEGATIVE_INFINITY);
        this.arrow.setMouseTransparent(true);
        this.arrowButton = new StackPane();
        this.arrowButton.setFocusTraversable(false);
        this.arrowButton.setId("arrow-button");
        this.arrowButton.getStyleClass().setAll("arrow-button");
        this.arrowButton.getChildren().add(this.arrow);
        if (comboBox.isEditable()) {
            this.arrowButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e2 -> {
                getBehavior().mouseEntered(e2);
            });
            this.arrowButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e3 -> {
                getBehavior().mousePressed(e3);
                e3.consume();
            });
            this.arrowButton.addEventHandler(MouseEvent.MOUSE_RELEASED, e4 -> {
                getBehavior().mouseReleased(e4);
                e4.consume();
            });
            this.arrowButton.addEventHandler(MouseEvent.MOUSE_EXITED, e5 -> {
                getBehavior().mouseExited(e5);
            });
        }
        getChildren().add(this.arrowButton);
        ((ComboBoxBase) getSkinnable()).focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.booleanValue()) {
                focusLost();
            }
        });
        registerChangeListener(comboBox.editableProperty(), "EDITABLE");
        registerChangeListener(comboBox.showingProperty(), "SHOWING");
        registerChangeListener(comboBox.focusedProperty(), "FOCUSED");
        registerChangeListener(comboBox.valueProperty(), "VALUE");
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void focusLost() {
        ((ComboBoxBase) getSkinnable()).hide();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.BehaviorSkinBase
    protected void handleControlPropertyChanged(String p2) {
        super.handleControlPropertyChanged(p2);
        if ("SHOWING".equals(p2)) {
            if (((ComboBoxBase) getSkinnable()).isShowing()) {
                show();
                return;
            } else {
                hide();
                return;
            }
        }
        if ("EDITABLE".equals(p2)) {
            updateDisplayArea();
        } else if ("VALUE".equals(p2)) {
            updateDisplayArea();
        }
    }

    protected void updateDisplayArea() {
        List<Node> children = getChildren();
        Node oldDisplayNode = this.displayNode;
        this.displayNode = getDisplayNode();
        if (oldDisplayNode != null && oldDisplayNode != this.displayNode) {
            children.remove(oldDisplayNode);
        }
        if (this.displayNode != null && !children.contains(this.displayNode)) {
            children.add(this.displayNode);
            this.displayNode.applyCss();
        }
    }

    private boolean isButton() {
        return getMode() == ComboBoxMode.BUTTON;
    }

    @Override // javafx.scene.control.SkinBase
    protected void layoutChildren(double x2, double y2, double w2, double h2) {
        if (this.displayNode == null) {
            updateDisplayArea();
        }
        double arrowWidth = snapSize(this.arrow.prefWidth(-1.0d));
        double arrowButtonWidth = isButton() ? 0.0d : this.arrowButton.snappedLeftInset() + arrowWidth + this.arrowButton.snappedRightInset();
        if (this.displayNode != null) {
            this.displayNode.resizeRelocate(x2, y2, w2 - arrowButtonWidth, h2);
        }
        this.arrowButton.setVisible(!isButton());
        if (!isButton()) {
            this.arrowButton.resize(arrowButtonWidth, h2);
            positionInArea(this.arrowButton, (x2 + w2) - arrowButtonWidth, y2, arrowButtonWidth, h2, 0.0d, HPos.CENTER, VPos.CENTER);
        }
    }

    @Override // javafx.scene.control.SkinBase
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        if (this.displayNode == null) {
            updateDisplayArea();
        }
        double arrowWidth = snapSize(this.arrow.prefWidth(-1.0d));
        double arrowButtonWidth = isButton() ? 0.0d : this.arrowButton.snappedLeftInset() + arrowWidth + this.arrowButton.snappedRightInset();
        double displayNodeWidth = this.displayNode == null ? 0.0d : this.displayNode.prefWidth(height);
        double totalWidth = displayNodeWidth + arrowButtonWidth;
        return leftInset + totalWidth + rightInset;
    }

    @Override // javafx.scene.control.SkinBase
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        double ph;
        if (this.displayNode == null) {
            updateDisplayArea();
        }
        if (this.displayNode == null) {
            double arrowHeight = isButton() ? 0.0d : this.arrowButton.snappedTopInset() + this.arrow.prefHeight(-1.0d) + this.arrowButton.snappedBottomInset();
            ph = Math.max(21.0d, arrowHeight);
        } else {
            ph = this.displayNode.prefHeight(width);
        }
        return topInset + ph + bottomInset;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return ((ComboBoxBase) getSkinnable()).prefWidth(height);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return ((ComboBoxBase) getSkinnable()).prefHeight(width);
    }

    @Override // javafx.scene.control.SkinBase
    protected double computeBaselineOffset(double topInset, double rightInset, double bottomInset, double leftInset) {
        if (this.displayNode == null) {
            updateDisplayArea();
        }
        if (this.displayNode != null) {
            return this.displayNode.getLayoutBounds().getMinY() + this.displayNode.getLayoutY() + this.displayNode.getBaselineOffset();
        }
        return super.computeBaselineOffset(topInset, rightInset, bottomInset, leftInset);
    }
}
