package org.apache.commons.math3.ode.events;

import java.util.Arrays;
import org.apache.commons.math3.ode.events.EventHandler;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/events/EventFilter.class */
public class EventFilter implements EventHandler {
    private static final int HISTORY_SIZE = 100;
    private final EventHandler rawHandler;
    private final FilterType filter;
    private final Transformer[] transformers = new Transformer[100];
    private final double[] updates = new double[100];
    private boolean forward;
    private double extremeT;

    public EventFilter(EventHandler rawHandler, FilterType filter) {
        this.rawHandler = rawHandler;
        this.filter = filter;
    }

    @Override // org.apache.commons.math3.ode.events.EventHandler
    public void init(double t0, double[] y0, double t2) {
        this.rawHandler.init(t0, y0, t2);
        this.forward = t2 >= t0;
        this.extremeT = this.forward ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
        Arrays.fill(this.transformers, Transformer.UNINITIALIZED);
        Arrays.fill(this.updates, this.extremeT);
    }

    @Override // org.apache.commons.math3.ode.events.EventHandler
    public double g(double t2, double[] y2) {
        double rawG = this.rawHandler.g(t2, y2);
        if (!this.forward) {
            if (t2 < this.extremeT) {
                Transformer previous = this.transformers[0];
                Transformer next = this.filter.selectTransformer(previous, rawG, this.forward);
                if (next != previous) {
                    System.arraycopy(this.updates, 0, this.updates, 1, this.updates.length - 1);
                    System.arraycopy(this.transformers, 0, this.transformers, 1, this.transformers.length - 1);
                    this.updates[0] = this.extremeT;
                    this.transformers[0] = next;
                }
                this.extremeT = t2;
                return next.transformed(rawG);
            }
            for (int i2 = 0; i2 < this.updates.length - 1; i2++) {
                if (t2 <= this.updates[i2]) {
                    return this.transformers[i2].transformed(rawG);
                }
            }
            return this.transformers[this.updates.length - 1].transformed(rawG);
        }
        int last = this.transformers.length - 1;
        if (this.extremeT < t2) {
            Transformer previous2 = this.transformers[last];
            Transformer next2 = this.filter.selectTransformer(previous2, rawG, this.forward);
            if (next2 != previous2) {
                System.arraycopy(this.updates, 1, this.updates, 0, last);
                System.arraycopy(this.transformers, 1, this.transformers, 0, last);
                this.updates[last] = this.extremeT;
                this.transformers[last] = next2;
            }
            this.extremeT = t2;
            return next2.transformed(rawG);
        }
        for (int i3 = last; i3 > 0; i3--) {
            if (this.updates[i3] <= t2) {
                return this.transformers[i3].transformed(rawG);
            }
        }
        return this.transformers[0].transformed(rawG);
    }

    @Override // org.apache.commons.math3.ode.events.EventHandler
    public EventHandler.Action eventOccurred(double t2, double[] y2, boolean increasing) {
        return this.rawHandler.eventOccurred(t2, y2, this.filter.getTriggeredIncreasing());
    }

    @Override // org.apache.commons.math3.ode.events.EventHandler
    public void resetState(double t2, double[] y2) {
        this.rawHandler.resetState(t2, y2);
    }
}
