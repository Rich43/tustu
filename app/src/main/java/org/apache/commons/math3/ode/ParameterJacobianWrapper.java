package org.apache.commons.math3.ode;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/ParameterJacobianWrapper.class */
class ParameterJacobianWrapper implements ParameterJacobianProvider {
    private final FirstOrderDifferentialEquations fode;
    private final ParameterizedODE pode;
    private final Map<String, Double> hParam = new HashMap();

    ParameterJacobianWrapper(FirstOrderDifferentialEquations fode, ParameterizedODE pode, ParameterConfiguration[] paramsAndSteps) {
        this.fode = fode;
        this.pode = pode;
        for (ParameterConfiguration param : paramsAndSteps) {
            String name = param.getParameterName();
            if (pode.isSupported(name)) {
                this.hParam.put(name, Double.valueOf(param.getHP()));
            }
        }
    }

    @Override // org.apache.commons.math3.ode.Parameterizable
    public Collection<String> getParametersNames() {
        return this.pode.getParametersNames();
    }

    @Override // org.apache.commons.math3.ode.Parameterizable
    public boolean isSupported(String name) {
        return this.pode.isSupported(name);
    }

    @Override // org.apache.commons.math3.ode.ParameterJacobianProvider
    public void computeParameterJacobian(double t2, double[] y2, double[] yDot, String paramName, double[] dFdP) throws UnknownParameterException, MaxCountExceededException, DimensionMismatchException {
        int n2 = this.fode.getDimension();
        if (this.pode.isSupported(paramName)) {
            double[] tmpDot = new double[n2];
            double p2 = this.pode.getParameter(paramName);
            double hP = this.hParam.get(paramName).doubleValue();
            this.pode.setParameter(paramName, p2 + hP);
            this.fode.computeDerivatives(t2, y2, tmpDot);
            for (int i2 = 0; i2 < n2; i2++) {
                dFdP[i2] = (tmpDot[i2] - yDot[i2]) / hP;
            }
            this.pode.setParameter(paramName, p2);
            return;
        }
        Arrays.fill(dFdP, 0, n2, 0.0d);
    }
}
