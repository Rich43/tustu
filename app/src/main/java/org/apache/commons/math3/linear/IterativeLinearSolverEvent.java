package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.MathUnsupportedOperationException;
import org.apache.commons.math3.util.IterationEvent;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/IterativeLinearSolverEvent.class */
public abstract class IterativeLinearSolverEvent extends IterationEvent {
    private static final long serialVersionUID = 20120129;

    public abstract RealVector getRightHandSideVector();

    public abstract double getNormOfResidual();

    public abstract RealVector getSolution();

    public IterativeLinearSolverEvent(Object source, int iterations) {
        super(source, iterations);
    }

    public RealVector getResidual() {
        throw new MathUnsupportedOperationException();
    }

    public boolean providesResidual() {
        return false;
    }
}
