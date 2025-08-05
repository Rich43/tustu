package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ToggleButtonBehavior;
import javafx.scene.control.ToggleButton;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ToggleButtonSkin.class */
public class ToggleButtonSkin extends LabeledSkinBase<ToggleButton, ToggleButtonBehavior<ToggleButton>> {
    public ToggleButtonSkin(ToggleButton toggleButton) {
        super(toggleButton, new ToggleButtonBehavior(toggleButton));
    }
}
