package javafx.css;

import javafx.beans.value.WritableValue;

/* loaded from: jfxrt.jar:javafx/css/StyleableProperty.class */
public interface StyleableProperty<T> extends WritableValue<T> {
    void applyStyle(StyleOrigin styleOrigin, T t2);

    StyleOrigin getStyleOrigin();

    CssMetaData<? extends Styleable, T> getCssMetaData();
}
