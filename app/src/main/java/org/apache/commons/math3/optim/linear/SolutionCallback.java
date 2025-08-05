package org.apache.commons.math3.optim.linear;

import org.apache.commons.math3.optim.OptimizationData;
import org.apache.commons.math3.optim.PointValuePair;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/linear/SolutionCallback.class */
public class SolutionCallback implements OptimizationData {
    private SimplexTableau tableau;

    void setTableau(SimplexTableau tableau) {
        this.tableau = tableau;
    }

    public PointValuePair getSolution() {
        if (this.tableau != null) {
            return this.tableau.getSolution();
        }
        return null;
    }

    public boolean isSolutionOptimal() {
        if (this.tableau != null) {
            return this.tableau.isOptimal();
        }
        return false;
    }
}
