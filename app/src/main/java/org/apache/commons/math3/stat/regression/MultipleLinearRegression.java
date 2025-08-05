package org.apache.commons.math3.stat.regression;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/regression/MultipleLinearRegression.class */
public interface MultipleLinearRegression {
    double[] estimateRegressionParameters();

    double[][] estimateRegressionParametersVariance();

    double[] estimateResiduals();

    double estimateRegressandVariance();

    double[] estimateRegressionParametersStandardErrors();
}
