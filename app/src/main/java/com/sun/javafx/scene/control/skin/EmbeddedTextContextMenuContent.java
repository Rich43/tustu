package com.sun.javafx.scene.control.skin;

import java.util.Map;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/EmbeddedTextContextMenuContent.class */
public class EmbeddedTextContextMenuContent extends StackPane {
    private ContextMenu contextMenu;
    private HBox menuBox = new HBox();
    private StackPane pointer = new StackPane();

    public EmbeddedTextContextMenuContent(ContextMenu popupMenu) {
        this.contextMenu = popupMenu;
        this.pointer.getStyleClass().add("pointer");
        updateMenuItemContainer();
        getChildren().addAll(this.pointer, this.menuBox);
        this.contextMenu.ownerNodeProperty().addListener(arg0 -> {
            if (this.contextMenu.getOwnerNode() instanceof TextArea) {
                TextAreaSkin tas = (TextAreaSkin) ((TextArea) this.contextMenu.getOwnerNode()).getSkin();
                ((TextArea) tas.getSkinnable()).getProperties().addListener(new InvalidationListener() { // from class: com.sun.javafx.scene.control.skin.EmbeddedTextContextMenuContent.1
                    @Override // javafx.beans.InvalidationListener
                    public void invalidated(Observable arg0) {
                        EmbeddedTextContextMenuContent.this.requestLayout();
                    }
                });
            } else if (this.contextMenu.getOwnerNode() instanceof TextField) {
                TextFieldSkin tfs = (TextFieldSkin) ((TextField) this.contextMenu.getOwnerNode()).getSkin();
                ((TextField) tfs.getSkinnable()).getProperties().addListener(new InvalidationListener() { // from class: com.sun.javafx.scene.control.skin.EmbeddedTextContextMenuContent.2
                    @Override // javafx.beans.InvalidationListener
                    public void invalidated(Observable arg0) {
                        EmbeddedTextContextMenuContent.this.requestLayout();
                    }
                });
            }
        });
        this.contextMenu.getItems().addListener(c2 -> {
            updateMenuItemContainer();
        });
    }

    private void updateMenuItemContainer() {
        this.menuBox.getChildren().clear();
        for (MenuItem item : this.contextMenu.getItems()) {
            MenuItemContainer menuItemContainer = new MenuItemContainer(item);
            menuItemContainer.visibleProperty().bind(item.visibleProperty());
            this.menuBox.getChildren().add(menuItemContainer);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideAllMenus(MenuItem item) {
        Menu parentMenu;
        this.contextMenu.hide();
        while (true) {
            parentMenu = item.getParentMenu();
            if (parentMenu == null) {
                break;
            }
            parentMenu.hide();
            item = parentMenu;
        }
        if (parentMenu == null && item.getParentPopup() != null) {
            item.getParentPopup().hide();
        }
    }

    @Override // javafx.scene.layout.StackPane, javafx.scene.layout.Region, javafx.scene.Parent
    protected double computePrefHeight(double width) {
        double pointerHeight = snapSize(this.pointer.prefHeight(width));
        double menuBoxHeight = snapSize(this.menuBox.prefHeight(width));
        return snappedTopInset() + pointerHeight + menuBoxHeight + snappedBottomInset();
    }

    @Override // javafx.scene.layout.StackPane, javafx.scene.layout.Region, javafx.scene.Parent
    protected double computePrefWidth(double height) {
        double menuBoxWidth = snapSize(this.menuBox.prefWidth(height));
        return snappedLeftInset() + menuBoxWidth + snappedRightInset();
    }

    @Override // javafx.scene.layout.StackPane, javafx.scene.Parent
    protected void layoutChildren() {
        double pointerX;
        double left = snappedLeftInset();
        double right = snappedRightInset();
        double top = snappedTopInset();
        double width = getWidth() - (left + right);
        double pointerWidth = snapSize(Utils.boundedSize(this.pointer.prefWidth(-1.0d), this.pointer.minWidth(-1.0d), this.pointer.maxWidth(-1.0d)));
        double pointerHeight = snapSize(Utils.boundedSize(this.pointer.prefWidth(-1.0d), this.pointer.minWidth(-1.0d), this.pointer.maxWidth(-1.0d)));
        double menuBoxWidth = snapSize(Utils.boundedSize(this.menuBox.prefWidth(-1.0d), this.menuBox.minWidth(-1.0d), this.menuBox.maxWidth(-1.0d)));
        double menuBoxHeight = snapSize(Utils.boundedSize(this.menuBox.prefWidth(-1.0d), this.menuBox.minWidth(-1.0d), this.menuBox.maxWidth(-1.0d)));
        double sceneX = 0.0d;
        double screenX = 0.0d;
        Map<Object, Object> properties = null;
        if (this.contextMenu.getOwnerNode() instanceof TextArea) {
            properties = ((TextArea) this.contextMenu.getOwnerNode()).getProperties();
        } else if (this.contextMenu.getOwnerNode() instanceof TextField) {
            properties = ((TextField) this.contextMenu.getOwnerNode()).getProperties();
        }
        if (properties != null) {
            if (properties.containsKey("CONTEXT_MENU_SCENE_X")) {
                sceneX = Double.valueOf(properties.get("CONTEXT_MENU_SCENE_X").toString()).doubleValue();
                properties.remove("CONTEXT_MENU_SCENE_X");
            }
            if (properties.containsKey("CONTEXT_MENU_SCREEN_X")) {
                screenX = Double.valueOf(properties.get("CONTEXT_MENU_SCREEN_X").toString()).doubleValue();
                properties.remove("CONTEXT_MENU_SCREEN_X");
            }
        }
        if (sceneX == 0.0d) {
            pointerX = width / 2.0d;
        } else {
            pointerX = ((screenX - sceneX) - this.contextMenu.getX()) + sceneX;
        }
        this.pointer.resize(pointerWidth, pointerHeight);
        positionInArea(this.pointer, pointerX, top, pointerWidth, pointerHeight, 0.0d, HPos.CENTER, VPos.CENTER);
        this.menuBox.resize(menuBoxWidth, menuBoxHeight);
        positionInArea(this.menuBox, left, top + pointerHeight, menuBoxWidth, menuBoxHeight, 0.0d, HPos.CENTER, VPos.CENTER);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/EmbeddedTextContextMenuContent$MenuItemContainer.class */
    class MenuItemContainer extends Button {
        private MenuItem item;

        public MenuItemContainer(MenuItem item) {
            getStyleClass().addAll(item.getStyleClass());
            setId(item.getId());
            this.item = item;
            setText(item.getText());
            setStyle(item.getStyle());
            textProperty().bind(item.textProperty());
        }

        public MenuItem getItem() {
            return this.item;
        }

        @Override // javafx.scene.control.Button, javafx.scene.control.ButtonBase
        public void fire() {
            Event.fireEvent(this.item, new ActionEvent());
            if (!Boolean.TRUE.equals((Boolean) this.item.getProperties().get("refreshMenu"))) {
                EmbeddedTextContextMenuContent.this.hideAllMenus(this.item);
            }
        }
    }
}
