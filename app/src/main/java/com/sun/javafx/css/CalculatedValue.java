package com.sun.javafx.css;

import javafx.css.StyleOrigin;

/* loaded from: jfxrt.jar:com/sun/javafx/css/CalculatedValue.class */
public final class CalculatedValue {
    public static final CalculatedValue SKIP = new CalculatedValue(new int[0], null, false);
    private final Object value;
    private final StyleOrigin origin;
    private final boolean relative;

    public CalculatedValue(Object value, StyleOrigin origin, boolean relative) {
        this.value = value;
        this.origin = origin;
        this.relative = relative;
    }

    public Object getValue() {
        return this.value;
    }

    public StyleOrigin getOrigin() {
        return this.origin;
    }

    public boolean isRelative() {
        return this.relative;
    }

    public String toString() {
        return '{' + String.valueOf(this.value) + ", " + ((Object) this.origin) + ", " + this.relative + '}';
    }

    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CalculatedValue other = (CalculatedValue) obj;
        if (this.relative != other.relative || this.origin != other.origin) {
            return false;
        }
        if (this.value == null) {
            if (other.value != null) {
                return false;
            }
            return true;
        }
        if (!this.value.equals(other.value)) {
            return false;
        }
        return true;
    }
}
