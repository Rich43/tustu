package com.sun.javafx.scene.control.skin;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Skin;
import javafx.scene.paint.Color;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/WebColorField.class */
class WebColorField extends InputField {
    private ObjectProperty<Color> value = new SimpleObjectProperty(this, "value");

    public final Color getValue() {
        return this.value.get();
    }

    public final void setValue(Color value) {
        this.value.set(value);
    }

    public final ObjectProperty<Color> valueProperty() {
        return this.value;
    }

    public WebColorField() {
        getStyleClass().setAll("webcolor-field");
    }

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new WebColorFieldSkin(this);
    }
}
