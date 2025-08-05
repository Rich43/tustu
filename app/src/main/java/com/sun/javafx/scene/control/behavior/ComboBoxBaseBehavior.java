package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.skin.Utils;
import java.util.ArrayList;
import java.util.List;
import javafx.event.EventTarget;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/ComboBoxBaseBehavior.class */
public class ComboBoxBaseBehavior<T> extends BehaviorBase<ComboBoxBase<T>> {
    private TwoLevelFocusComboBehavior tlFocus;
    private KeyEvent lastEvent;
    private boolean keyDown;
    private static final String PRESS_ACTION = "Press";
    private static final String RELEASE_ACTION = "Release";
    protected static final List<KeyBinding> COMBO_BOX_BASE_BINDINGS = new ArrayList();
    private boolean showPopupOnMouseRelease;
    private boolean mouseInsideButton;

    public ComboBoxBaseBehavior(ComboBoxBase<T> comboBox, List<KeyBinding> bindings) {
        super(comboBox, bindings);
        this.showPopupOnMouseRelease = true;
        this.mouseInsideButton = false;
        if (Utils.isTwoLevelFocus()) {
            this.tlFocus = new TwoLevelFocusComboBehavior(comboBox);
        }
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    public void dispose() {
        if (this.tlFocus != null) {
            this.tlFocus.dispose();
        }
        super.dispose();
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    protected void focusChanged() {
        ComboBoxBase<T> box = getControl();
        if (this.keyDown && !box.isFocused()) {
            this.keyDown = false;
            box.disarm();
        }
    }

    static {
        COMBO_BOX_BASE_BINDINGS.add(new KeyBinding(KeyCode.F4, KeyEvent.KEY_RELEASED, "togglePopup"));
        COMBO_BOX_BASE_BINDINGS.add(new KeyBinding(KeyCode.UP, "togglePopup").alt());
        COMBO_BOX_BASE_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "togglePopup").alt());
        COMBO_BOX_BASE_BINDINGS.add(new KeyBinding(KeyCode.SPACE, KeyEvent.KEY_PRESSED, PRESS_ACTION));
        COMBO_BOX_BASE_BINDINGS.add(new KeyBinding(KeyCode.SPACE, KeyEvent.KEY_RELEASED, RELEASE_ACTION));
        COMBO_BOX_BASE_BINDINGS.add(new KeyBinding(KeyCode.ENTER, KeyEvent.KEY_PRESSED, PRESS_ACTION));
        COMBO_BOX_BASE_BINDINGS.add(new KeyBinding(KeyCode.ENTER, KeyEvent.KEY_RELEASED, RELEASE_ACTION));
        COMBO_BOX_BASE_BINDINGS.add(new KeyBinding(KeyCode.ESCAPE, "Cancel"));
        COMBO_BOX_BASE_BINDINGS.add(new KeyBinding(KeyCode.F10, "ToParent"));
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    protected void callActionForEvent(KeyEvent e2) {
        this.lastEvent = e2;
        this.showPopupOnMouseRelease = true;
        super.callActionForEvent(e2);
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    protected void callAction(String name) {
        if (PRESS_ACTION.equals(name)) {
            keyPressed();
            return;
        }
        if (RELEASE_ACTION.equals(name)) {
            keyReleased();
            return;
        }
        if ("showPopup".equals(name)) {
            show();
            return;
        }
        if ("togglePopup".equals(name)) {
            if (!getControl().isShowing()) {
                show();
                return;
            } else {
                hide();
                return;
            }
        }
        if ("Cancel".equals(name)) {
            cancelEdit(this.lastEvent);
        } else if ("ToParent".equals(name)) {
            forwardToParent(this.lastEvent);
        } else {
            super.callAction(name);
        }
    }

    private void keyPressed() {
        if (Utils.isTwoLevelFocus()) {
            show();
            if (this.tlFocus != null) {
                this.tlFocus.setExternalFocus(false);
                return;
            }
            return;
        }
        if (!getControl().isPressed() && !getControl().isArmed()) {
            this.keyDown = true;
            getControl().arm();
        }
    }

    private void keyReleased() {
        if (!Utils.isTwoLevelFocus() && this.keyDown) {
            this.keyDown = false;
            if (getControl().isArmed()) {
                getControl().disarm();
            }
        }
    }

    protected void forwardToParent(KeyEvent event) {
        if (getControl().getParent() != null) {
            getControl().getParent().fireEvent(event);
        }
    }

    protected void cancelEdit(KeyEvent event) {
        ComboBoxBase comboBoxBase = getControl();
        TextField textField = null;
        if (comboBoxBase instanceof DatePicker) {
            textField = ((DatePicker) comboBoxBase).getEditor();
        } else if (comboBoxBase instanceof ComboBox) {
            textField = comboBoxBase.isEditable() ? ((ComboBox) comboBoxBase).getEditor() : null;
        }
        if (textField != null && textField.getTextFormatter() != null) {
            textField.cancelEdit();
        } else {
            forwardToParent(event);
        }
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    public void mousePressed(MouseEvent e2) {
        super.mousePressed(e2);
        arm(e2);
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    public void mouseReleased(MouseEvent e2) {
        super.mouseReleased(e2);
        disarm();
        if (this.showPopupOnMouseRelease) {
            show();
        } else {
            this.showPopupOnMouseRelease = true;
            hide();
        }
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    public void mouseEntered(MouseEvent e2) {
        super.mouseEntered(e2);
        if (!getControl().isEditable()) {
            this.mouseInsideButton = true;
        } else {
            EventTarget target = e2.getTarget();
            this.mouseInsideButton = (target instanceof Node) && "arrow-button".equals(((Node) target).getId());
        }
        arm();
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    public void mouseExited(MouseEvent e2) {
        super.mouseExited(e2);
        this.mouseInsideButton = false;
        disarm();
    }

    private void getFocus() {
        if (!getControl().isFocused() && getControl().isFocusTraversable()) {
            getControl().requestFocus();
        }
    }

    private void arm(MouseEvent e2) {
        boolean valid = (e2.getButton() != MouseButton.PRIMARY || e2.isMiddleButtonDown() || e2.isSecondaryButtonDown() || e2.isShiftDown() || e2.isControlDown() || e2.isAltDown() || e2.isMetaDown()) ? false : true;
        if (!getControl().isArmed() && valid) {
            getControl().arm();
        }
    }

    public void show() {
        if (!getControl().isShowing()) {
            getControl().requestFocus();
            getControl().show();
        }
    }

    public void hide() {
        if (getControl().isShowing()) {
            getControl().hide();
        }
    }

    public void onAutoHide() {
        hide();
        boolean z2 = (this.mouseInsideButton && this.showPopupOnMouseRelease) ? false : true;
        this.showPopupOnMouseRelease = z2;
    }

    public void arm() {
        if (getControl().isPressed()) {
            getControl().arm();
        }
    }

    public void disarm() {
        if (!this.keyDown && getControl().isArmed()) {
            getControl().disarm();
        }
    }
}
