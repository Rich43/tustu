package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.MenuButtonBehavior;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.MenuButton;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/MenuButtonSkin.class */
public class MenuButtonSkin extends MenuButtonSkinBase<MenuButton, MenuButtonBehavior> {
    static final String AUTOHIDE = "autoHide";

    public MenuButtonSkin(MenuButton menuButton) {
        super(menuButton, new MenuButtonBehavior(menuButton));
        this.popup.setOnAutoHide(new EventHandler<Event>() { // from class: com.sun.javafx.scene.control.skin.MenuButtonSkin.1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // javafx.event.EventHandler
            public void handle(Event t2) {
                MenuButton menuButton2 = (MenuButton) MenuButtonSkin.this.getSkinnable();
                if (!menuButton2.getProperties().containsKey(MenuButtonSkin.AUTOHIDE)) {
                    menuButton2.getProperties().put(MenuButtonSkin.AUTOHIDE, Boolean.TRUE);
                }
            }
        });
        this.popup.setOnShown(event -> {
            ContextMenuContent cmContent = (ContextMenuContent) this.popup.getSkin().getNode();
            if (cmContent != null) {
                cmContent.requestFocus();
            }
        });
        if (menuButton.getOnAction() == null) {
            menuButton.setOnAction(e2 -> {
                menuButton.show();
            });
        }
        this.label.setLabelFor(menuButton);
    }
}
