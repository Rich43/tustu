package com.sun.javafx.scene.control.behavior;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.MenuButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import sun.security.tools.policytool.ToolWindow;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/MenuButtonBehavior.class */
public class MenuButtonBehavior extends MenuButtonBehaviorBase<MenuButton> {
    protected static final List<KeyBinding> MENU_BUTTON_BINDINGS = new ArrayList();

    public MenuButtonBehavior(MenuButton menuButton) {
        super(menuButton, MENU_BUTTON_BINDINGS);
    }

    static {
        MENU_BUTTON_BINDINGS.addAll(BASE_MENU_BUTTON_BINDINGS);
        MENU_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.SPACE, KeyEvent.KEY_PRESSED, ToolWindow.OPEN_POLICY_FILE));
        MENU_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.ENTER, KeyEvent.KEY_PRESSED, ToolWindow.OPEN_POLICY_FILE));
    }
}
