package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import java.util.Arrays;
import java.util.Comparator;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optim.OptimizationData;
import org.apache.commons.math3.optim.PointValuePair;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/nonlinear/scalar/noderiv/AbstractSimplex.class */
public abstract class AbstractSimplex implements OptimizationData {
    private PointValuePair[] simplex;
    private double[][] startConfiguration;
    private final int dimension;

    public abstract void iterate(MultivariateFunction multivariateFunction, Comparator<PointValuePair> comparator);

    protected AbstractSimplex(int n2) {
        this(n2, 1.0d);
    }

    protected AbstractSimplex(int n2, double sideLength) {
        this(createHypercubeSteps(n2, sideLength));
    }

    protected AbstractSimplex(double[] steps) {
        if (steps == null) {
            throw new NullArgumentException();
        }
        if (steps.length == 0) {
            throw new ZeroException();
        }
        this.dimension = steps.length;
        this.startConfiguration = new double[this.dimension][this.dimension];
        for (int i2 = 0; i2 < this.dimension; i2++) {
            double[] vertexI = this.startConfiguration[i2];
            for (int j2 = 0; j2 < i2 + 1; j2++) {
                if (steps[j2] == 0.0d) {
                    throw new ZeroException(LocalizedFormats.EQUAL_VERTICES_IN_SIMPLEX, new Object[0]);
                }
                System.arraycopy(steps, 0, vertexI, 0, j2 + 1);
            }
        }
    }

    protected AbstractSimplex(double[][] referenceSimplex) {
        if (referenceSimplex.length <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.SIMPLEX_NEED_ONE_POINT, Integer.valueOf(referenceSimplex.length));
        }
        this.dimension = referenceSimplex.length - 1;
        this.startConfiguration = new double[this.dimension][this.dimension];
        double[] ref0 = referenceSimplex[0];
        for (int i2 = 0; i2 < referenceSimplex.length; i2++) {
            double[] refI = referenceSimplex[i2];
            if (refI.length != this.dimension) {
                throw new DimensionMismatchException(refI.length, this.dimension);
            }
            for (int j2 = 0; j2 < i2; j2++) {
                double[] refJ = referenceSimplex[j2];
                boolean allEquals = true;
                int k2 = 0;
                while (true) {
                    if (k2 >= this.dimension) {
                        break;
                    }
                    if (refI[k2] == refJ[k2]) {
                        k2++;
                    } else {
                        allEquals = false;
                        break;
                    }
                }
                if (allEquals) {
                    throw new MathIllegalArgumentException(LocalizedFormats.EQUAL_VERTICES_IN_SIMPLEX, Integer.valueOf(i2), Integer.valueOf(j2));
                }
            }
            if (i2 > 0) {
                double[] confI = this.startConfiguration[i2 - 1];
                for (int k3 = 0; k3 < this.dimension; k3++) {
                    confI[k3] = refI[k3] - ref0[k3];
                }
            }
        }
    }

    public int getDimension() {
        return this.dimension;
    }

    public int getSize() {
        return this.simplex.length;
    }

    public void build(double[] startPoint) {
        if (this.dimension != startPoint.length) {
            throw new DimensionMismatchException(this.dimension, startPoint.length);
        }
        this.simplex = new PointValuePair[this.dimension + 1];
        this.simplex[0] = new PointValuePair(startPoint, Double.NaN);
        for (int i2 = 0; i2 < this.dimension; i2++) {
            double[] confI = this.startConfiguration[i2];
            double[] vertexI = new double[this.dimension];
            for (int k2 = 0; k2 < this.dimension; k2++) {
                vertexI[k2] = startPoint[k2] + confI[k2];
            }
            this.simplex[i2 + 1] = new PointValuePair(vertexI, Double.NaN);
        }
    }

    public void evaluate(MultivariateFunction evaluationFunction, Comparator<PointValuePair> comparator) {
        for (int i2 = 0; i2 < this.simplex.length; i2++) {
            PointValuePair vertex = this.simplex[i2];
            double[] point = vertex.getPointRef();
            if (Double.isNaN(vertex.getValue().doubleValue())) {
                this.simplex[i2] = new PointValuePair(point, evaluationFunction.value(point), false);
            }
        }
        Arrays.sort(this.simplex, comparator);
    }

    protected void replaceWorstPoint(PointValuePair pointValuePair, Comparator<PointValuePair> comparator) {
        for (int i2 = 0; i2 < this.dimension; i2++) {
            if (comparator.compare(this.simplex[i2], pointValuePair) > 0) {
                PointValuePair tmp = this.simplex[i2];
                this.simplex[i2] = pointValuePair;
                pointValuePair = tmp;
            }
        }
        this.simplex[this.dimension] = pointValuePair;
    }

    public PointValuePair[] getPoints() {
        PointValuePair[] copy = new PointValuePair[this.simplex.length];
        System.arraycopy(this.simplex, 0, copy, 0, this.simplex.length);
        return copy;
    }

    public PointValuePair getPoint(int index) {
        if (index < 0 || index >= this.simplex.length) {
            throw new OutOfRangeException(Integer.valueOf(index), 0, Integer.valueOf(this.simplex.length - 1));
        }
        return this.simplex[index];
    }

    protected void setPoint(int index, PointValuePair point) {
        if (index < 0 || index >= this.simplex.length) {
            throw new OutOfRangeException(Integer.valueOf(index), 0, Integer.valueOf(this.simplex.length - 1));
        }
        this.simplex[index] = point;
    }

    protected void setPoints(PointValuePair[] points) {
        if (points.length != this.simplex.length) {
            throw new DimensionMismatchException(points.length, this.simplex.length);
        }
        this.simplex = points;
    }

    private static double[] createHypercubeSteps(int n2, double sideLength) {
        double[] steps = new double[n2];
        for (int i2 = 0; i2 < n2; i2++) {
            steps[i2] = sideLength;
        }
        return steps;
    }
}
