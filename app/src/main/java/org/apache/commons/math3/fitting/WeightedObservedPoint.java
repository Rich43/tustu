package org.apache.commons.math3.fitting;

import java.io.Serializable;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/fitting/WeightedObservedPoint.class */
public class WeightedObservedPoint implements Serializable {
    private static final long serialVersionUID = 5306874947404636157L;
    private final double weight;

    /* renamed from: x, reason: collision with root package name */
    private final double f12998x;

    /* renamed from: y, reason: collision with root package name */
    private final double f12999y;

    public WeightedObservedPoint(double weight, double x2, double y2) {
        this.weight = weight;
        this.f12998x = x2;
        this.f12999y = y2;
    }

    public double getWeight() {
        return this.weight;
    }

    public double getX() {
        return this.f12998x;
    }

    public double getY() {
        return this.f12999y;
    }
}
