package org.apache.commons.math3.optimization.direct;

import java.util.Comparator;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optimization.PointValuePair;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/direct/MultiDirectionalSimplex.class */
public class MultiDirectionalSimplex extends AbstractSimplex {
    private static final double DEFAULT_KHI = 2.0d;
    private static final double DEFAULT_GAMMA = 0.5d;
    private final double khi;
    private final double gamma;

    public MultiDirectionalSimplex(int n2) {
        this(n2, 1.0d);
    }

    public MultiDirectionalSimplex(int n2, double sideLength) {
        this(n2, sideLength, 2.0d, 0.5d);
    }

    public MultiDirectionalSimplex(int n2, double khi, double gamma) {
        this(n2, 1.0d, khi, gamma);
    }

    public MultiDirectionalSimplex(int n2, double sideLength, double khi, double gamma) {
        super(n2, sideLength);
        this.khi = khi;
        this.gamma = gamma;
    }

    public MultiDirectionalSimplex(double[] steps) {
        this(steps, 2.0d, 0.5d);
    }

    public MultiDirectionalSimplex(double[] steps, double khi, double gamma) {
        super(steps);
        this.khi = khi;
        this.gamma = gamma;
    }

    public MultiDirectionalSimplex(double[][] referenceSimplex) {
        this(referenceSimplex, 2.0d, 0.5d);
    }

    public MultiDirectionalSimplex(double[][] referenceSimplex, double khi, double gamma) {
        super(referenceSimplex);
        this.khi = khi;
        this.gamma = gamma;
    }

    @Override // org.apache.commons.math3.optimization.direct.AbstractSimplex
    public void iterate(MultivariateFunction evaluationFunction, Comparator<PointValuePair> comparator) {
        PointValuePair[] original = getPoints();
        PointValuePair best = original[0];
        PointValuePair reflected = evaluateNewSimplex(evaluationFunction, original, 1.0d, comparator);
        if (comparator.compare(reflected, best) < 0) {
            PointValuePair[] reflectedSimplex = getPoints();
            PointValuePair expanded = evaluateNewSimplex(evaluationFunction, original, this.khi, comparator);
            if (comparator.compare(reflected, expanded) <= 0) {
                setPoints(reflectedSimplex);
                return;
            }
            return;
        }
        evaluateNewSimplex(evaluationFunction, original, this.gamma, comparator);
    }

    private PointValuePair evaluateNewSimplex(MultivariateFunction evaluationFunction, PointValuePair[] original, double coeff, Comparator<PointValuePair> comparator) {
        double[] xSmallest = original[0].getPointRef();
        setPoint(0, original[0]);
        int dim = getDimension();
        for (int i2 = 1; i2 < getSize(); i2++) {
            double[] xOriginal = original[i2].getPointRef();
            double[] xTransformed = new double[dim];
            for (int j2 = 0; j2 < dim; j2++) {
                xTransformed[j2] = xSmallest[j2] + (coeff * (xSmallest[j2] - xOriginal[j2]));
            }
            setPoint(i2, new PointValuePair(xTransformed, Double.NaN, false));
        }
        evaluate(evaluationFunction, comparator);
        return getPoint(0);
    }
}
