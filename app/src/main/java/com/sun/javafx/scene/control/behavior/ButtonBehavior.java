package com.sun.javafx.scene.control.behavior;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ButtonBase;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/ButtonBehavior.class */
public class ButtonBehavior<C extends ButtonBase> extends BehaviorBase<C> {
    private boolean keyDown;
    private static final String PRESS_ACTION = "Press";
    private static final String RELEASE_ACTION = "Release";
    protected static final List<KeyBinding> BUTTON_BINDINGS = new ArrayList();

    public ButtonBehavior(C button) {
        super(button, BUTTON_BINDINGS);
    }

    public ButtonBehavior(C button, List<KeyBinding> bindings) {
        super(button, bindings);
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    protected void focusChanged() {
        ButtonBase button = getControl();
        if (this.keyDown && !button.isFocused()) {
            this.keyDown = false;
            button.disarm();
        }
    }

    static {
        BUTTON_BINDINGS.add(new KeyBinding(KeyCode.SPACE, KeyEvent.KEY_PRESSED, PRESS_ACTION));
        BUTTON_BINDINGS.add(new KeyBinding(KeyCode.SPACE, KeyEvent.KEY_RELEASED, RELEASE_ACTION));
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    protected void callAction(String name) {
        if (!getControl().isDisabled()) {
            if (PRESS_ACTION.equals(name)) {
                keyPressed();
            } else if (RELEASE_ACTION.equals(name)) {
                keyReleased();
            } else {
                super.callAction(name);
            }
        }
    }

    private void keyPressed() {
        ButtonBase button = getControl();
        if (!button.isPressed() && !button.isArmed()) {
            this.keyDown = true;
            button.arm();
        }
    }

    private void keyReleased() {
        ButtonBase button = getControl();
        if (this.keyDown) {
            this.keyDown = false;
            if (button.isArmed()) {
                button.disarm();
                button.fire();
            }
        }
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    public void mousePressed(MouseEvent e2) {
        ButtonBase button = getControl();
        super.mousePressed(e2);
        if (!button.isFocused() && button.isFocusTraversable()) {
            button.requestFocus();
        }
        boolean valid = (e2.getButton() != MouseButton.PRIMARY || e2.isMiddleButtonDown() || e2.isSecondaryButtonDown() || e2.isShiftDown() || e2.isControlDown() || e2.isAltDown() || e2.isMetaDown()) ? false : true;
        if (!button.isArmed() && valid) {
            button.arm();
        }
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    public void mouseReleased(MouseEvent e2) {
        ButtonBase button = getControl();
        if (!this.keyDown && button.isArmed()) {
            button.fire();
            button.disarm();
        }
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    public void mouseEntered(MouseEvent e2) {
        ButtonBase button = getControl();
        super.mouseEntered(e2);
        if (!this.keyDown && button.isPressed()) {
            button.arm();
        }
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    public void mouseExited(MouseEvent e2) {
        ButtonBase button = getControl();
        super.mouseExited(e2);
        if (!this.keyDown && button.isArmed()) {
            button.disarm();
        }
    }
}
