package org.apache.commons.math3.fraction;

import java.io.Serializable;
import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/fraction/FractionField.class */
public class FractionField implements Field<Fraction>, Serializable {
    private static final long serialVersionUID = -1257768487499119313L;

    private FractionField() {
    }

    public static FractionField getInstance() {
        return LazyHolder.INSTANCE;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.Field
    public Fraction getOne() {
        return Fraction.ONE;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.Field
    public Fraction getZero() {
        return Fraction.ZERO;
    }

    @Override // org.apache.commons.math3.Field
    public Class<? extends FieldElement<Fraction>> getRuntimeClass() {
        return Fraction.class;
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/fraction/FractionField$LazyHolder.class */
    private static class LazyHolder {
        private static final FractionField INSTANCE = new FractionField();

        private LazyHolder() {
        }
    }

    private Object readResolve() {
        return LazyHolder.INSTANCE;
    }
}
