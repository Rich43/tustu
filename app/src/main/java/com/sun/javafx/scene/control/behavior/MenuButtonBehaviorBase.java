package com.sun.javafx.scene.control.behavior;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Side;
import javafx.scene.control.MenuButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/MenuButtonBehaviorBase.class */
public abstract class MenuButtonBehaviorBase<C extends MenuButton> extends ButtonBehavior<C> {
    protected static final String OPEN_ACTION = "Open";
    protected static final String CLOSE_ACTION = "Close";
    protected static final List<KeyBinding> BASE_MENU_BUTTON_BINDINGS = new ArrayList();

    public MenuButtonBehaviorBase(C menuButton, List<KeyBinding> bindings) {
        super(menuButton, bindings);
    }

    static {
        BASE_MENU_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.UP, "TraverseUp"));
        BASE_MENU_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "TraverseDown"));
        BASE_MENU_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "TraverseLeft"));
        BASE_MENU_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "TraverseRight"));
        BASE_MENU_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.ESCAPE, KeyEvent.KEY_PRESSED, CLOSE_ACTION));
        BASE_MENU_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.CANCEL, KeyEvent.KEY_PRESSED, CLOSE_ACTION));
    }

    @Override // com.sun.javafx.scene.control.behavior.ButtonBehavior, com.sun.javafx.scene.control.behavior.BehaviorBase
    protected void callAction(String name) {
        MenuButton button = getControl();
        Side popupSide = button.getPopupSide();
        if (CLOSE_ACTION.equals(name)) {
            button.hide();
            return;
        }
        if ("Open".equals(name)) {
            if (button.isShowing()) {
                button.hide();
                return;
            } else {
                button.show();
                return;
            }
        }
        if ((!button.isShowing() && "TraverseUp".equals(name) && popupSide == Side.TOP) || (("TraverseDown".equals(name) && (popupSide == Side.BOTTOM || popupSide == Side.TOP)) || (("TraverseLeft".equals(name) && (popupSide == Side.RIGHT || popupSide == Side.LEFT)) || ("TraverseRight".equals(name) && (popupSide == Side.RIGHT || popupSide == Side.LEFT))))) {
            button.show();
        } else {
            super.callAction(name);
        }
    }

    public void mousePressed(MouseEvent e2, boolean behaveLikeButton) {
        C control = getControl();
        if (behaveLikeButton) {
            if (control.isShowing()) {
                control.hide();
            }
            super.mousePressed(e2);
            return;
        }
        if (!control.isFocused() && control.isFocusTraversable()) {
            control.requestFocus();
        }
        if (control.isShowing()) {
            control.hide();
        } else if (e2.getButton() == MouseButton.PRIMARY) {
            control.show();
        }
    }

    @Override // com.sun.javafx.scene.control.behavior.ButtonBehavior, com.sun.javafx.scene.control.behavior.BehaviorBase
    public void mouseReleased(MouseEvent e2) {
    }

    public void mouseReleased(MouseEvent e2, boolean behaveLikeButton) {
        if (behaveLikeButton) {
            super.mouseReleased(e2);
            return;
        }
        if (getControl().isShowing() && !getControl().contains(e2.getX(), e2.getY())) {
            getControl().hide();
        }
        getControl().disarm();
    }
}
