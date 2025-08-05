package org.apache.commons.math3.fitting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/fitting/WeightedObservedPoints.class */
public class WeightedObservedPoints implements Serializable {
    private static final long serialVersionUID = 20130813;
    private final List<WeightedObservedPoint> observations = new ArrayList();

    public void add(double x2, double y2) {
        add(1.0d, x2, y2);
    }

    public void add(double weight, double x2, double y2) {
        this.observations.add(new WeightedObservedPoint(weight, x2, y2));
    }

    public void add(WeightedObservedPoint observed) {
        this.observations.add(observed);
    }

    public List<WeightedObservedPoint> toList() {
        return new ArrayList(this.observations);
    }

    public void clear() {
        this.observations.clear();
    }
}
