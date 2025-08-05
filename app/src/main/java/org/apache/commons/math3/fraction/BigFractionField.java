package org.apache.commons.math3.fraction;

import java.io.Serializable;
import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/fraction/BigFractionField.class */
public class BigFractionField implements Field<BigFraction>, Serializable {
    private static final long serialVersionUID = -1699294557189741703L;

    private BigFractionField() {
    }

    public static BigFractionField getInstance() {
        return LazyHolder.INSTANCE;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.Field
    public BigFraction getOne() {
        return BigFraction.ONE;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.Field
    public BigFraction getZero() {
        return BigFraction.ZERO;
    }

    @Override // org.apache.commons.math3.Field
    public Class<? extends FieldElement<BigFraction>> getRuntimeClass() {
        return BigFraction.class;
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/fraction/BigFractionField$LazyHolder.class */
    private static class LazyHolder {
        private static final BigFractionField INSTANCE = new BigFractionField();

        private LazyHolder() {
        }
    }

    private Object readResolve() {
        return LazyHolder.INSTANCE;
    }
}
