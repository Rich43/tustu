package com.sun.javafx.scene.control.skin;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Skin;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/DoubleField.class */
class DoubleField extends InputField {
    private DoubleProperty value = new SimpleDoubleProperty(this, "value");

    public final double getValue() {
        return this.value.get();
    }

    public final void setValue(double value) {
        this.value.set(value);
    }

    public final DoubleProperty valueProperty() {
        return this.value;
    }

    public DoubleField() {
        getStyleClass().setAll("double-field");
    }

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new DoubleFieldSkin(this);
    }
}
