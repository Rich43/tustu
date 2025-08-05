package org.apache.commons.math3.ode;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/FieldExpandableODE.class */
public class FieldExpandableODE<T extends RealFieldElement<T>> {
    private final FirstOrderFieldDifferentialEquations<T> primary;
    private List<FieldSecondaryEquations<T>> components = new ArrayList();
    private FieldEquationsMapper<T> mapper;

    public FieldExpandableODE(FirstOrderFieldDifferentialEquations<T> primary) {
        this.primary = primary;
        this.mapper = new FieldEquationsMapper<>(null, primary.getDimension());
    }

    public FieldEquationsMapper<T> getMapper() {
        return this.mapper;
    }

    public int addSecondaryEquations(FieldSecondaryEquations<T> secondary) {
        this.components.add(secondary);
        this.mapper = new FieldEquationsMapper<>(this.mapper, secondary.getDimension());
        return this.components.size();
    }

    public void init(T t0, T[] y0, T finalTime) throws MathIllegalArgumentException {
        int index = 0;
        RealFieldElement[] realFieldElementArrExtractEquationData = this.mapper.extractEquationData(0, y0);
        this.primary.init(t0, realFieldElementArrExtractEquationData, finalTime);
        while (true) {
            index++;
            if (index < this.mapper.getNumberOfEquations()) {
                this.components.get(index - 1).init(t0, realFieldElementArrExtractEquationData, this.mapper.extractEquationData(index, y0), finalTime);
            } else {
                return;
            }
        }
    }

    public T[] computeDerivatives(T t2, T[] tArr) throws MaxCountExceededException, MathIllegalArgumentException {
        T[] tArr2 = (T[]) ((RealFieldElement[]) MathArrays.buildArray(t2.getField2(), this.mapper.getTotalDimension()));
        int i2 = 0;
        RealFieldElement[] realFieldElementArrExtractEquationData = this.mapper.extractEquationData(0, tArr);
        RealFieldElement[] realFieldElementArrComputeDerivatives = this.primary.computeDerivatives(t2, realFieldElementArrExtractEquationData);
        this.mapper.insertEquationData(0, realFieldElementArrComputeDerivatives, tArr2);
        while (true) {
            i2++;
            if (i2 < this.mapper.getNumberOfEquations()) {
                this.mapper.insertEquationData(i2, this.components.get(i2 - 1).computeDerivatives(t2, realFieldElementArrExtractEquationData, realFieldElementArrComputeDerivatives, this.mapper.extractEquationData(i2, tArr)), tArr2);
            } else {
                return tArr2;
            }
        }
    }
}
