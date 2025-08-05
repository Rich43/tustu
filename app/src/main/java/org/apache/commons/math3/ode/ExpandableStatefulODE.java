package org.apache.commons.math3.ode;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/ExpandableStatefulODE.class */
public class ExpandableStatefulODE {
    private final FirstOrderDifferentialEquations primary;
    private final EquationsMapper primaryMapper;
    private double time;
    private final double[] primaryState;
    private final double[] primaryStateDot;
    private List<SecondaryComponent> components;

    public ExpandableStatefulODE(FirstOrderDifferentialEquations primary) {
        int n2 = primary.getDimension();
        this.primary = primary;
        this.primaryMapper = new EquationsMapper(0, n2);
        this.time = Double.NaN;
        this.primaryState = new double[n2];
        this.primaryStateDot = new double[n2];
        this.components = new ArrayList();
    }

    public FirstOrderDifferentialEquations getPrimary() {
        return this.primary;
    }

    public int getTotalDimension() {
        if (this.components.isEmpty()) {
            return this.primaryMapper.getDimension();
        }
        EquationsMapper lastMapper = this.components.get(this.components.size() - 1).mapper;
        return lastMapper.getFirstIndex() + lastMapper.getDimension();
    }

    public void computeDerivatives(double t2, double[] y2, double[] yDot) throws MaxCountExceededException, DimensionMismatchException {
        this.primaryMapper.extractEquationData(y2, this.primaryState);
        this.primary.computeDerivatives(t2, this.primaryState, this.primaryStateDot);
        for (SecondaryComponent component : this.components) {
            component.mapper.extractEquationData(y2, component.state);
            component.equation.computeDerivatives(t2, this.primaryState, this.primaryStateDot, component.state, component.stateDot);
            component.mapper.insertEquationData(component.stateDot, yDot);
        }
        this.primaryMapper.insertEquationData(this.primaryStateDot, yDot);
    }

    public int addSecondaryEquations(SecondaryEquations secondary) {
        int firstIndex;
        if (this.components.isEmpty()) {
            this.components = new ArrayList();
            firstIndex = this.primary.getDimension();
        } else {
            SecondaryComponent last = this.components.get(this.components.size() - 1);
            firstIndex = last.mapper.getFirstIndex() + last.mapper.getDimension();
        }
        this.components.add(new SecondaryComponent(secondary, firstIndex));
        return this.components.size() - 1;
    }

    public EquationsMapper getPrimaryMapper() {
        return this.primaryMapper;
    }

    public EquationsMapper[] getSecondaryMappers() {
        EquationsMapper[] mappers = new EquationsMapper[this.components.size()];
        for (int i2 = 0; i2 < mappers.length; i2++) {
            mappers[i2] = this.components.get(i2).mapper;
        }
        return mappers;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getTime() {
        return this.time;
    }

    public void setPrimaryState(double[] primaryState) throws DimensionMismatchException {
        if (primaryState.length != this.primaryState.length) {
            throw new DimensionMismatchException(primaryState.length, this.primaryState.length);
        }
        System.arraycopy(primaryState, 0, this.primaryState, 0, primaryState.length);
    }

    public double[] getPrimaryState() {
        return (double[]) this.primaryState.clone();
    }

    public double[] getPrimaryStateDot() {
        return (double[]) this.primaryStateDot.clone();
    }

    public void setSecondaryState(int index, double[] secondaryState) throws DimensionMismatchException {
        double[] localArray = this.components.get(index).state;
        if (secondaryState.length != localArray.length) {
            throw new DimensionMismatchException(secondaryState.length, localArray.length);
        }
        System.arraycopy(secondaryState, 0, localArray, 0, secondaryState.length);
    }

    public double[] getSecondaryState(int index) {
        return (double[]) this.components.get(index).state.clone();
    }

    public double[] getSecondaryStateDot(int index) {
        return (double[]) this.components.get(index).stateDot.clone();
    }

    public void setCompleteState(double[] completeState) throws DimensionMismatchException {
        if (completeState.length != getTotalDimension()) {
            throw new DimensionMismatchException(completeState.length, getTotalDimension());
        }
        this.primaryMapper.extractEquationData(completeState, this.primaryState);
        for (SecondaryComponent component : this.components) {
            component.mapper.extractEquationData(completeState, component.state);
        }
    }

    public double[] getCompleteState() throws DimensionMismatchException {
        double[] completeState = new double[getTotalDimension()];
        this.primaryMapper.insertEquationData(this.primaryState, completeState);
        for (SecondaryComponent component : this.components) {
            component.mapper.insertEquationData(component.state, completeState);
        }
        return completeState;
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/ExpandableStatefulODE$SecondaryComponent.class */
    private static class SecondaryComponent {
        private final SecondaryEquations equation;
        private final EquationsMapper mapper;
        private final double[] state;
        private final double[] stateDot;

        SecondaryComponent(SecondaryEquations equation, int firstIndex) {
            int n2 = equation.getDimension();
            this.equation = equation;
            this.mapper = new EquationsMapper(firstIndex, n2);
            this.state = new double[n2];
            this.stateDot = new double[n2];
        }
    }
}
