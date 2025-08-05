package com.sun.javafx.scene.control.skin;

import com.sun.javafx.event.EventDispatchChainImpl;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.EventDispatchChain;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/InputFieldSkin.class */
abstract class InputFieldSkin implements Skin<InputField> {
    protected InputField control;
    private InnerTextField textField = new InnerTextField() { // from class: com.sun.javafx.scene.control.skin.InputFieldSkin.1
        @Override // javafx.scene.control.TextInputControl
        public void replaceText(int start, int end, String text) {
            String t2 = InputFieldSkin.this.textField.getText() == null ? "" : InputFieldSkin.this.textField.getText();
            if (InputFieldSkin.this.accept(t2.substring(0, start) + text + t2.substring(end))) {
                super.replaceText(start, end, text);
            }
        }

        @Override // javafx.scene.control.TextInputControl
        public void replaceSelection(String text) {
            String t2 = InputFieldSkin.this.textField.getText() == null ? "" : InputFieldSkin.this.textField.getText();
            int start = Math.min(InputFieldSkin.this.textField.getAnchor(), InputFieldSkin.this.textField.getCaretPosition());
            int end = Math.max(InputFieldSkin.this.textField.getAnchor(), InputFieldSkin.this.textField.getCaretPosition());
            if (InputFieldSkin.this.accept(t2.substring(0, start) + text + t2.substring(end))) {
                super.replaceSelection(text);
            }
        }
    };
    private InvalidationListener InputFieldFocusListener;
    private InvalidationListener InputFieldStyleClassListener;

    protected abstract boolean accept(String str);

    protected abstract void updateText();

    protected abstract void updateValue();

    public InputFieldSkin(InputField control) {
        this.control = control;
        this.textField.setId("input-text-field");
        this.textField.setFocusTraversable(false);
        control.getStyleClass().addAll(this.textField.getStyleClass());
        this.textField.getStyleClass().setAll(control.getStyleClass());
        ObservableList<String> styleClass = control.getStyleClass();
        InvalidationListener invalidationListener = observable -> {
            this.textField.getStyleClass().setAll(control.getStyleClass());
        };
        this.InputFieldStyleClassListener = invalidationListener;
        styleClass.addListener(invalidationListener);
        this.textField.promptTextProperty().bind(control.promptTextProperty());
        this.textField.prefColumnCountProperty().bind(control.prefColumnCountProperty());
        this.textField.textProperty().addListener(observable2 -> {
            updateValue();
        });
        ReadOnlyBooleanProperty readOnlyBooleanPropertyFocusedProperty = control.focusedProperty();
        InvalidationListener invalidationListener2 = observable3 -> {
            this.textField.handleFocus(control.isFocused());
        };
        this.InputFieldFocusListener = invalidationListener2;
        readOnlyBooleanPropertyFocusedProperty.addListener(invalidationListener2);
        updateText();
    }

    @Override // javafx.scene.control.Skin
    public InputField getSkinnable() {
        return this.control;
    }

    @Override // javafx.scene.control.Skin
    public Node getNode() {
        return this.textField;
    }

    @Override // javafx.scene.control.Skin
    public void dispose() {
        this.control.getStyleClass().removeListener(this.InputFieldStyleClassListener);
        this.control.focusedProperty().removeListener(this.InputFieldFocusListener);
        this.textField = null;
    }

    protected TextField getTextField() {
        return this.textField;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/InputFieldSkin$InnerTextField.class */
    private class InnerTextField extends TextField {
        private InnerTextField() {
        }

        public void handleFocus(boolean b2) {
            setFocused(b2);
        }

        @Override // javafx.scene.Node, javafx.event.EventTarget
        public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
            EventDispatchChain chain = new EventDispatchChainImpl();
            chain.append(InputFieldSkin.this.textField.getEventDispatcher());
            return chain;
        }
    }
}
