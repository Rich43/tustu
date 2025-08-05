package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.skin.Utils;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/ToggleButtonBehavior.class */
public class ToggleButtonBehavior<C extends ToggleButton> extends ButtonBehavior<C> {
    protected static final List<KeyBinding> TOGGLE_BUTTON_BINDINGS = new ArrayList();

    public ToggleButtonBehavior(C button) {
        super(button, TOGGLE_BUTTON_BINDINGS);
    }

    static {
        TOGGLE_BUTTON_BINDINGS.addAll(BUTTON_BINDINGS);
        TOGGLE_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "ToggleNext-Right"));
        TOGGLE_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "TogglePrevious-Left"));
        TOGGLE_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "ToggleNext-Down"));
        TOGGLE_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.UP, "TogglePrevious-Up"));
    }

    private int nextToggleIndex(ObservableList<Toggle> toggles, int from) {
        int i2;
        if (from < 0 || from >= toggles.size()) {
            return 0;
        }
        int i3 = from + 1;
        int size = toggles.size();
        while (true) {
            i2 = i3 % size;
            if (i2 == from) {
                break;
            }
            Object obj = (Toggle) toggles.get(i2);
            if (!(obj instanceof Node) || !((Node) obj).isDisabled()) {
                break;
            }
            i3 = i2 + 1;
            size = toggles.size();
        }
        return i2;
    }

    private int previousToggleIndex(ObservableList<Toggle> toggles, int from) {
        int i2;
        if (from < 0 || from >= toggles.size()) {
            return toggles.size();
        }
        int iFloorMod = Math.floorMod(from - 1, toggles.size());
        while (true) {
            i2 = iFloorMod;
            if (i2 == from) {
                break;
            }
            Object obj = (Toggle) toggles.get(i2);
            if (!(obj instanceof Node) || !((Node) obj).isDisabled()) {
                break;
            }
            iFloorMod = Math.floorMod(i2 - 1, toggles.size());
        }
        return i2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.behavior.ButtonBehavior, com.sun.javafx.scene.control.behavior.BehaviorBase
    protected void callAction(String name) {
        ObservableList<Toggle> toggles;
        int currentToggleIdx;
        ToggleButton toggleButton = getControl();
        ToggleGroup toggleGroup = toggleButton.getToggleGroup();
        if (toggleGroup == null) {
            super.callAction(name);
        }
        toggles = toggleGroup.getToggles();
        currentToggleIdx = toggles.indexOf(toggleButton);
        switch (name) {
            case "ToggleNext-Right":
            case "ToggleNext-Down":
            case "TogglePrevious-Left":
            case "TogglePrevious-Up":
                boolean traversingToNext = traversingToNext(name, toggleButton.getEffectiveNodeOrientation());
                if (Utils.isTwoLevelFocus()) {
                    super.callAction(toggleToTraverseAction(name));
                    break;
                } else if (traversingToNext) {
                    int nextToggleIndex = nextToggleIndex(toggles, currentToggleIdx);
                    if (nextToggleIndex == currentToggleIdx) {
                        super.callAction(toggleToTraverseAction(name));
                        break;
                    } else {
                        Toggle toggle = toggles.get(nextToggleIndex);
                        toggleGroup.selectToggle(toggle);
                        ((Control) toggle).requestFocus();
                        break;
                    }
                } else {
                    int prevToggleIndex = previousToggleIndex(toggles, currentToggleIdx);
                    if (prevToggleIndex == currentToggleIdx) {
                        super.callAction(toggleToTraverseAction(name));
                        break;
                    } else {
                        Toggle toggle2 = toggles.get(prevToggleIndex);
                        toggleGroup.selectToggle(toggle2);
                        ((Control) toggle2).requestFocus();
                        break;
                    }
                }
            default:
                super.callAction(name);
                break;
        }
    }

    private boolean traversingToNext(String name, NodeOrientation effectiveNodeOrientation) {
        boolean rtl;
        rtl = effectiveNodeOrientation == NodeOrientation.RIGHT_TO_LEFT;
        switch (name) {
            case "ToggleNext-Right":
                return !rtl;
            case "ToggleNext-Down":
                return true;
            case "TogglePrevious-Left":
                return rtl;
            case "TogglePrevious-Up":
                return false;
            default:
                throw new IllegalArgumentException("Not a toggle action");
        }
    }

    private String toggleToTraverseAction(String name) {
        switch (name) {
            case "ToggleNext-Right":
                return "TraverseRight";
            case "ToggleNext-Down":
                return "TraverseDown";
            case "TogglePrevious-Left":
                return "TraverseLeft";
            case "TogglePrevious-Up":
                return "TraverseUp";
            default:
                throw new IllegalArgumentException("Not a toggle action");
        }
    }
}
