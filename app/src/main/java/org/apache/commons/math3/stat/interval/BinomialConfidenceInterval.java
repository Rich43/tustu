package org.apache.commons.math3.stat.interval;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.OutOfRangeException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/interval/BinomialConfidenceInterval.class */
public interface BinomialConfidenceInterval {
    ConfidenceInterval createInterval(int i2, int i3, double d2) throws OutOfRangeException, NotStrictlyPositiveException, NotPositiveException, NumberIsTooLargeException;
}
