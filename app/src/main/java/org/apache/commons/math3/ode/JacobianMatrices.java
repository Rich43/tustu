package org.apache.commons.math3.ode;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/JacobianMatrices.class */
public class JacobianMatrices {
    private ExpandableStatefulODE efode;
    private int index;
    private MainStateJacobianProvider jode;
    private ParameterizedODE pode;
    private int stateDim;
    private ParameterConfiguration[] selectedParameters;
    private List<ParameterJacobianProvider> jacobianProviders;
    private int paramDim;
    private boolean dirtyParameter;
    private double[] matricesData;

    public JacobianMatrices(FirstOrderDifferentialEquations fode, double[] hY, String... parameters) throws DimensionMismatchException {
        this(new MainStateJacobianWrapper(fode, hY), parameters);
    }

    public JacobianMatrices(MainStateJacobianProvider jode, String... parameters) {
        this.efode = null;
        this.index = -1;
        this.jode = jode;
        this.pode = null;
        this.stateDim = jode.getDimension();
        if (parameters == null) {
            this.selectedParameters = null;
            this.paramDim = 0;
        } else {
            this.selectedParameters = new ParameterConfiguration[parameters.length];
            for (int i2 = 0; i2 < parameters.length; i2++) {
                this.selectedParameters[i2] = new ParameterConfiguration(parameters[i2], Double.NaN);
            }
            this.paramDim = parameters.length;
        }
        this.dirtyParameter = false;
        this.jacobianProviders = new ArrayList();
        this.matricesData = new double[(this.stateDim + this.paramDim) * this.stateDim];
        for (int i3 = 0; i3 < this.stateDim; i3++) {
            this.matricesData[i3 * (this.stateDim + 1)] = 1.0d;
        }
    }

    public void registerVariationalEquations(ExpandableStatefulODE expandable) throws MismatchedEquations, DimensionMismatchException {
        FirstOrderDifferentialEquations ode = this.jode instanceof MainStateJacobianWrapper ? ((MainStateJacobianWrapper) this.jode).ode : this.jode;
        if (expandable.getPrimary() != ode) {
            throw new MismatchedEquations();
        }
        this.efode = expandable;
        this.index = this.efode.addSecondaryEquations(new JacobiansSecondaryEquations());
        this.efode.setSecondaryState(this.index, this.matricesData);
    }

    public void addParameterJacobianProvider(ParameterJacobianProvider provider) {
        this.jacobianProviders.add(provider);
    }

    public void setParameterizedODE(ParameterizedODE parameterizedOde) {
        this.pode = parameterizedOde;
        this.dirtyParameter = true;
    }

    public void setParameterStep(String parameter, double hP) throws UnknownParameterException {
        ParameterConfiguration[] arr$ = this.selectedParameters;
        for (ParameterConfiguration param : arr$) {
            if (parameter.equals(param.getParameterName())) {
                param.setHP(hP);
                this.dirtyParameter = true;
                return;
            }
        }
        throw new UnknownParameterException(parameter);
    }

    public void setInitialMainStateJacobian(double[][] dYdY0) throws DimensionMismatchException {
        checkDimension(this.stateDim, dYdY0);
        checkDimension(this.stateDim, dYdY0[0]);
        int i2 = 0;
        for (double[] row : dYdY0) {
            System.arraycopy(row, 0, this.matricesData, i2, this.stateDim);
            i2 += this.stateDim;
        }
        if (this.efode != null) {
            this.efode.setSecondaryState(this.index, this.matricesData);
        }
    }

    public void setInitialParameterJacobian(String pName, double[] dYdP) throws UnknownParameterException, DimensionMismatchException {
        checkDimension(this.stateDim, dYdP);
        int i2 = this.stateDim * this.stateDim;
        ParameterConfiguration[] arr$ = this.selectedParameters;
        for (ParameterConfiguration param : arr$) {
            if (pName.equals(param.getParameterName())) {
                System.arraycopy(dYdP, 0, this.matricesData, i2, this.stateDim);
                if (this.efode != null) {
                    this.efode.setSecondaryState(this.index, this.matricesData);
                    return;
                }
                return;
            }
            i2 += this.stateDim;
        }
        throw new UnknownParameterException(pName);
    }

    public void getCurrentMainSetJacobian(double[][] dYdY0) {
        double[] p2 = this.efode.getSecondaryState(this.index);
        int j2 = 0;
        for (int i2 = 0; i2 < this.stateDim; i2++) {
            System.arraycopy(p2, j2, dYdY0[i2], 0, this.stateDim);
            j2 += this.stateDim;
        }
    }

    public void getCurrentParameterJacobian(String pName, double[] dYdP) {
        double[] p2 = this.efode.getSecondaryState(this.index);
        int i2 = this.stateDim * this.stateDim;
        ParameterConfiguration[] arr$ = this.selectedParameters;
        for (ParameterConfiguration param : arr$) {
            if (param.getParameterName().equals(pName)) {
                System.arraycopy(p2, i2, dYdP, 0, this.stateDim);
                return;
            }
            i2 += this.stateDim;
        }
    }

    private void checkDimension(int expected, Object array) throws DimensionMismatchException {
        int arrayDimension = array == null ? 0 : Array.getLength(array);
        if (arrayDimension != expected) {
            throw new DimensionMismatchException(arrayDimension, expected);
        }
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/JacobianMatrices$JacobiansSecondaryEquations.class */
    private class JacobiansSecondaryEquations implements SecondaryEquations {
        private JacobiansSecondaryEquations() {
        }

        @Override // org.apache.commons.math3.ode.SecondaryEquations
        public int getDimension() {
            return JacobianMatrices.this.stateDim * (JacobianMatrices.this.stateDim + JacobianMatrices.this.paramDim);
        }

        @Override // org.apache.commons.math3.ode.SecondaryEquations
        public void computeDerivatives(double t2, double[] y2, double[] yDot, double[] z2, double[] zDot) throws UnknownParameterException, MaxCountExceededException, DimensionMismatchException {
            if (JacobianMatrices.this.dirtyParameter && JacobianMatrices.this.paramDim != 0) {
                JacobianMatrices.this.jacobianProviders.add(new ParameterJacobianWrapper(JacobianMatrices.this.jode, JacobianMatrices.this.pode, JacobianMatrices.this.selectedParameters));
                JacobianMatrices.this.dirtyParameter = false;
            }
            double[][] dFdY = new double[JacobianMatrices.this.stateDim][JacobianMatrices.this.stateDim];
            JacobianMatrices.this.jode.computeMainStateJacobian(t2, y2, yDot, dFdY);
            for (int i2 = 0; i2 < JacobianMatrices.this.stateDim; i2++) {
                double[] dFdYi = dFdY[i2];
                for (int j2 = 0; j2 < JacobianMatrices.this.stateDim; j2++) {
                    double s2 = 0.0d;
                    int startIndex = j2;
                    int zIndex = startIndex;
                    for (int l2 = 0; l2 < JacobianMatrices.this.stateDim; l2++) {
                        s2 += dFdYi[l2] * z2[zIndex];
                        zIndex += JacobianMatrices.this.stateDim;
                    }
                    zDot[startIndex + (i2 * JacobianMatrices.this.stateDim)] = s2;
                }
            }
            if (JacobianMatrices.this.paramDim != 0) {
                double[] dFdP = new double[JacobianMatrices.this.stateDim];
                int startIndex2 = JacobianMatrices.this.stateDim * JacobianMatrices.this.stateDim;
                ParameterConfiguration[] arr$ = JacobianMatrices.this.selectedParameters;
                for (ParameterConfiguration param : arr$) {
                    boolean found = false;
                    for (int k2 = 0; !found && k2 < JacobianMatrices.this.jacobianProviders.size(); k2++) {
                        ParameterJacobianProvider provider = (ParameterJacobianProvider) JacobianMatrices.this.jacobianProviders.get(k2);
                        if (provider.isSupported(param.getParameterName())) {
                            provider.computeParameterJacobian(t2, y2, yDot, param.getParameterName(), dFdP);
                            for (int i3 = 0; i3 < JacobianMatrices.this.stateDim; i3++) {
                                double[] dFdYi2 = dFdY[i3];
                                int zIndex2 = startIndex2;
                                double s3 = dFdP[i3];
                                for (int l3 = 0; l3 < JacobianMatrices.this.stateDim; l3++) {
                                    s3 += dFdYi2[l3] * z2[zIndex2];
                                    zIndex2++;
                                }
                                zDot[startIndex2 + i3] = s3;
                            }
                            found = true;
                        }
                    }
                    if (!found) {
                        Arrays.fill(zDot, startIndex2, startIndex2 + JacobianMatrices.this.stateDim, 0.0d);
                    }
                    startIndex2 += JacobianMatrices.this.stateDim;
                }
            }
        }
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/JacobianMatrices$MainStateJacobianWrapper.class */
    private static class MainStateJacobianWrapper implements MainStateJacobianProvider {
        private final FirstOrderDifferentialEquations ode;
        private final double[] hY;

        MainStateJacobianWrapper(FirstOrderDifferentialEquations ode, double[] hY) throws DimensionMismatchException {
            this.ode = ode;
            this.hY = (double[]) hY.clone();
            if (hY.length != ode.getDimension()) {
                throw new DimensionMismatchException(ode.getDimension(), hY.length);
            }
        }

        @Override // org.apache.commons.math3.ode.FirstOrderDifferentialEquations
        public int getDimension() {
            return this.ode.getDimension();
        }

        @Override // org.apache.commons.math3.ode.FirstOrderDifferentialEquations
        public void computeDerivatives(double t2, double[] y2, double[] yDot) throws MaxCountExceededException, DimensionMismatchException {
            this.ode.computeDerivatives(t2, y2, yDot);
        }

        @Override // org.apache.commons.math3.ode.MainStateJacobianProvider
        public void computeMainStateJacobian(double t2, double[] y2, double[] yDot, double[][] dFdY) throws MaxCountExceededException, DimensionMismatchException {
            int n2 = this.ode.getDimension();
            double[] tmpDot = new double[n2];
            for (int j2 = 0; j2 < n2; j2++) {
                double savedYj = y2[j2];
                int i2 = j2;
                y2[i2] = y2[i2] + this.hY[j2];
                this.ode.computeDerivatives(t2, y2, tmpDot);
                for (int i3 = 0; i3 < n2; i3++) {
                    dFdY[i3][j2] = (tmpDot[i3] - yDot[i3]) / this.hY[j2];
                }
                y2[j2] = savedYj;
            }
        }
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/JacobianMatrices$MismatchedEquations.class */
    public static class MismatchedEquations extends MathIllegalArgumentException {
        private static final long serialVersionUID = 20120902;

        public MismatchedEquations() {
            super(LocalizedFormats.UNMATCHED_ODE_IN_EXPANDED_SET, new Object[0]);
        }
    }
}
