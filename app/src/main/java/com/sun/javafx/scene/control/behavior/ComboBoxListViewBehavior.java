package com.sun.javafx.scene.control.behavior;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionModel;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/ComboBoxListViewBehavior.class */
public class ComboBoxListViewBehavior<T> extends ComboBoxBaseBehavior<T> {
    protected static final List<KeyBinding> COMBO_BOX_BINDINGS = new ArrayList();

    public ComboBoxListViewBehavior(ComboBox<T> comboBox) {
        super(comboBox, COMBO_BOX_BINDINGS);
    }

    static {
        COMBO_BOX_BINDINGS.add(new KeyBinding(KeyCode.UP, KeyEvent.KEY_PRESSED, "selectPrevious"));
        COMBO_BOX_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "selectNext"));
        COMBO_BOX_BINDINGS.addAll(COMBO_BOX_BASE_BINDINGS);
    }

    @Override // com.sun.javafx.scene.control.behavior.ComboBoxBaseBehavior, com.sun.javafx.scene.control.behavior.BehaviorBase
    protected void callAction(String name) {
        if ("selectPrevious".equals(name)) {
            selectPrevious();
        } else if ("selectNext".equals(name)) {
            selectNext();
        } else {
            super.callAction(name);
        }
    }

    private ComboBox<T> getComboBox() {
        return (ComboBox) getControl();
    }

    private void selectPrevious() {
        SelectionModel<T> sm = getComboBox().getSelectionModel();
        if (sm == null) {
            return;
        }
        sm.selectPrevious();
    }

    private void selectNext() {
        SelectionModel<T> sm = getComboBox().getSelectionModel();
        if (sm == null) {
            return;
        }
        sm.selectNext();
    }
}
