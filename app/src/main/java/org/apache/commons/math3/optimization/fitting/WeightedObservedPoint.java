package org.apache.commons.math3.optimization.fitting;

import java.io.Serializable;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/fitting/WeightedObservedPoint.class */
public class WeightedObservedPoint implements Serializable {
    private static final long serialVersionUID = 5306874947404636157L;
    private final double weight;

    /* renamed from: x, reason: collision with root package name */
    private final double f13080x;

    /* renamed from: y, reason: collision with root package name */
    private final double f13081y;

    public WeightedObservedPoint(double weight, double x2, double y2) {
        this.weight = weight;
        this.f13080x = x2;
        this.f13081y = y2;
    }

    public double getWeight() {
        return this.weight;
    }

    public double getX() {
        return this.f13080x;
    }

    public double getY() {
        return this.f13081y;
    }
}
