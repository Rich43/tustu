package org.apache.commons.math3.analysis.interpolation;

import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/interpolation/InterpolatingMicrosphere2D.class */
public class InterpolatingMicrosphere2D extends InterpolatingMicrosphere {
    private static final int DIMENSION = 2;

    public InterpolatingMicrosphere2D(int size, double maxDarkFraction, double darkThreshold, double background) {
        super(2, size, maxDarkFraction, darkThreshold, background);
        for (int i2 = 0; i2 < size; i2++) {
            double angle = (i2 * 6.283185307179586d) / size;
            add(new double[]{FastMath.cos(angle), FastMath.sin(angle)}, false);
        }
    }

    protected InterpolatingMicrosphere2D(InterpolatingMicrosphere2D other) {
        super(other);
    }

    @Override // org.apache.commons.math3.analysis.interpolation.InterpolatingMicrosphere
    public InterpolatingMicrosphere2D copy() {
        return new InterpolatingMicrosphere2D(this);
    }
}
