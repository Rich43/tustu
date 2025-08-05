package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.skin.ColorPickerSkin;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/ColorPickerBehavior.class */
public class ColorPickerBehavior extends ComboBoxBaseBehavior<Color> {
    protected static final String OPEN_ACTION = "Open";
    protected static final String CLOSE_ACTION = "Close";
    protected static final List<KeyBinding> COLOR_PICKER_BINDINGS = new ArrayList();

    public ColorPickerBehavior(ColorPicker colorPicker) {
        super(colorPicker, COLOR_PICKER_BINDINGS);
    }

    static {
        COLOR_PICKER_BINDINGS.add(new KeyBinding(KeyCode.ESCAPE, KeyEvent.KEY_PRESSED, CLOSE_ACTION));
        COLOR_PICKER_BINDINGS.add(new KeyBinding(KeyCode.SPACE, KeyEvent.KEY_PRESSED, "Open"));
        COLOR_PICKER_BINDINGS.add(new KeyBinding(KeyCode.ENTER, KeyEvent.KEY_PRESSED, "Open"));
    }

    @Override // com.sun.javafx.scene.control.behavior.ComboBoxBaseBehavior, com.sun.javafx.scene.control.behavior.BehaviorBase
    protected void callAction(String name) {
        if ("Open".equals(name)) {
            show();
        } else if (CLOSE_ACTION.equals(name)) {
            hide();
        } else {
            super.callAction(name);
        }
    }

    @Override // com.sun.javafx.scene.control.behavior.ComboBoxBaseBehavior
    public void onAutoHide() {
        ColorPicker colorPicker = (ColorPicker) getControl();
        ColorPickerSkin cpSkin = (ColorPickerSkin) colorPicker.getSkin();
        cpSkin.syncWithAutoUpdate();
        if (!colorPicker.isShowing()) {
            super.onAutoHide();
        }
    }
}
