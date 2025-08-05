package javafx.css;

import javafx.beans.property.DoublePropertyBase;
import javafx.beans.value.ObservableValue;

/* loaded from: jfxrt.jar:javafx/css/StyleableDoubleProperty.class */
public abstract class StyleableDoubleProperty extends DoublePropertyBase implements StyleableProperty<Number> {
    private StyleOrigin origin;

    public StyleableDoubleProperty() {
        this.origin = null;
    }

    public StyleableDoubleProperty(double initialValue) {
        super(initialValue);
        this.origin = null;
    }

    @Override // javafx.css.StyleableProperty
    public void applyStyle(StyleOrigin origin, Number v2) {
        setValue(v2);
        this.origin = origin;
    }

    @Override // javafx.beans.property.DoublePropertyBase, javafx.beans.property.Property
    public void bind(ObservableValue<? extends Number> observable) {
        super.bind(observable);
        this.origin = StyleOrigin.USER;
    }

    @Override // javafx.beans.property.DoublePropertyBase, javafx.beans.value.WritableDoubleValue
    public void set(double v2) {
        super.set(v2);
        this.origin = StyleOrigin.USER;
    }

    @Override // javafx.css.StyleableProperty
    public StyleOrigin getStyleOrigin() {
        return this.origin;
    }
}
