package com.sun.javafx.scene.control.skin;

import java.util.Locale;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.paint.Color;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/WebColorFieldSkin.class */
class WebColorFieldSkin extends InputFieldSkin {
    private InvalidationListener integerFieldValueListener;
    private boolean noChangeInValue;

    public WebColorFieldSkin(WebColorField control) {
        super(control);
        this.noChangeInValue = false;
        ObjectProperty<Color> objectPropertyValueProperty = control.valueProperty();
        InvalidationListener invalidationListener = observable -> {
            updateText();
        };
        this.integerFieldValueListener = invalidationListener;
        objectPropertyValueProperty.addListener(invalidationListener);
        getTextField().setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
    }

    @Override // com.sun.javafx.scene.control.skin.InputFieldSkin, javafx.scene.control.Skin
    public WebColorField getSkinnable() {
        return (WebColorField) this.control;
    }

    @Override // com.sun.javafx.scene.control.skin.InputFieldSkin, javafx.scene.control.Skin
    public Node getNode() {
        return getTextField();
    }

    @Override // com.sun.javafx.scene.control.skin.InputFieldSkin, javafx.scene.control.Skin
    public void dispose() {
        ((WebColorField) this.control).valueProperty().removeListener(this.integerFieldValueListener);
        super.dispose();
    }

    @Override // com.sun.javafx.scene.control.skin.InputFieldSkin
    protected boolean accept(String text) {
        if (text.length() == 0 || text.matches("#[a-fA-F0-9]{0,6}") || text.matches("[a-fA-F0-9]{0,6}")) {
            return true;
        }
        return false;
    }

    @Override // com.sun.javafx.scene.control.skin.InputFieldSkin
    protected void updateText() {
        Color color = ((WebColorField) this.control).getValue();
        if (color == null) {
            color = Color.BLACK;
        }
        getTextField().setText(ColorPickerSkin.formatHexString(color));
    }

    @Override // com.sun.javafx.scene.control.skin.InputFieldSkin
    protected void updateValue() {
        if (this.noChangeInValue) {
            return;
        }
        Color value = ((WebColorField) this.control).getValue();
        String text = getTextField().getText() == null ? "" : getTextField().getText().trim().toUpperCase(Locale.ROOT);
        if (text.matches("#[A-F0-9]{6}") || text.matches("[A-F0-9]{6}")) {
            try {
                Color newValue = text.charAt(0) == '#' ? Color.web(text) : Color.web(FXMLLoader.CONTROLLER_METHOD_PREFIX + text);
                if (!newValue.equals(value)) {
                    ((WebColorField) this.control).setValue(newValue);
                } else {
                    this.noChangeInValue = true;
                    getTextField().setText(ColorPickerSkin.formatHexString(newValue));
                    this.noChangeInValue = false;
                }
            } catch (IllegalArgumentException e2) {
                System.out.println("Failed to parse [" + text + "]");
            }
        }
    }
}
