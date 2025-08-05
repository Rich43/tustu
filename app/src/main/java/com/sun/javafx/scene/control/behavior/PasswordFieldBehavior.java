package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.text.HitInfo;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/PasswordFieldBehavior.class */
public class PasswordFieldBehavior extends TextFieldBehavior {
    public PasswordFieldBehavior(PasswordField passwordField) {
        super(passwordField);
    }

    @Override // com.sun.javafx.scene.control.behavior.TextInputControlBehavior
    protected void deletePreviousWord() {
    }

    @Override // com.sun.javafx.scene.control.behavior.TextInputControlBehavior
    protected void deleteNextWord() {
    }

    @Override // com.sun.javafx.scene.control.behavior.TextInputControlBehavior
    protected void selectPreviousWord() {
    }

    @Override // com.sun.javafx.scene.control.behavior.TextInputControlBehavior
    protected void selectNextWord() {
    }

    @Override // com.sun.javafx.scene.control.behavior.TextInputControlBehavior
    protected void previousWord() {
    }

    @Override // com.sun.javafx.scene.control.behavior.TextInputControlBehavior
    protected void nextWord() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.behavior.TextInputControlBehavior
    protected void selectWord() {
        ((TextField) getControl()).selectAll();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.behavior.TextFieldBehavior
    protected void mouseDoubleClick(HitInfo hit) {
        ((TextField) getControl()).selectAll();
    }
}
