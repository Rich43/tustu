package com.sun.prism.paint;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.paint.Paint;
import java.util.List;

/* loaded from: jfxrt.jar:com/sun/prism/paint/Gradient.class */
public abstract class Gradient extends Paint {
    public static final int PAD = 0;
    public static final int REFLECT = 1;
    public static final int REPEAT = 2;
    private final int numStops;
    private final List<Stop> stops;
    private final BaseTransform gradientTransform;
    private final int spreadMethod;
    private long cacheOffset;

    protected Gradient(Paint.Type type, BaseTransform gradientTransform, boolean proportional, int spreadMethod, List<Stop> stops) {
        super(type, proportional, false);
        this.cacheOffset = -1L;
        if (gradientTransform != null) {
            this.gradientTransform = gradientTransform.copy();
        } else {
            this.gradientTransform = BaseTransform.IDENTITY_TRANSFORM;
        }
        this.spreadMethod = spreadMethod;
        this.numStops = stops.size();
        this.stops = stops;
    }

    public int getSpreadMethod() {
        return this.spreadMethod;
    }

    public BaseTransform getGradientTransformNoClone() {
        return this.gradientTransform;
    }

    public int getNumStops() {
        return this.numStops;
    }

    public List<Stop> getStops() {
        return this.stops;
    }

    public void setGradientOffset(long offset) {
        this.cacheOffset = offset;
    }

    public long getGradientOffset() {
        return this.cacheOffset;
    }

    @Override // com.sun.prism.paint.Paint
    public boolean isOpaque() {
        for (int i2 = 0; i2 < this.numStops; i2++) {
            if (!this.stops.get(i2).getColor().isOpaque()) {
                return false;
            }
        }
        return true;
    }
}
