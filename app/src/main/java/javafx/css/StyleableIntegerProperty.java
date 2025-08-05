package javafx.css;

import javafx.beans.property.IntegerPropertyBase;
import javafx.beans.value.ObservableValue;

/* loaded from: jfxrt.jar:javafx/css/StyleableIntegerProperty.class */
public abstract class StyleableIntegerProperty extends IntegerPropertyBase implements StyleableProperty<Number> {
    private StyleOrigin origin;

    public StyleableIntegerProperty() {
        this.origin = null;
    }

    public StyleableIntegerProperty(int initialValue) {
        super(initialValue);
        this.origin = null;
    }

    @Override // javafx.css.StyleableProperty
    public void applyStyle(StyleOrigin origin, Number v2) {
        setValue(v2);
        this.origin = origin;
    }

    @Override // javafx.beans.property.IntegerPropertyBase, javafx.beans.property.Property
    public void bind(ObservableValue<? extends Number> observable) {
        super.bind(observable);
        this.origin = StyleOrigin.USER;
    }

    @Override // javafx.beans.property.IntegerPropertyBase, javafx.beans.value.WritableIntegerValue
    public void set(int v2) {
        super.set(v2);
        this.origin = StyleOrigin.USER;
    }

    @Override // javafx.css.StyleableProperty
    public StyleOrigin getStyleOrigin() {
        return this.origin;
    }
}
