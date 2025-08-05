package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.MathUnsupportedOperationException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/DefaultIterativeLinearSolverEvent.class */
public class DefaultIterativeLinearSolverEvent extends IterativeLinearSolverEvent {
    private static final long serialVersionUID = 20120129;

    /* renamed from: b, reason: collision with root package name */
    private final RealVector f13022b;

    /* renamed from: r, reason: collision with root package name */
    private final RealVector f13023r;
    private final double rnorm;

    /* renamed from: x, reason: collision with root package name */
    private final RealVector f13024x;

    public DefaultIterativeLinearSolverEvent(Object source, int iterations, RealVector x2, RealVector b2, RealVector r2, double rnorm) {
        super(source, iterations);
        this.f13024x = x2;
        this.f13022b = b2;
        this.f13023r = r2;
        this.rnorm = rnorm;
    }

    public DefaultIterativeLinearSolverEvent(Object source, int iterations, RealVector x2, RealVector b2, double rnorm) {
        super(source, iterations);
        this.f13024x = x2;
        this.f13022b = b2;
        this.f13023r = null;
        this.rnorm = rnorm;
    }

    @Override // org.apache.commons.math3.linear.IterativeLinearSolverEvent
    public double getNormOfResidual() {
        return this.rnorm;
    }

    @Override // org.apache.commons.math3.linear.IterativeLinearSolverEvent
    public RealVector getResidual() {
        if (this.f13023r != null) {
            return this.f13023r;
        }
        throw new MathUnsupportedOperationException();
    }

    @Override // org.apache.commons.math3.linear.IterativeLinearSolverEvent
    public RealVector getRightHandSideVector() {
        return this.f13022b;
    }

    @Override // org.apache.commons.math3.linear.IterativeLinearSolverEvent
    public RealVector getSolution() {
        return this.f13024x;
    }

    @Override // org.apache.commons.math3.linear.IterativeLinearSolverEvent
    public boolean providesResidual() {
        return this.f13023r != null;
    }
}
