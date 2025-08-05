package javafx.css;

import javafx.beans.property.StringPropertyBase;
import javafx.beans.value.ObservableValue;

/* loaded from: jfxrt.jar:javafx/css/StyleableStringProperty.class */
public abstract class StyleableStringProperty extends StringPropertyBase implements StyleableProperty<String> {
    private StyleOrigin origin;

    public StyleableStringProperty() {
        this.origin = null;
    }

    public StyleableStringProperty(String initialValue) {
        super(initialValue);
        this.origin = null;
    }

    @Override // javafx.css.StyleableProperty
    public void applyStyle(StyleOrigin origin, String v2) {
        set(v2);
        this.origin = origin;
    }

    @Override // javafx.beans.property.StringPropertyBase, javafx.beans.property.Property
    public void bind(ObservableValue<? extends String> observable) {
        super.bind(observable);
        this.origin = StyleOrigin.USER;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.beans.property.StringPropertyBase, javafx.beans.value.WritableObjectValue
    public void set(String v2) {
        super.set(v2);
        this.origin = StyleOrigin.USER;
    }

    @Override // javafx.css.StyleableProperty
    public StyleOrigin getStyleOrigin() {
        return this.origin;
    }
}
