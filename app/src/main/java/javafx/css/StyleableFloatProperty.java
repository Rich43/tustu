package javafx.css;

import javafx.beans.property.FloatPropertyBase;
import javafx.beans.value.ObservableValue;

/* loaded from: jfxrt.jar:javafx/css/StyleableFloatProperty.class */
public abstract class StyleableFloatProperty extends FloatPropertyBase implements StyleableProperty<Number> {
    private StyleOrigin origin;

    public StyleableFloatProperty() {
        this.origin = null;
    }

    public StyleableFloatProperty(float initialValue) {
        super(initialValue);
        this.origin = null;
    }

    @Override // javafx.css.StyleableProperty
    public void applyStyle(StyleOrigin origin, Number v2) {
        setValue(v2);
        this.origin = origin;
    }

    @Override // javafx.beans.property.FloatPropertyBase, javafx.beans.property.Property
    public void bind(ObservableValue<? extends Number> observable) {
        super.bind(observable);
        this.origin = StyleOrigin.USER;
    }

    @Override // javafx.beans.property.FloatPropertyBase, javafx.beans.value.WritableFloatValue
    public void set(float v2) {
        super.set(v2);
        this.origin = StyleOrigin.USER;
    }

    @Override // javafx.css.StyleableProperty
    public StyleOrigin getStyleOrigin() {
        return this.origin;
    }
}
