package org.apache.commons.math3.util;

import java.io.Serializable;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/DefaultTransformer.class */
public class DefaultTransformer implements NumberTransformer, Serializable {
    private static final long serialVersionUID = 4019938025047800455L;

    @Override // org.apache.commons.math3.util.NumberTransformer
    public double transform(Object o2) throws MathIllegalArgumentException {
        if (o2 == null) {
            throw new NullArgumentException(LocalizedFormats.OBJECT_TRANSFORMATION, new Object[0]);
        }
        if (o2 instanceof Number) {
            return ((Number) o2).doubleValue();
        }
        try {
            return Double.parseDouble(o2.toString());
        } catch (NumberFormatException e2) {
            throw new MathIllegalArgumentException(LocalizedFormats.CANNOT_TRANSFORM_TO_DOUBLE, o2.toString());
        }
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        return other instanceof DefaultTransformer;
    }

    public int hashCode() {
        return 401993047;
    }
}
