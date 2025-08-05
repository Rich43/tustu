package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.MathIllegalArgumentException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/PivotingStrategyInterface.class */
public interface PivotingStrategyInterface {
    int pivotIndex(double[] dArr, int i2, int i3) throws MathIllegalArgumentException;
}
