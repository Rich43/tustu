package org.apache.commons.math3.ml.distance;

import java.io.Serializable;
import org.apache.commons.math3.exception.DimensionMismatchException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ml/distance/DistanceMeasure.class */
public interface DistanceMeasure extends Serializable {
    double compute(double[] dArr, double[] dArr2) throws DimensionMismatchException;
}
