package org.apache.commons.math3.ode;

import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/ParameterizedWrapper.class */
class ParameterizedWrapper implements ParameterizedODE {
    private final FirstOrderDifferentialEquations fode;

    ParameterizedWrapper(FirstOrderDifferentialEquations ode) {
        this.fode = ode;
    }

    public int getDimension() {
        return this.fode.getDimension();
    }

    public void computeDerivatives(double t2, double[] y2, double[] yDot) throws MaxCountExceededException, DimensionMismatchException {
        this.fode.computeDerivatives(t2, y2, yDot);
    }

    @Override // org.apache.commons.math3.ode.Parameterizable
    public Collection<String> getParametersNames() {
        return new ArrayList();
    }

    @Override // org.apache.commons.math3.ode.Parameterizable
    public boolean isSupported(String name) {
        return false;
    }

    @Override // org.apache.commons.math3.ode.ParameterizedODE
    public double getParameter(String name) throws UnknownParameterException {
        if (!isSupported(name)) {
            throw new UnknownParameterException(name);
        }
        return Double.NaN;
    }

    @Override // org.apache.commons.math3.ode.ParameterizedODE
    public void setParameter(String name, double value) {
    }
}
