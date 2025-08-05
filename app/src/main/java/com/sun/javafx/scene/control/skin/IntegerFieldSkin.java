package com.sun.javafx.scene.control.skin;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.property.IntegerProperty;
import javafx.scene.Node;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/IntegerFieldSkin.class */
public class IntegerFieldSkin extends InputFieldSkin {
    private InvalidationListener integerFieldValueListener;

    public IntegerFieldSkin(IntegerField control) {
        super(control);
        IntegerProperty integerPropertyValueProperty = control.valueProperty();
        InvalidationListener invalidationListener = observable -> {
            updateText();
        };
        this.integerFieldValueListener = invalidationListener;
        integerPropertyValueProperty.addListener(invalidationListener);
    }

    @Override // com.sun.javafx.scene.control.skin.InputFieldSkin, javafx.scene.control.Skin
    public IntegerField getSkinnable() {
        return (IntegerField) this.control;
    }

    @Override // com.sun.javafx.scene.control.skin.InputFieldSkin, javafx.scene.control.Skin
    public Node getNode() {
        return getTextField();
    }

    @Override // com.sun.javafx.scene.control.skin.InputFieldSkin, javafx.scene.control.Skin
    public void dispose() {
        ((IntegerField) this.control).valueProperty().removeListener(this.integerFieldValueListener);
        super.dispose();
    }

    @Override // com.sun.javafx.scene.control.skin.InputFieldSkin
    protected boolean accept(String text) {
        if (text.length() == 0) {
            return true;
        }
        if (text.matches("[0-9]*")) {
            try {
                Integer.parseInt(text);
                int value = Integer.parseInt(text);
                int maxValue = ((IntegerField) this.control).getMaxValue();
                if (maxValue != -1 && value > maxValue) {
                    return false;
                }
                return true;
            } catch (NumberFormatException e2) {
                return false;
            }
        }
        return false;
    }

    @Override // com.sun.javafx.scene.control.skin.InputFieldSkin
    protected void updateText() {
        getTextField().setText("" + ((IntegerField) this.control).getValue());
    }

    @Override // com.sun.javafx.scene.control.skin.InputFieldSkin
    protected void updateValue() {
        int value = ((IntegerField) this.control).getValue();
        String text = getTextField().getText() == null ? "" : getTextField().getText().trim();
        try {
            int newValue = Integer.parseInt(text);
            if (newValue != value) {
                ((IntegerField) this.control).setValue(newValue);
            }
        } catch (NumberFormatException e2) {
            ((IntegerField) this.control).setValue(0);
            Platform.runLater(() -> {
                getTextField().positionCaret(1);
            });
        }
    }
}
