package com.sun.javafx.scene.control.behavior;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/SplitMenuButtonBehavior.class */
public class SplitMenuButtonBehavior extends MenuButtonBehaviorBase<SplitMenuButton> {
    protected static final List<KeyBinding> SPLIT_MENU_BUTTON_BINDINGS = new ArrayList();

    public SplitMenuButtonBehavior(SplitMenuButton splitMenuButton) {
        super(splitMenuButton, SPLIT_MENU_BUTTON_BINDINGS);
    }

    static {
        SPLIT_MENU_BUTTON_BINDINGS.addAll(BASE_MENU_BUTTON_BINDINGS);
        SPLIT_MENU_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.SPACE, KeyEvent.KEY_PRESSED, "Press"));
        SPLIT_MENU_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.SPACE, KeyEvent.KEY_RELEASED, "Release"));
        SPLIT_MENU_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.ENTER, KeyEvent.KEY_PRESSED, "Press"));
        SPLIT_MENU_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.ENTER, KeyEvent.KEY_RELEASED, "Release"));
    }
}
