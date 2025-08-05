package org.apache.commons.math3.stat.regression;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoDataException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/regression/UpdatingMultipleLinearRegression.class */
public interface UpdatingMultipleLinearRegression {
    boolean hasIntercept();

    long getN();

    void addObservation(double[] dArr, double d2) throws ModelSpecificationException;

    void addObservations(double[][] dArr, double[] dArr2) throws ModelSpecificationException;

    void clear();

    RegressionResults regress() throws NoDataException, ModelSpecificationException;

    RegressionResults regress(int[] iArr) throws MathIllegalArgumentException;
}
