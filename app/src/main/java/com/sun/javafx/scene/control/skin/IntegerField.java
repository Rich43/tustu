package com.sun.javafx.scene.control.skin;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Skin;
import javax.management.JMX;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/IntegerField.class */
class IntegerField extends InputField {
    private IntegerProperty value;
    private IntegerProperty maxValue;

    public final int getValue() {
        return this.value.get();
    }

    public final void setValue(int value) {
        this.value.set(value);
    }

    public final IntegerProperty valueProperty() {
        return this.value;
    }

    public final int getMaxValue() {
        return this.maxValue.get();
    }

    public final void setMaxValue(int maxVal) {
        this.maxValue.set(maxVal);
    }

    public final IntegerProperty maxValueProperty() {
        return this.maxValue;
    }

    public IntegerField() {
        this(-1);
    }

    public IntegerField(int maxVal) {
        this.value = new SimpleIntegerProperty(this, "value");
        this.maxValue = new SimpleIntegerProperty(this, JMX.MAX_VALUE_FIELD, -1);
        getStyleClass().setAll("integer-field");
        setMaxValue(maxVal);
    }

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new IntegerFieldSkin(this);
    }
}
