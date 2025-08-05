package org.apache.commons.math3.filter;

import org.apache.commons.math3.linear.RealMatrix;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/filter/MeasurementModel.class */
public interface MeasurementModel {
    RealMatrix getMeasurementMatrix();

    RealMatrix getMeasurementNoise();
}
