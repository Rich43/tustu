package com.sun.javafx.css;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javafx.css.PseudoClass;
import javafx.scene.text.Font;

/* loaded from: jfxrt.jar:com/sun/javafx/css/StyleCacheEntry.class */
public final class StyleCacheEntry {
    private Map<String, CalculatedValue> calculatedValues;

    public CalculatedValue get(String property) {
        CalculatedValue cv = null;
        if (this.calculatedValues != null && !this.calculatedValues.isEmpty()) {
            cv = this.calculatedValues.get(property);
        }
        return cv;
    }

    public void put(String property, CalculatedValue calculatedValue) {
        if (this.calculatedValues == null) {
            this.calculatedValues = new HashMap(5);
        }
        this.calculatedValues.put(property, calculatedValue);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/StyleCacheEntry$Key.class */
    public static final class Key {
        private final Set<PseudoClass>[] pseudoClassStates;
        private final double fontSize;
        private int hash = Integer.MIN_VALUE;

        public Key(Set<PseudoClass>[] pseudoClassStates, Font font) {
            this.pseudoClassStates = new Set[pseudoClassStates.length];
            for (int n2 = 0; n2 < pseudoClassStates.length; n2++) {
                this.pseudoClassStates[n2] = new PseudoClassState();
                this.pseudoClassStates[n2].addAll(pseudoClassStates[n2]);
            }
            this.fontSize = font != null ? font.getSize() : Font.getDefault().getSize();
        }

        public String toString() {
            return Arrays.toString(this.pseudoClassStates) + ", " + this.fontSize;
        }

        public static int hashCode(double value) {
            long bits = Double.doubleToLongBits(value);
            return (int) (bits ^ (bits >>> 32));
        }

        public int hashCode() {
            if (this.hash == Integer.MIN_VALUE) {
                this.hash = hashCode(this.fontSize);
                int iMax = this.pseudoClassStates != null ? this.pseudoClassStates.length : 0;
                for (int i2 = 0; i2 < iMax; i2++) {
                    Set<PseudoClass> states = this.pseudoClassStates[i2];
                    if (states != null) {
                        this.hash = 67 * (this.hash + states.hashCode());
                    }
                }
            }
            return this.hash;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj == null || obj.getClass() != getClass()) {
                return false;
            }
            Key other = (Key) obj;
            if (this.hash != other.hash) {
                return false;
            }
            double diff = this.fontSize - other.fontSize;
            if (diff < -1.0E-6d || 1.0E-6d < diff) {
                return false;
            }
            if ((this.pseudoClassStates == null) ^ (other.pseudoClassStates == null)) {
                return false;
            }
            if (this.pseudoClassStates == null) {
                return true;
            }
            if (this.pseudoClassStates.length != other.pseudoClassStates.length) {
                return false;
            }
            for (int i2 = 0; i2 < this.pseudoClassStates.length; i2++) {
                Set<PseudoClass> this_pcs = this.pseudoClassStates[i2];
                Set<PseudoClass> other_pcs = other.pseudoClassStates[i2];
                if (this_pcs == null) {
                    if (other_pcs != null) {
                        return false;
                    }
                } else if (!this_pcs.equals(other_pcs)) {
                    return false;
                }
            }
            return true;
        }
    }
}
