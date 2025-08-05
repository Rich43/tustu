package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.MathIllegalArgumentException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/NumberTransformer.class */
public interface NumberTransformer {
    double transform(Object obj) throws MathIllegalArgumentException;
}
