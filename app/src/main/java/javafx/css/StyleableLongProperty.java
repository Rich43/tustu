package javafx.css;

import javafx.beans.property.LongPropertyBase;
import javafx.beans.value.ObservableValue;

/* loaded from: jfxrt.jar:javafx/css/StyleableLongProperty.class */
public abstract class StyleableLongProperty extends LongPropertyBase implements StyleableProperty<Number> {
    private StyleOrigin origin;

    public StyleableLongProperty() {
        this.origin = null;
    }

    public StyleableLongProperty(long initialValue) {
        super(initialValue);
        this.origin = null;
    }

    @Override // javafx.css.StyleableProperty
    public void applyStyle(StyleOrigin origin, Number v2) {
        setValue(v2);
        this.origin = origin;
    }

    @Override // javafx.beans.property.LongPropertyBase, javafx.beans.property.Property
    public void bind(ObservableValue<? extends Number> observable) {
        super.bind(observable);
        this.origin = StyleOrigin.USER;
    }

    @Override // javafx.beans.property.LongPropertyBase, javafx.beans.value.WritableLongValue
    public void set(long v2) {
        super.set(v2);
        this.origin = StyleOrigin.USER;
    }

    @Override // javafx.css.StyleableProperty
    public StyleOrigin getStyleOrigin() {
        return this.origin;
    }
}
