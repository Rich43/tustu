package org.apache.commons.math3.optim.nonlinear.vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.BaseMultiStartMultivariateOptimizer;
import org.apache.commons.math3.optim.PointVectorValuePair;
import org.apache.commons.math3.random.RandomVectorGenerator;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/nonlinear/vector/MultiStartMultivariateVectorOptimizer.class */
public class MultiStartMultivariateVectorOptimizer extends BaseMultiStartMultivariateOptimizer<PointVectorValuePair> {
    private final MultivariateVectorOptimizer optimizer;
    private final List<PointVectorValuePair> optima;

    public MultiStartMultivariateVectorOptimizer(MultivariateVectorOptimizer optimizer, int starts, RandomVectorGenerator generator) throws NotStrictlyPositiveException, NullArgumentException {
        super(optimizer, starts, generator);
        this.optima = new ArrayList();
        this.optimizer = optimizer;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.optim.BaseMultiStartMultivariateOptimizer
    public PointVectorValuePair[] getOptima() {
        Collections.sort(this.optima, getPairComparator());
        return (PointVectorValuePair[]) this.optima.toArray(new PointVectorValuePair[0]);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.math3.optim.BaseMultiStartMultivariateOptimizer
    public void store(PointVectorValuePair optimum) {
        this.optima.add(optimum);
    }

    @Override // org.apache.commons.math3.optim.BaseMultiStartMultivariateOptimizer
    protected void clear() {
        this.optima.clear();
    }

    private Comparator<PointVectorValuePair> getPairComparator() {
        return new Comparator<PointVectorValuePair>() { // from class: org.apache.commons.math3.optim.nonlinear.vector.MultiStartMultivariateVectorOptimizer.1
            private final RealVector target;
            private final RealMatrix weight;

            {
                this.target = new ArrayRealVector(MultiStartMultivariateVectorOptimizer.this.optimizer.getTarget(), false);
                this.weight = MultiStartMultivariateVectorOptimizer.this.optimizer.getWeight();
            }

            @Override // java.util.Comparator
            public int compare(PointVectorValuePair o1, PointVectorValuePair o2) {
                if (o1 == null) {
                    return o2 == null ? 0 : 1;
                }
                if (o2 == null) {
                    return -1;
                }
                return Double.compare(weightedResidual(o1), weightedResidual(o2));
            }

            private double weightedResidual(PointVectorValuePair pv) throws OutOfRangeException, DimensionMismatchException {
                RealVector v2 = new ArrayRealVector(pv.getValueRef(), false);
                RealVector r2 = this.target.subtract(v2);
                return r2.dotProduct(this.weight.operate(r2));
            }
        };
    }
}
