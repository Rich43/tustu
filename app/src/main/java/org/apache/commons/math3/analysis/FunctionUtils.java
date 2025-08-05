package org.apache.commons.math3.analysis;

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.MultivariateDifferentiableFunction;
import org.apache.commons.math3.analysis.differentiation.MultivariateDifferentiableVectorFunction;
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction;
import org.apache.commons.math3.analysis.function.Identity;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/FunctionUtils.class */
public class FunctionUtils {
    private FunctionUtils() {
    }

    public static UnivariateFunction compose(final UnivariateFunction... f2) {
        return new UnivariateFunction() { // from class: org.apache.commons.math3.analysis.FunctionUtils.1
            @Override // org.apache.commons.math3.analysis.UnivariateFunction
            public double value(double x2) {
                double r2 = x2;
                for (int i2 = f2.length - 1; i2 >= 0; i2--) {
                    r2 = f2[i2].value(r2);
                }
                return r2;
            }
        };
    }

    public static UnivariateDifferentiableFunction compose(final UnivariateDifferentiableFunction... f2) {
        return new UnivariateDifferentiableFunction() { // from class: org.apache.commons.math3.analysis.FunctionUtils.2
            @Override // org.apache.commons.math3.analysis.UnivariateFunction
            public double value(double t2) {
                double r2 = t2;
                for (int i2 = f2.length - 1; i2 >= 0; i2--) {
                    r2 = f2[i2].value(r2);
                }
                return r2;
            }

            @Override // org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction
            public DerivativeStructure value(DerivativeStructure t2) throws DimensionMismatchException {
                DerivativeStructure r2 = t2;
                for (int i2 = f2.length - 1; i2 >= 0; i2--) {
                    r2 = f2[i2].value(r2);
                }
                return r2;
            }
        };
    }

    @Deprecated
    public static DifferentiableUnivariateFunction compose(final DifferentiableUnivariateFunction... f2) {
        return new DifferentiableUnivariateFunction() { // from class: org.apache.commons.math3.analysis.FunctionUtils.3
            @Override // org.apache.commons.math3.analysis.UnivariateFunction
            public double value(double x2) {
                double r2 = x2;
                for (int i2 = f2.length - 1; i2 >= 0; i2--) {
                    r2 = f2[i2].value(r2);
                }
                return r2;
            }

            @Override // org.apache.commons.math3.analysis.DifferentiableUnivariateFunction
            public UnivariateFunction derivative() {
                return new UnivariateFunction() { // from class: org.apache.commons.math3.analysis.FunctionUtils.3.1
                    @Override // org.apache.commons.math3.analysis.UnivariateFunction
                    public double value(double x2) {
                        double p2 = 1.0d;
                        double r2 = x2;
                        for (int i2 = f2.length - 1; i2 >= 0; i2--) {
                            p2 *= f2[i2].derivative().value(r2);
                            r2 = f2[i2].value(r2);
                        }
                        return p2;
                    }
                };
            }
        };
    }

    public static UnivariateFunction add(final UnivariateFunction... f2) {
        return new UnivariateFunction() { // from class: org.apache.commons.math3.analysis.FunctionUtils.4
            @Override // org.apache.commons.math3.analysis.UnivariateFunction
            public double value(double x2) {
                double r2 = f2[0].value(x2);
                for (int i2 = 1; i2 < f2.length; i2++) {
                    r2 += f2[i2].value(x2);
                }
                return r2;
            }
        };
    }

    public static UnivariateDifferentiableFunction add(final UnivariateDifferentiableFunction... f2) {
        return new UnivariateDifferentiableFunction() { // from class: org.apache.commons.math3.analysis.FunctionUtils.5
            @Override // org.apache.commons.math3.analysis.UnivariateFunction
            public double value(double t2) {
                double r2 = f2[0].value(t2);
                for (int i2 = 1; i2 < f2.length; i2++) {
                    r2 += f2[i2].value(t2);
                }
                return r2;
            }

            @Override // org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction
            public DerivativeStructure value(DerivativeStructure t2) throws DimensionMismatchException {
                DerivativeStructure r2 = f2[0].value(t2);
                for (int i2 = 1; i2 < f2.length; i2++) {
                    r2 = r2.add(f2[i2].value(t2));
                }
                return r2;
            }
        };
    }

    @Deprecated
    public static DifferentiableUnivariateFunction add(final DifferentiableUnivariateFunction... f2) {
        return new DifferentiableUnivariateFunction() { // from class: org.apache.commons.math3.analysis.FunctionUtils.6
            @Override // org.apache.commons.math3.analysis.UnivariateFunction
            public double value(double x2) {
                double r2 = f2[0].value(x2);
                for (int i2 = 1; i2 < f2.length; i2++) {
                    r2 += f2[i2].value(x2);
                }
                return r2;
            }

            @Override // org.apache.commons.math3.analysis.DifferentiableUnivariateFunction
            public UnivariateFunction derivative() {
                return new UnivariateFunction() { // from class: org.apache.commons.math3.analysis.FunctionUtils.6.1
                    @Override // org.apache.commons.math3.analysis.UnivariateFunction
                    public double value(double x2) {
                        double r2 = f2[0].derivative().value(x2);
                        for (int i2 = 1; i2 < f2.length; i2++) {
                            r2 += f2[i2].derivative().value(x2);
                        }
                        return r2;
                    }
                };
            }
        };
    }

    public static UnivariateFunction multiply(final UnivariateFunction... f2) {
        return new UnivariateFunction() { // from class: org.apache.commons.math3.analysis.FunctionUtils.7
            @Override // org.apache.commons.math3.analysis.UnivariateFunction
            public double value(double x2) {
                double r2 = f2[0].value(x2);
                for (int i2 = 1; i2 < f2.length; i2++) {
                    r2 *= f2[i2].value(x2);
                }
                return r2;
            }
        };
    }

    public static UnivariateDifferentiableFunction multiply(final UnivariateDifferentiableFunction... f2) {
        return new UnivariateDifferentiableFunction() { // from class: org.apache.commons.math3.analysis.FunctionUtils.8
            @Override // org.apache.commons.math3.analysis.UnivariateFunction
            public double value(double t2) {
                double r2 = f2[0].value(t2);
                for (int i2 = 1; i2 < f2.length; i2++) {
                    r2 *= f2[i2].value(t2);
                }
                return r2;
            }

            @Override // org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction
            public DerivativeStructure value(DerivativeStructure t2) throws DimensionMismatchException {
                DerivativeStructure r2 = f2[0].value(t2);
                for (int i2 = 1; i2 < f2.length; i2++) {
                    r2 = r2.multiply(f2[i2].value(t2));
                }
                return r2;
            }
        };
    }

    @Deprecated
    public static DifferentiableUnivariateFunction multiply(final DifferentiableUnivariateFunction... f2) {
        return new DifferentiableUnivariateFunction() { // from class: org.apache.commons.math3.analysis.FunctionUtils.9
            @Override // org.apache.commons.math3.analysis.UnivariateFunction
            public double value(double x2) {
                double r2 = f2[0].value(x2);
                for (int i2 = 1; i2 < f2.length; i2++) {
                    r2 *= f2[i2].value(x2);
                }
                return r2;
            }

            @Override // org.apache.commons.math3.analysis.DifferentiableUnivariateFunction
            public UnivariateFunction derivative() {
                return new UnivariateFunction() { // from class: org.apache.commons.math3.analysis.FunctionUtils.9.1
                    @Override // org.apache.commons.math3.analysis.UnivariateFunction
                    public double value(double x2) {
                        double sum = 0.0d;
                        for (int i2 = 0; i2 < f2.length; i2++) {
                            double prod = f2[i2].derivative().value(x2);
                            for (int j2 = 0; j2 < f2.length; j2++) {
                                if (i2 != j2) {
                                    prod *= f2[j2].value(x2);
                                }
                            }
                            sum += prod;
                        }
                        return sum;
                    }
                };
            }
        };
    }

    public static UnivariateFunction combine(final BivariateFunction combiner, final UnivariateFunction f2, final UnivariateFunction g2) {
        return new UnivariateFunction() { // from class: org.apache.commons.math3.analysis.FunctionUtils.10
            @Override // org.apache.commons.math3.analysis.UnivariateFunction
            public double value(double x2) {
                return combiner.value(f2.value(x2), g2.value(x2));
            }
        };
    }

    public static MultivariateFunction collector(final BivariateFunction combiner, final UnivariateFunction f2, final double initialValue) {
        return new MultivariateFunction() { // from class: org.apache.commons.math3.analysis.FunctionUtils.11
            @Override // org.apache.commons.math3.analysis.MultivariateFunction
            public double value(double[] point) {
                double result = combiner.value(initialValue, f2.value(point[0]));
                for (int i2 = 1; i2 < point.length; i2++) {
                    result = combiner.value(result, f2.value(point[i2]));
                }
                return result;
            }
        };
    }

    public static MultivariateFunction collector(BivariateFunction combiner, double initialValue) {
        return collector(combiner, new Identity(), initialValue);
    }

    public static UnivariateFunction fix1stArgument(final BivariateFunction f2, final double fixed) {
        return new UnivariateFunction() { // from class: org.apache.commons.math3.analysis.FunctionUtils.12
            @Override // org.apache.commons.math3.analysis.UnivariateFunction
            public double value(double x2) {
                return f2.value(fixed, x2);
            }
        };
    }

    public static UnivariateFunction fix2ndArgument(final BivariateFunction f2, final double fixed) {
        return new UnivariateFunction() { // from class: org.apache.commons.math3.analysis.FunctionUtils.13
            @Override // org.apache.commons.math3.analysis.UnivariateFunction
            public double value(double x2) {
                return f2.value(x2, fixed);
            }
        };
    }

    public static double[] sample(UnivariateFunction f2, double min, double max, int n2) throws NotStrictlyPositiveException, NumberIsTooLargeException {
        if (n2 <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.NOT_POSITIVE_NUMBER_OF_SAMPLES, Integer.valueOf(n2));
        }
        if (min >= max) {
            throw new NumberIsTooLargeException(Double.valueOf(min), Double.valueOf(max), false);
        }
        double[] s2 = new double[n2];
        double h2 = (max - min) / n2;
        for (int i2 = 0; i2 < n2; i2++) {
            s2[i2] = f2.value(min + (i2 * h2));
        }
        return s2;
    }

    @Deprecated
    public static DifferentiableUnivariateFunction toDifferentiableUnivariateFunction(final UnivariateDifferentiableFunction f2) {
        return new DifferentiableUnivariateFunction() { // from class: org.apache.commons.math3.analysis.FunctionUtils.14
            @Override // org.apache.commons.math3.analysis.UnivariateFunction
            public double value(double x2) {
                return f2.value(x2);
            }

            @Override // org.apache.commons.math3.analysis.DifferentiableUnivariateFunction
            public UnivariateFunction derivative() {
                return new UnivariateFunction() { // from class: org.apache.commons.math3.analysis.FunctionUtils.14.1
                    @Override // org.apache.commons.math3.analysis.UnivariateFunction
                    public double value(double x2) {
                        return f2.value(new DerivativeStructure(1, 1, 0, x2)).getPartialDerivative(1);
                    }
                };
            }
        };
    }

    @Deprecated
    public static UnivariateDifferentiableFunction toUnivariateDifferential(final DifferentiableUnivariateFunction f2) {
        return new UnivariateDifferentiableFunction() { // from class: org.apache.commons.math3.analysis.FunctionUtils.15
            @Override // org.apache.commons.math3.analysis.UnivariateFunction
            public double value(double x2) {
                return f2.value(x2);
            }

            @Override // org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction
            public DerivativeStructure value(DerivativeStructure t2) throws NumberIsTooLargeException {
                switch (t2.getOrder()) {
                    case 0:
                        return new DerivativeStructure(t2.getFreeParameters(), 0, f2.value(t2.getValue()));
                    case 1:
                        int parameters = t2.getFreeParameters();
                        double[] derivatives = new double[parameters + 1];
                        derivatives[0] = f2.value(t2.getValue());
                        double fPrime = f2.derivative().value(t2.getValue());
                        int[] orders = new int[parameters];
                        for (int i2 = 0; i2 < parameters; i2++) {
                            orders[i2] = 1;
                            derivatives[i2 + 1] = fPrime * t2.getPartialDerivative(orders);
                            orders[i2] = 0;
                        }
                        return new DerivativeStructure(parameters, 1, derivatives);
                    default:
                        throw new NumberIsTooLargeException(Integer.valueOf(t2.getOrder()), 1, true);
                }
            }
        };
    }

    @Deprecated
    public static DifferentiableMultivariateFunction toDifferentiableMultivariateFunction(final MultivariateDifferentiableFunction f2) {
        return new DifferentiableMultivariateFunction() { // from class: org.apache.commons.math3.analysis.FunctionUtils.16
            @Override // org.apache.commons.math3.analysis.MultivariateFunction
            public double value(double[] x2) {
                return f2.value(x2);
            }

            @Override // org.apache.commons.math3.analysis.DifferentiableMultivariateFunction
            public MultivariateFunction partialDerivative(final int k2) {
                return new MultivariateFunction() { // from class: org.apache.commons.math3.analysis.FunctionUtils.16.1
                    @Override // org.apache.commons.math3.analysis.MultivariateFunction
                    public double value(double[] x2) throws MathIllegalArgumentException {
                        int n2 = x2.length;
                        DerivativeStructure[] dsX = new DerivativeStructure[n2];
                        for (int i2 = 0; i2 < n2; i2++) {
                            if (i2 == k2) {
                                dsX[i2] = new DerivativeStructure(1, 1, 0, x2[i2]);
                            } else {
                                dsX[i2] = new DerivativeStructure(1, 1, x2[i2]);
                            }
                        }
                        DerivativeStructure y2 = f2.value(dsX);
                        return y2.getPartialDerivative(1);
                    }
                };
            }

            @Override // org.apache.commons.math3.analysis.DifferentiableMultivariateFunction
            public MultivariateVectorFunction gradient() {
                return new MultivariateVectorFunction() { // from class: org.apache.commons.math3.analysis.FunctionUtils.16.2
                    @Override // org.apache.commons.math3.analysis.MultivariateVectorFunction
                    public double[] value(double[] x2) throws MathIllegalArgumentException {
                        int n2 = x2.length;
                        DerivativeStructure[] dsX = new DerivativeStructure[n2];
                        for (int i2 = 0; i2 < n2; i2++) {
                            dsX[i2] = new DerivativeStructure(n2, 1, i2, x2[i2]);
                        }
                        DerivativeStructure y2 = f2.value(dsX);
                        double[] gradient = new double[n2];
                        int[] orders = new int[n2];
                        for (int i3 = 0; i3 < n2; i3++) {
                            orders[i3] = 1;
                            gradient[i3] = y2.getPartialDerivative(orders);
                            orders[i3] = 0;
                        }
                        return gradient;
                    }
                };
            }
        };
    }

    @Deprecated
    public static MultivariateDifferentiableFunction toMultivariateDifferentiableFunction(final DifferentiableMultivariateFunction f2) {
        return new MultivariateDifferentiableFunction() { // from class: org.apache.commons.math3.analysis.FunctionUtils.17
            @Override // org.apache.commons.math3.analysis.MultivariateFunction
            public double value(double[] x2) {
                return f2.value(x2);
            }

            @Override // org.apache.commons.math3.analysis.differentiation.MultivariateDifferentiableFunction
            public DerivativeStructure value(DerivativeStructure[] t2) throws IllegalArgumentException {
                int parameters = t2[0].getFreeParameters();
                int order = t2[0].getOrder();
                int n2 = t2.length;
                if (order > 1) {
                    throw new NumberIsTooLargeException(Integer.valueOf(order), 1, true);
                }
                for (int i2 = 0; i2 < n2; i2++) {
                    if (t2[i2].getFreeParameters() != parameters) {
                        throw new DimensionMismatchException(t2[i2].getFreeParameters(), parameters);
                    }
                    if (t2[i2].getOrder() != order) {
                        throw new DimensionMismatchException(t2[i2].getOrder(), order);
                    }
                }
                double[] point = new double[n2];
                for (int i3 = 0; i3 < n2; i3++) {
                    point[i3] = t2[i3].getValue();
                }
                double value = f2.value(point);
                double[] gradient = f2.gradient().value(point);
                double[] derivatives = new double[parameters + 1];
                derivatives[0] = value;
                int[] orders = new int[parameters];
                for (int i4 = 0; i4 < parameters; i4++) {
                    orders[i4] = 1;
                    for (int j2 = 0; j2 < n2; j2++) {
                        int i5 = i4 + 1;
                        derivatives[i5] = derivatives[i5] + (gradient[j2] * t2[j2].getPartialDerivative(orders));
                    }
                    orders[i4] = 0;
                }
                return new DerivativeStructure(parameters, order, derivatives);
            }
        };
    }

    @Deprecated
    public static DifferentiableMultivariateVectorFunction toDifferentiableMultivariateVectorFunction(final MultivariateDifferentiableVectorFunction f2) {
        return new DifferentiableMultivariateVectorFunction() { // from class: org.apache.commons.math3.analysis.FunctionUtils.18
            @Override // org.apache.commons.math3.analysis.MultivariateVectorFunction
            public double[] value(double[] x2) {
                return f2.value(x2);
            }

            @Override // org.apache.commons.math3.analysis.DifferentiableMultivariateVectorFunction
            public MultivariateMatrixFunction jacobian() {
                return new MultivariateMatrixFunction() { // from class: org.apache.commons.math3.analysis.FunctionUtils.18.1
                    @Override // org.apache.commons.math3.analysis.MultivariateMatrixFunction
                    public double[][] value(double[] x2) throws MathIllegalArgumentException {
                        int n2 = x2.length;
                        DerivativeStructure[] dsX = new DerivativeStructure[n2];
                        for (int i2 = 0; i2 < n2; i2++) {
                            dsX[i2] = new DerivativeStructure(n2, 1, i2, x2[i2]);
                        }
                        DerivativeStructure[] y2 = f2.value(dsX);
                        double[][] jacobian = new double[y2.length][n2];
                        int[] orders = new int[n2];
                        for (int i3 = 0; i3 < y2.length; i3++) {
                            for (int j2 = 0; j2 < n2; j2++) {
                                orders[j2] = 1;
                                jacobian[i3][j2] = y2[i3].getPartialDerivative(orders);
                                orders[j2] = 0;
                            }
                        }
                        return jacobian;
                    }
                };
            }
        };
    }

    @Deprecated
    public static MultivariateDifferentiableVectorFunction toMultivariateDifferentiableVectorFunction(final DifferentiableMultivariateVectorFunction f2) {
        return new MultivariateDifferentiableVectorFunction() { // from class: org.apache.commons.math3.analysis.FunctionUtils.19
            @Override // org.apache.commons.math3.analysis.MultivariateVectorFunction
            public double[] value(double[] x2) {
                return f2.value(x2);
            }

            @Override // org.apache.commons.math3.analysis.differentiation.MultivariateDifferentiableVectorFunction
            public DerivativeStructure[] value(DerivativeStructure[] t2) throws IllegalArgumentException {
                int parameters = t2[0].getFreeParameters();
                int order = t2[0].getOrder();
                int n2 = t2.length;
                if (order > 1) {
                    throw new NumberIsTooLargeException(Integer.valueOf(order), 1, true);
                }
                for (int i2 = 0; i2 < n2; i2++) {
                    if (t2[i2].getFreeParameters() != parameters) {
                        throw new DimensionMismatchException(t2[i2].getFreeParameters(), parameters);
                    }
                    if (t2[i2].getOrder() != order) {
                        throw new DimensionMismatchException(t2[i2].getOrder(), order);
                    }
                }
                double[] point = new double[n2];
                for (int i3 = 0; i3 < n2; i3++) {
                    point[i3] = t2[i3].getValue();
                }
                double[] value = f2.value(point);
                double[][] jacobian = f2.jacobian().value(point);
                DerivativeStructure[] merged = new DerivativeStructure[value.length];
                for (int k2 = 0; k2 < merged.length; k2++) {
                    double[] derivatives = new double[parameters + 1];
                    derivatives[0] = value[k2];
                    int[] orders = new int[parameters];
                    for (int i4 = 0; i4 < parameters; i4++) {
                        orders[i4] = 1;
                        for (int j2 = 0; j2 < n2; j2++) {
                            int i5 = i4 + 1;
                            derivatives[i5] = derivatives[i5] + (jacobian[k2][j2] * t2[j2].getPartialDerivative(orders));
                        }
                        orders[i4] = 0;
                    }
                    merged[k2] = new DerivativeStructure(parameters, order, derivatives);
                }
                return merged;
            }
        };
    }
}
