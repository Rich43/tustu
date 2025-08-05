package com.sun.javafx.scene.control.behavior;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/ToolBarBehavior.class */
public class ToolBarBehavior extends BehaviorBase<ToolBar> {
    private static final String CTRL_F5 = "Ctrl_F5";
    protected static final List<KeyBinding> TOOLBAR_BINDINGS = new ArrayList();

    public ToolBarBehavior(ToolBar toolbar) {
        super(toolbar, TOOLBAR_BINDINGS);
    }

    static {
        TOOLBAR_BINDINGS.add(new KeyBinding(KeyCode.F5, CTRL_F5).ctrl());
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    protected void callAction(String name) {
        if (CTRL_F5.equals(name)) {
            ToolBar toolbar = getControl();
            if (!toolbar.getItems().isEmpty()) {
                toolbar.getItems().get(0).requestFocus();
                return;
            }
            return;
        }
        super.callAction(name);
    }
}
