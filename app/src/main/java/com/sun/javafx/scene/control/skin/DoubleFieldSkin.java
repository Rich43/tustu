package com.sun.javafx.scene.control.skin;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/DoubleFieldSkin.class */
public class DoubleFieldSkin extends InputFieldSkin {
    private InvalidationListener doubleFieldValueListener;

    public DoubleFieldSkin(DoubleField control) {
        super(control);
        DoubleProperty doublePropertyValueProperty = control.valueProperty();
        InvalidationListener invalidationListener = observable -> {
            updateText();
        };
        this.doubleFieldValueListener = invalidationListener;
        doublePropertyValueProperty.addListener(invalidationListener);
    }

    @Override // com.sun.javafx.scene.control.skin.InputFieldSkin, javafx.scene.control.Skin
    public DoubleField getSkinnable() {
        return (DoubleField) this.control;
    }

    @Override // com.sun.javafx.scene.control.skin.InputFieldSkin, javafx.scene.control.Skin
    public Node getNode() {
        return getTextField();
    }

    @Override // com.sun.javafx.scene.control.skin.InputFieldSkin, javafx.scene.control.Skin
    public void dispose() {
        ((DoubleField) this.control).valueProperty().removeListener(this.doubleFieldValueListener);
        super.dispose();
    }

    @Override // com.sun.javafx.scene.control.skin.InputFieldSkin
    protected boolean accept(String text) {
        if (text.length() == 0) {
            return true;
        }
        if (text.matches("[0-9\\.]*")) {
            try {
                Double.parseDouble(text);
                return true;
            } catch (NumberFormatException e2) {
                return false;
            }
        }
        return false;
    }

    @Override // com.sun.javafx.scene.control.skin.InputFieldSkin
    protected void updateText() {
        getTextField().setText("" + ((DoubleField) this.control).getValue());
    }

    @Override // com.sun.javafx.scene.control.skin.InputFieldSkin
    protected void updateValue() {
        double value = ((DoubleField) this.control).getValue();
        String text = getTextField().getText() == null ? "" : getTextField().getText().trim();
        try {
            double newValue = Double.parseDouble(text);
            if (newValue != value) {
                ((DoubleField) this.control).setValue(newValue);
            }
        } catch (NumberFormatException e2) {
            ((DoubleField) this.control).setValue(0.0d);
            Platform.runLater(() -> {
                getTextField().positionCaret(1);
            });
        }
    }
}
