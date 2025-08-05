package org.apache.commons.math3.optim.nonlinear.scalar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.BaseMultiStartMultivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.random.RandomVectorGenerator;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/nonlinear/scalar/MultiStartMultivariateOptimizer.class */
public class MultiStartMultivariateOptimizer extends BaseMultiStartMultivariateOptimizer<PointValuePair> {
    private final MultivariateOptimizer optimizer;
    private final List<PointValuePair> optima;

    public MultiStartMultivariateOptimizer(MultivariateOptimizer optimizer, int starts, RandomVectorGenerator generator) throws NotStrictlyPositiveException, NullArgumentException {
        super(optimizer, starts, generator);
        this.optima = new ArrayList();
        this.optimizer = optimizer;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.optim.BaseMultiStartMultivariateOptimizer
    public PointValuePair[] getOptima() {
        Collections.sort(this.optima, getPairComparator());
        return (PointValuePair[]) this.optima.toArray(new PointValuePair[0]);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.math3.optim.BaseMultiStartMultivariateOptimizer
    public void store(PointValuePair optimum) {
        this.optima.add(optimum);
    }

    @Override // org.apache.commons.math3.optim.BaseMultiStartMultivariateOptimizer
    protected void clear() {
        this.optima.clear();
    }

    private Comparator<PointValuePair> getPairComparator() {
        return new Comparator<PointValuePair>() { // from class: org.apache.commons.math3.optim.nonlinear.scalar.MultiStartMultivariateOptimizer.1
            @Override // java.util.Comparator
            public int compare(PointValuePair o1, PointValuePair o2) {
                if (o1 == null) {
                    return o2 == null ? 0 : 1;
                }
                if (o2 == null) {
                    return -1;
                }
                double v1 = o1.getValue().doubleValue();
                double v2 = o2.getValue().doubleValue();
                return MultiStartMultivariateOptimizer.this.optimizer.getGoalType() == GoalType.MINIMIZE ? Double.compare(v1, v2) : Double.compare(v2, v1);
            }
        };
    }
}
