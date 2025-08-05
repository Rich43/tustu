package org.apache.commons.math3.optimization.linear;

import java.util.Collection;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/linear/LinearOptimizer.class */
public interface LinearOptimizer {
    void setMaxIterations(int i2);

    int getMaxIterations();

    int getIterations();

    PointValuePair optimize(LinearObjectiveFunction linearObjectiveFunction, Collection<LinearConstraint> collection, GoalType goalType, boolean z2) throws MathIllegalStateException;
}
