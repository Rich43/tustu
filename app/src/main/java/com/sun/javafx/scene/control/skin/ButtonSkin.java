package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ButtonBehavior;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ButtonSkin.class */
public class ButtonSkin extends LabeledSkinBase<Button, ButtonBehavior<Button>> {
    Runnable defaultButtonRunnable;
    Runnable cancelButtonRunnable;
    private KeyCodeCombination defaultAcceleratorKeyCodeCombination;
    private KeyCodeCombination cancelAcceleratorKeyCodeCombination;

    /* JADX WARN: Multi-variable type inference failed */
    public ButtonSkin(Button button) {
        super(button, new ButtonBehavior(button));
        this.defaultButtonRunnable = () -> {
            if (((Button) getSkinnable()).getScene() != null && ((Button) getSkinnable()).impl_isTreeVisible() && !((Button) getSkinnable()).isDisabled()) {
                ((Button) getSkinnable()).fire();
            }
        };
        this.cancelButtonRunnable = () -> {
            if (((Button) getSkinnable()).getScene() != null && ((Button) getSkinnable()).impl_isTreeVisible() && !((Button) getSkinnable()).isDisabled()) {
                ((Button) getSkinnable()).fire();
            }
        };
        registerChangeListener(button.defaultButtonProperty(), "DEFAULT_BUTTON");
        registerChangeListener(button.cancelButtonProperty(), "CANCEL_BUTTON");
        registerChangeListener(button.focusedProperty(), "FOCUSED");
        if (((Button) getSkinnable()).isDefaultButton()) {
            setDefaultButton(true);
        }
        if (((Button) getSkinnable()).isCancelButton()) {
            setCancelButton(true);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, com.sun.javafx.scene.control.skin.BehaviorSkinBase
    protected void handleControlPropertyChanged(String p2) {
        ContextMenu cm;
        super.handleControlPropertyChanged(p2);
        if ("DEFAULT_BUTTON".equals(p2)) {
            setDefaultButton(((Button) getSkinnable()).isDefaultButton());
            return;
        }
        if ("CANCEL_BUTTON".equals(p2)) {
            setCancelButton(((Button) getSkinnable()).isCancelButton());
            return;
        }
        if ("FOCUSED".equals(p2)) {
            if (!((Button) getSkinnable()).isFocused() && (cm = ((Button) getSkinnable()).getContextMenu()) != null && cm.isShowing()) {
                cm.hide();
                Utils.removeMnemonics(cm, ((Button) getSkinnable()).getScene());
                return;
            }
            return;
        }
        if ("PARENT".equals(p2) && ((Button) getSkinnable()).getParent() == null && ((Button) getSkinnable()).getScene() != null) {
            if (((Button) getSkinnable()).isDefaultButton()) {
                ((Button) getSkinnable()).getScene().getAccelerators().remove(this.defaultAcceleratorKeyCodeCombination);
            }
            if (((Button) getSkinnable()).isCancelButton()) {
                ((Button) getSkinnable()).getScene().getAccelerators().remove(this.cancelAcceleratorKeyCodeCombination);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void setDefaultButton(boolean value) {
        Scene scene = ((Button) getSkinnable()).getScene();
        if (scene != null) {
            KeyCode acceleratorCode = KeyCode.ENTER;
            this.defaultAcceleratorKeyCodeCombination = new KeyCodeCombination(acceleratorCode, new KeyCombination.Modifier[0]);
            Runnable oldDefault = scene.getAccelerators().get(this.defaultAcceleratorKeyCodeCombination);
            if (!value) {
                if (this.defaultButtonRunnable.equals(oldDefault)) {
                    scene.getAccelerators().remove(this.defaultAcceleratorKeyCodeCombination);
                }
            } else if (!this.defaultButtonRunnable.equals(oldDefault)) {
                scene.getAccelerators().remove(this.defaultAcceleratorKeyCodeCombination);
                scene.getAccelerators().put(this.defaultAcceleratorKeyCodeCombination, this.defaultButtonRunnable);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void setCancelButton(boolean value) {
        Scene scene = ((Button) getSkinnable()).getScene();
        if (scene != null) {
            KeyCode acceleratorCode = KeyCode.ESCAPE;
            this.cancelAcceleratorKeyCodeCombination = new KeyCodeCombination(acceleratorCode, new KeyCombination.Modifier[0]);
            Runnable oldCancel = scene.getAccelerators().get(this.cancelAcceleratorKeyCodeCombination);
            if (!value) {
                if (this.cancelButtonRunnable.equals(oldCancel)) {
                    scene.getAccelerators().remove(this.cancelAcceleratorKeyCodeCombination);
                }
            } else if (!this.cancelButtonRunnable.equals(oldCancel)) {
                scene.getAccelerators().remove(this.cancelAcceleratorKeyCodeCombination);
                scene.getAccelerators().put(this.cancelAcceleratorKeyCodeCombination, this.cancelButtonRunnable);
            }
        }
    }
}
