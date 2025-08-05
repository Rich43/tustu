package javafx.css;

import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.value.ObservableValue;

/* loaded from: jfxrt.jar:javafx/css/StyleableBooleanProperty.class */
public abstract class StyleableBooleanProperty extends BooleanPropertyBase implements StyleableProperty<Boolean> {
    private StyleOrigin origin;

    public StyleableBooleanProperty() {
        this.origin = null;
    }

    public StyleableBooleanProperty(boolean initialValue) {
        super(initialValue);
        this.origin = null;
    }

    @Override // javafx.css.StyleableProperty
    public void applyStyle(StyleOrigin origin, Boolean v2) {
        set(v2.booleanValue());
        this.origin = origin;
    }

    @Override // javafx.beans.property.BooleanPropertyBase, javafx.beans.property.Property
    public void bind(ObservableValue<? extends Boolean> observable) {
        super.bind(observable);
        this.origin = StyleOrigin.USER;
    }

    @Override // javafx.beans.property.BooleanPropertyBase, javafx.beans.value.WritableBooleanValue
    public void set(boolean v2) {
        super.set(v2);
        this.origin = StyleOrigin.USER;
    }

    @Override // javafx.css.StyleableProperty
    public StyleOrigin getStyleOrigin() {
        return this.origin;
    }
}
