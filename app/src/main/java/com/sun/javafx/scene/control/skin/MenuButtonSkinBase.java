package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.ControlAcceleratorSupport;
import com.sun.javafx.scene.control.behavior.MenuButtonBehaviorBase;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/MenuButtonSkinBase.class */
public abstract class MenuButtonSkinBase<C extends MenuButton, B extends MenuButtonBehaviorBase<C>> extends BehaviorSkinBase<C, B> {
    protected final LabeledImpl label;
    protected final StackPane arrow;
    protected final StackPane arrowButton;
    protected ContextMenu popup;
    protected boolean behaveLikeButton;
    private ListChangeListener<MenuItem> itemsChangedListener;

    public MenuButtonSkinBase(C control, B behavior) {
        super(control, behavior);
        this.behaveLikeButton = false;
        if (control.getOnMousePressed() == null) {
            control.addEventHandler(MouseEvent.MOUSE_PRESSED, e2 -> {
                getBehavior().mousePressed(e2, this.behaveLikeButton);
            });
        }
        if (control.getOnMouseReleased() == null) {
            control.addEventHandler(MouseEvent.MOUSE_RELEASED, e3 -> {
                getBehavior().mouseReleased(e3, this.behaveLikeButton);
            });
        }
        this.label = new MenuLabeledImpl(getSkinnable());
        this.label.setMnemonicParsing(control.isMnemonicParsing());
        this.label.setLabelFor(control);
        this.arrow = new StackPane();
        this.arrow.getStyleClass().setAll("arrow");
        this.arrow.setMaxWidth(Double.NEGATIVE_INFINITY);
        this.arrow.setMaxHeight(Double.NEGATIVE_INFINITY);
        this.arrowButton = new StackPane();
        this.arrowButton.getStyleClass().setAll("arrow-button");
        this.arrowButton.getChildren().add(this.arrow);
        this.popup = new ContextMenu();
        this.popup.getItems().clear();
        this.popup.getItems().addAll(getSkinnable().getItems());
        getChildren().clear();
        getChildren().addAll(this.label, this.arrowButton);
        getSkinnable().requestLayout();
        this.itemsChangedListener = c2 -> {
            while (c2.next()) {
                this.popup.getItems().removeAll(c2.getRemoved());
                this.popup.getItems().addAll(c2.getFrom(), c2.getAddedSubList());
            }
        };
        control.getItems().addListener(this.itemsChangedListener);
        if (getSkinnable().getScene() != null) {
            ControlAcceleratorSupport.addAcceleratorsIntoScene(getSkinnable().getItems(), (Node) getSkinnable());
        }
        control.sceneProperty().addListener((scene, oldValue, newValue) -> {
            if (getSkinnable() != null && getSkinnable().getScene() != null) {
                ControlAcceleratorSupport.addAcceleratorsIntoScene(getSkinnable().getItems(), (Node) getSkinnable());
            }
        });
        registerChangeListener(control.showingProperty(), "SHOWING");
        registerChangeListener(control.focusedProperty(), "FOCUSED");
        registerChangeListener(control.mnemonicParsingProperty(), "MNEMONIC_PARSING");
        registerChangeListener(this.popup.showingProperty(), "POPUP_VISIBLE");
    }

    @Override // com.sun.javafx.scene.control.skin.BehaviorSkinBase, javafx.scene.control.SkinBase, javafx.scene.control.Skin
    public void dispose() {
        getSkinnable().getItems().removeListener(this.itemsChangedListener);
        super.dispose();
        if (this.popup != null) {
            if (this.popup.getSkin() != null && this.popup.getSkin().getNode() != null) {
                ContextMenuContent cmContent = (ContextMenuContent) this.popup.getSkin().getNode();
                cmContent.dispose();
            }
            this.popup.setSkin(null);
            this.popup = null;
        }
    }

    private void show() {
        if (!this.popup.isShowing()) {
            this.popup.show(getSkinnable(), getSkinnable().getPopupSide(), 0.0d, 0.0d);
        }
    }

    private void hide() {
        if (this.popup.isShowing()) {
            this.popup.hide();
        }
    }

    @Override // com.sun.javafx.scene.control.skin.BehaviorSkinBase
    protected void handleControlPropertyChanged(String p2) {
        super.handleControlPropertyChanged(p2);
        if ("SHOWING".equals(p2)) {
            if (getSkinnable().isShowing()) {
                show();
                return;
            } else {
                hide();
                return;
            }
        }
        if ("FOCUSED".equals(p2)) {
            if (!getSkinnable().isFocused() && getSkinnable().isShowing()) {
                hide();
            }
            if (!getSkinnable().isFocused() && this.popup.isShowing()) {
                hide();
                return;
            }
            return;
        }
        if ("POPUP_VISIBLE".equals(p2)) {
            if (!this.popup.isShowing() && getSkinnable().isShowing()) {
                getSkinnable().hide();
            }
            if (this.popup.isShowing()) {
                Utils.addMnemonics(this.popup, getSkinnable().getScene(), getSkinnable().impl_isShowMnemonics());
                return;
            } else {
                Utils.removeMnemonics(this.popup, getSkinnable().getScene());
                return;
            }
        }
        if ("MNEMONIC_PARSING".equals(p2)) {
            this.label.setMnemonicParsing(getSkinnable().isMnemonicParsing());
            getSkinnable().requestLayout();
        }
    }

    @Override // javafx.scene.control.SkinBase
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return leftInset + this.label.minWidth(height) + snapSize(this.arrowButton.minWidth(height)) + rightInset;
    }

    @Override // javafx.scene.control.SkinBase
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return topInset + Math.max(this.label.minHeight(width), snapSize(this.arrowButton.minHeight(-1.0d))) + bottomInset;
    }

    @Override // javafx.scene.control.SkinBase
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return leftInset + this.label.prefWidth(height) + snapSize(this.arrowButton.prefWidth(height)) + rightInset;
    }

    @Override // javafx.scene.control.SkinBase
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return topInset + Math.max(this.label.prefHeight(width), snapSize(this.arrowButton.prefHeight(-1.0d))) + bottomInset;
    }

    @Override // javafx.scene.control.SkinBase
    protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return getSkinnable().prefWidth(height);
    }

    @Override // javafx.scene.control.SkinBase
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return getSkinnable().prefHeight(width);
    }

    @Override // javafx.scene.control.SkinBase
    protected void layoutChildren(double x2, double y2, double w2, double h2) {
        double arrowButtonWidth = snapSize(this.arrowButton.prefWidth(-1.0d));
        this.label.resizeRelocate(x2, y2, w2 - arrowButtonWidth, h2);
        this.arrowButton.resizeRelocate(x2 + (w2 - arrowButtonWidth), y2, arrowButtonWidth, h2);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/MenuButtonSkinBase$MenuLabeledImpl.class */
    private class MenuLabeledImpl extends LabeledImpl {
        MenuButton button;

        public MenuLabeledImpl(MenuButton b2) {
            super(b2);
            this.button = b2;
            addEventHandler(ActionEvent.ACTION, e2 -> {
                this.button.fireEvent(new ActionEvent());
                e2.consume();
            });
        }
    }
}
