package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.SpinnerBehavior;
import com.sun.javafx.scene.control.skin.ComboBoxPopupControl;
import com.sun.javafx.scene.traversal.Algorithm;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.ParentTraversalEngine;
import com.sun.javafx.scene.traversal.TraversalContext;
import java.util.List;
import javafx.css.PseudoClass;
import javafx.event.EventTarget;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/SpinnerSkin.class */
public class SpinnerSkin<T> extends BehaviorSkinBase<Spinner<T>, SpinnerBehavior<T>> {
    private TextField textField;
    private Region incrementArrow;
    private StackPane incrementArrowButton;
    private Region decrementArrow;
    private StackPane decrementArrowButton;
    private static final int ARROWS_ON_RIGHT_VERTICAL = 0;
    private static final int ARROWS_ON_LEFT_VERTICAL = 1;
    private static final int ARROWS_ON_RIGHT_HORIZONTAL = 2;
    private static final int ARROWS_ON_LEFT_HORIZONTAL = 3;
    private static final int SPLIT_ARROWS_VERTICAL = 4;
    private static final int SPLIT_ARROWS_HORIZONTAL = 5;
    private int layoutMode;
    private static PseudoClass CONTAINS_FOCUS_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("contains-focus");

    public SpinnerSkin(Spinner<T> spinner) {
        super(spinner, new SpinnerBehavior(spinner));
        this.layoutMode = 0;
        this.textField = spinner.getEditor();
        getChildren().add(this.textField);
        updateStyleClass();
        spinner.getStyleClass().addListener(c2 -> {
            updateStyleClass();
        });
        this.incrementArrow = new Region();
        this.incrementArrow.setFocusTraversable(false);
        this.incrementArrow.getStyleClass().setAll("increment-arrow");
        this.incrementArrow.setMaxWidth(Double.NEGATIVE_INFINITY);
        this.incrementArrow.setMaxHeight(Double.NEGATIVE_INFINITY);
        this.incrementArrow.setMouseTransparent(true);
        this.incrementArrowButton = new StackPane() { // from class: com.sun.javafx.scene.control.skin.SpinnerSkin.1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // javafx.scene.Node
            public void executeAccessibleAction(AccessibleAction action, Object... parameters) {
                switch (AnonymousClass4.$SwitchMap$javafx$scene$AccessibleAction[action.ordinal()]) {
                    case 1:
                        ((Spinner) SpinnerSkin.this.getSkinnable()).increment();
                        break;
                }
                super.executeAccessibleAction(action, parameters);
            }
        };
        this.incrementArrowButton.setAccessibleRole(AccessibleRole.INCREMENT_BUTTON);
        this.incrementArrowButton.setFocusTraversable(false);
        this.incrementArrowButton.getStyleClass().setAll("increment-arrow-button");
        this.incrementArrowButton.getChildren().add(this.incrementArrow);
        this.incrementArrowButton.setOnMousePressed(e2 -> {
            ((Spinner) getSkinnable()).requestFocus();
            getBehavior().startSpinning(true);
        });
        this.incrementArrowButton.setOnMouseReleased(e3 -> {
            getBehavior().stopSpinning();
        });
        this.decrementArrow = new Region();
        this.decrementArrow.setFocusTraversable(false);
        this.decrementArrow.getStyleClass().setAll("decrement-arrow");
        this.decrementArrow.setMaxWidth(Double.NEGATIVE_INFINITY);
        this.decrementArrow.setMaxHeight(Double.NEGATIVE_INFINITY);
        this.decrementArrow.setMouseTransparent(true);
        this.decrementArrowButton = new StackPane() { // from class: com.sun.javafx.scene.control.skin.SpinnerSkin.2
            /* JADX WARN: Multi-variable type inference failed */
            @Override // javafx.scene.Node
            public void executeAccessibleAction(AccessibleAction action, Object... parameters) {
                switch (AnonymousClass4.$SwitchMap$javafx$scene$AccessibleAction[action.ordinal()]) {
                    case 1:
                        ((Spinner) SpinnerSkin.this.getSkinnable()).decrement();
                        break;
                }
                super.executeAccessibleAction(action, parameters);
            }
        };
        this.decrementArrowButton.setAccessibleRole(AccessibleRole.DECREMENT_BUTTON);
        this.decrementArrowButton.setFocusTraversable(false);
        this.decrementArrowButton.getStyleClass().setAll("decrement-arrow-button");
        this.decrementArrowButton.getChildren().add(this.decrementArrow);
        this.decrementArrowButton.setOnMousePressed(e4 -> {
            ((Spinner) getSkinnable()).requestFocus();
            getBehavior().startSpinning(false);
        });
        this.decrementArrowButton.setOnMouseReleased(e5 -> {
            getBehavior().stopSpinning();
        });
        getChildren().addAll(this.incrementArrowButton, this.decrementArrowButton);
        spinner.focusedProperty().addListener((ov, t2, hasFocus) -> {
            ((ComboBoxPopupControl.FakeFocusTextField) this.textField).setFakeFocus(hasFocus.booleanValue());
        });
        spinner.addEventFilter(KeyEvent.ANY, ke -> {
            if (!spinner.isEditable() || ke.getTarget().equals(this.textField) || ke.getCode() == KeyCode.ESCAPE || ke.getCode() == KeyCode.F10) {
                return;
            }
            this.textField.fireEvent(ke.copyFor((Object) this.textField, (EventTarget) this.textField));
            ke.consume();
        });
        this.textField.addEventFilter(KeyEvent.ANY, ke2 -> {
            if (!spinner.isEditable()) {
                spinner.fireEvent(ke2.copyFor((Object) spinner, (EventTarget) spinner));
                ke2.consume();
            }
        });
        this.textField.focusedProperty().addListener((ov2, t3, hasFocus2) -> {
            spinner.getProperties().put("FOCUSED", hasFocus2);
            if (!hasFocus2.booleanValue()) {
                pseudoClassStateChanged(CONTAINS_FOCUS_PSEUDOCLASS_STATE, false);
            } else {
                pseudoClassStateChanged(CONTAINS_FOCUS_PSEUDOCLASS_STATE, true);
            }
        });
        this.textField.focusTraversableProperty().bind(spinner.editableProperty());
        spinner.setImpl_traversalEngine(new ParentTraversalEngine(spinner, new Algorithm() { // from class: com.sun.javafx.scene.control.skin.SpinnerSkin.3
            @Override // com.sun.javafx.scene.traversal.Algorithm
            public Node select(Node owner, Direction dir, TraversalContext context) {
                return null;
            }

            @Override // com.sun.javafx.scene.traversal.Algorithm
            public Node selectFirst(TraversalContext context) {
                return null;
            }

            @Override // com.sun.javafx.scene.traversal.Algorithm
            public Node selectLast(TraversalContext context) {
                return null;
            }
        }));
    }

    /* renamed from: com.sun.javafx.scene.control.skin.SpinnerSkin$4, reason: invalid class name */
    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/SpinnerSkin$4.class */
    static /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] $SwitchMap$javafx$scene$AccessibleAction = new int[AccessibleAction.values().length];

        static {
            try {
                $SwitchMap$javafx$scene$AccessibleAction[AccessibleAction.FIRE.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void updateStyleClass() {
        List<String> styleClass = ((Spinner) getSkinnable()).getStyleClass();
        if (styleClass.contains(Spinner.STYLE_CLASS_ARROWS_ON_LEFT_VERTICAL)) {
            this.layoutMode = 1;
            return;
        }
        if (styleClass.contains(Spinner.STYLE_CLASS_ARROWS_ON_LEFT_HORIZONTAL)) {
            this.layoutMode = 3;
            return;
        }
        if (styleClass.contains(Spinner.STYLE_CLASS_ARROWS_ON_RIGHT_HORIZONTAL)) {
            this.layoutMode = 2;
            return;
        }
        if (styleClass.contains(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL)) {
            this.layoutMode = 4;
        } else if (styleClass.contains(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL)) {
            this.layoutMode = 5;
        } else {
            this.layoutMode = 0;
        }
    }

    @Override // javafx.scene.control.SkinBase
    protected void layoutChildren(double x2, double y2, double w2, double h2) {
        double incrementArrowButtonWidth = this.incrementArrowButton.snappedLeftInset() + snapSize(this.incrementArrow.prefWidth(-1.0d)) + this.incrementArrowButton.snappedRightInset();
        double decrementArrowButtonWidth = this.decrementArrowButton.snappedLeftInset() + snapSize(this.decrementArrow.prefWidth(-1.0d)) + this.decrementArrowButton.snappedRightInset();
        double widestArrowButton = Math.max(incrementArrowButtonWidth, decrementArrowButtonWidth);
        if (this.layoutMode == 0 || this.layoutMode == 1) {
            double textFieldStartX = this.layoutMode == 0 ? x2 : x2 + widestArrowButton;
            double buttonStartX = this.layoutMode == 0 ? (x2 + w2) - widestArrowButton : x2;
            double halfHeight = Math.floor(h2 / 2.0d);
            this.textField.resizeRelocate(textFieldStartX, y2, w2 - widestArrowButton, h2);
            this.incrementArrowButton.resize(widestArrowButton, halfHeight);
            positionInArea(this.incrementArrowButton, buttonStartX, y2, widestArrowButton, halfHeight, 0.0d, HPos.CENTER, VPos.CENTER);
            this.decrementArrowButton.resize(widestArrowButton, halfHeight);
            positionInArea(this.decrementArrowButton, buttonStartX, y2 + halfHeight, widestArrowButton, h2 - halfHeight, 0.0d, HPos.CENTER, VPos.BOTTOM);
            return;
        }
        if (this.layoutMode == 2 || this.layoutMode == 3) {
            double totalButtonWidth = incrementArrowButtonWidth + decrementArrowButtonWidth;
            double textFieldStartX2 = this.layoutMode == 2 ? x2 : x2 + totalButtonWidth;
            double buttonStartX2 = this.layoutMode == 2 ? (x2 + w2) - totalButtonWidth : x2;
            this.textField.resizeRelocate(textFieldStartX2, y2, w2 - totalButtonWidth, h2);
            this.decrementArrowButton.resize(decrementArrowButtonWidth, h2);
            positionInArea(this.decrementArrowButton, buttonStartX2, y2, decrementArrowButtonWidth, h2, 0.0d, HPos.CENTER, VPos.CENTER);
            this.incrementArrowButton.resize(incrementArrowButtonWidth, h2);
            positionInArea(this.incrementArrowButton, buttonStartX2 + decrementArrowButtonWidth, y2, incrementArrowButtonWidth, h2, 0.0d, HPos.CENTER, VPos.CENTER);
            return;
        }
        if (this.layoutMode != 4) {
            if (this.layoutMode == 5) {
                this.decrementArrowButton.resize(widestArrowButton, h2);
                positionInArea(this.decrementArrowButton, x2, y2, widestArrowButton, h2, 0.0d, HPos.CENTER, VPos.CENTER);
                this.textField.resizeRelocate(x2 + widestArrowButton, y2, w2 - (2.0d * widestArrowButton), h2);
                this.incrementArrowButton.resize(widestArrowButton, h2);
                positionInArea(this.incrementArrowButton, w2 - widestArrowButton, y2, widestArrowButton, h2, 0.0d, HPos.CENTER, VPos.CENTER);
                return;
            }
            return;
        }
        double incrementArrowButtonHeight = this.incrementArrowButton.snappedTopInset() + snapSize(this.incrementArrow.prefHeight(-1.0d)) + this.incrementArrowButton.snappedBottomInset();
        double decrementArrowButtonHeight = this.decrementArrowButton.snappedTopInset() + snapSize(this.decrementArrow.prefHeight(-1.0d)) + this.decrementArrowButton.snappedBottomInset();
        double tallestArrowButton = Math.max(incrementArrowButtonHeight, decrementArrowButtonHeight);
        this.incrementArrowButton.resize(w2, tallestArrowButton);
        positionInArea(this.incrementArrowButton, x2, y2, w2, tallestArrowButton, 0.0d, HPos.CENTER, VPos.CENTER);
        this.textField.resizeRelocate(x2, y2 + tallestArrowButton, w2, h2 - (2.0d * tallestArrowButton));
        this.decrementArrowButton.resize(w2, tallestArrowButton);
        positionInArea(this.decrementArrowButton, x2, h2 - tallestArrowButton, w2, tallestArrowButton, 0.0d, HPos.CENTER, VPos.CENTER);
    }

    @Override // javafx.scene.control.SkinBase
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
    }

    @Override // javafx.scene.control.SkinBase
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        double textfieldWidth = this.textField.prefWidth(height);
        return leftInset + textfieldWidth + rightInset;
    }

    @Override // javafx.scene.control.SkinBase
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        double ph;
        double textFieldHeight = this.textField.prefHeight(width);
        if (this.layoutMode == 4) {
            ph = topInset + this.incrementArrowButton.prefHeight(width) + textFieldHeight + this.decrementArrowButton.prefHeight(width) + bottomInset;
        } else {
            ph = topInset + textFieldHeight + bottomInset;
        }
        return ph;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return ((Spinner) getSkinnable()).prefWidth(height);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return ((Spinner) getSkinnable()).prefHeight(width);
    }

    @Override // javafx.scene.control.SkinBase
    protected double computeBaselineOffset(double topInset, double rightInset, double bottomInset, double leftInset) {
        return this.textField.getLayoutBounds().getMinY() + this.textField.getLayoutY() + this.textField.getBaselineOffset();
    }
}
