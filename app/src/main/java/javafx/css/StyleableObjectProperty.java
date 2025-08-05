package javafx.css;

import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.value.ObservableValue;

/* loaded from: jfxrt.jar:javafx/css/StyleableObjectProperty.class */
public abstract class StyleableObjectProperty<T> extends ObjectPropertyBase<T> implements StyleableProperty<T> {
    private StyleOrigin origin;

    public StyleableObjectProperty() {
        this.origin = null;
    }

    public StyleableObjectProperty(T initialValue) {
        super(initialValue);
        this.origin = null;
    }

    public void applyStyle(StyleOrigin origin, T v2) {
        set(v2);
        this.origin = origin;
    }

    @Override // javafx.beans.property.ObjectPropertyBase, javafx.beans.property.Property
    public void bind(ObservableValue<? extends T> observable) {
        super.bind(observable);
        this.origin = StyleOrigin.USER;
    }

    @Override // javafx.beans.property.ObjectPropertyBase, javafx.beans.value.WritableObjectValue
    public void set(T v2) {
        super.set(v2);
        this.origin = StyleOrigin.USER;
    }

    public StyleOrigin getStyleOrigin() {
        return this.origin;
    }
}
