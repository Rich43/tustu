package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.skin.Utils;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SelectionModel;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/ChoiceBoxBehavior.class */
public class ChoiceBoxBehavior<T> extends BehaviorBase<ChoiceBox<T>> {
    protected static final List<KeyBinding> CHOICE_BUTTON_BINDINGS = new ArrayList();
    private TwoLevelFocusComboBehavior tlFocus;

    static {
        CHOICE_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.SPACE, KeyEvent.KEY_PRESSED, "Press"));
        CHOICE_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.SPACE, KeyEvent.KEY_RELEASED, "Release"));
        if (Utils.isTwoLevelFocus()) {
            CHOICE_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.ENTER, KeyEvent.KEY_PRESSED, "Press"));
            CHOICE_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.ENTER, KeyEvent.KEY_RELEASED, "Release"));
        }
        CHOICE_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.ESCAPE, KeyEvent.KEY_RELEASED, "Cancel"));
        CHOICE_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.DOWN, KeyEvent.KEY_RELEASED, "Down"));
        CHOICE_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.CANCEL, KeyEvent.KEY_RELEASED, "Cancel"));
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    protected void callAction(String name) {
        if (!name.equals("Cancel")) {
            if (!name.equals("Press")) {
                if (!name.equals("Release")) {
                    if (!name.equals("Down")) {
                        super.callAction(name);
                        return;
                    } else {
                        showPopup();
                        return;
                    }
                }
                keyReleased();
                return;
            }
            keyPressed();
            return;
        }
        cancel();
    }

    public ChoiceBoxBehavior(ChoiceBox<T> control) {
        super(control, CHOICE_BUTTON_BINDINGS);
        if (Utils.isTwoLevelFocus()) {
            this.tlFocus = new TwoLevelFocusComboBehavior(control);
        }
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    public void dispose() {
        if (this.tlFocus != null) {
            this.tlFocus.dispose();
        }
        super.dispose();
    }

    public void select(int index) {
        SelectionModel<T> sm = getControl().getSelectionModel();
        if (sm == null) {
            return;
        }
        sm.select(index);
    }

    public void close() {
        getControl().hide();
    }

    public void showPopup() {
        getControl().show();
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    public void mousePressed(MouseEvent e2) {
        ChoiceBox<T> choiceButton = getControl();
        super.mousePressed(e2);
        if (choiceButton.isFocusTraversable()) {
            choiceButton.requestFocus();
        }
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    public void mouseReleased(MouseEvent e2) {
        ChoiceBox<T> choiceButton = getControl();
        super.mouseReleased(e2);
        if (choiceButton.isShowing() || !choiceButton.contains(e2.getX(), e2.getY())) {
            choiceButton.hide();
        } else if (e2.getButton() == MouseButton.PRIMARY) {
            choiceButton.show();
        }
    }

    private void keyPressed() {
        ChoiceBox<T> choiceButton = getControl();
        if (!choiceButton.isShowing()) {
            choiceButton.show();
        }
    }

    private void keyReleased() {
    }

    public void cancel() {
        ChoiceBox<T> choiceButton = getControl();
        choiceButton.hide();
    }
}
