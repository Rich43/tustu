package javafx.css;

import java.util.Collections;
import java.util.List;
import javafx.css.Styleable;

/* loaded from: jfxrt.jar:javafx/css/CssMetaData.class */
public abstract class CssMetaData<S extends Styleable, V> {
    private final String property;
    private final StyleConverter<?, V> converter;
    private final V initialValue;
    private final List<CssMetaData<? extends Styleable, ?>> subProperties;
    private final boolean inherits;

    public abstract boolean isSettable(S s2);

    public abstract StyleableProperty<V> getStyleableProperty(S s2);

    @Deprecated
    public void set(S styleable, V value, StyleOrigin origin) {
        StyleableProperty<V> styleableProperty = getStyleableProperty(styleable);
        StyleOrigin currentOrigin = styleableProperty.getStyleOrigin();
        V currentValue = styleableProperty.getValue();
        if (currentOrigin == origin) {
            if (currentValue != null) {
                if (currentValue.equals(value)) {
                    return;
                }
            } else if (value == null) {
                return;
            }
        }
        styleableProperty.applyStyle(origin, value);
    }

    public final String getProperty() {
        return this.property;
    }

    public final StyleConverter<?, V> getConverter() {
        return this.converter;
    }

    public V getInitialValue(S styleable) {
        return this.initialValue;
    }

    public final List<CssMetaData<? extends Styleable, ?>> getSubProperties() {
        return this.subProperties;
    }

    public final boolean isInherits() {
        return this.inherits;
    }

    protected CssMetaData(String property, StyleConverter<?, V> converter, V initialValue, boolean inherits, List<CssMetaData<? extends Styleable, ?>> subProperties) {
        this.property = property;
        this.converter = converter;
        this.initialValue = initialValue;
        this.inherits = inherits;
        this.subProperties = subProperties != null ? Collections.unmodifiableList(subProperties) : null;
        if (this.property == null || this.converter == null) {
            throw new IllegalArgumentException("neither property nor converter can be null");
        }
    }

    protected CssMetaData(String property, StyleConverter<?, V> converter, V initialValue, boolean inherits) {
        this(property, converter, initialValue, inherits, null);
    }

    protected CssMetaData(String property, StyleConverter<?, V> converter, V initialValue) {
        this(property, converter, initialValue, false, null);
    }

    protected CssMetaData(String property, StyleConverter<?, V> converter) {
        this(property, converter, null, false, null);
    }

    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CssMetaData<? extends Styleable, ?> other = (CssMetaData) obj;
        if (this.property == null) {
            if (other.property != null) {
                return false;
            }
            return true;
        }
        if (!this.property.equals(other.property)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = (19 * 3) + (this.property != null ? this.property.hashCode() : 0);
        return hash;
    }

    public String toString() {
        return "CSSProperty {property: " + this.property + ", converter: " + this.converter.toString() + ", initalValue: " + String.valueOf(this.initialValue) + ", inherits: " + this.inherits + ", subProperties: " + (this.subProperties != null ? this.subProperties.toString() : "[]") + "}";
    }
}
