package com.efiAnalytics.apps.ts.dashboard.renderers;

import java.awt.BasicStroke;
import java.io.Serializable;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/renderers/g.class */
public class g extends BasicStroke implements Serializable {
    public g(float f2, int i2, int i3, float f3, float[] fArr, float f4) {
        super(f2, i2, i3, f3, fArr, f4);
    }

    public g(float f2, int i2, int i3) {
        super(f2, i2, i3);
    }

    public g(float f2) {
        this(f2, 2, 0, 10.0f, null, 0.0f);
    }

    public g() {
        this(1.0f, 2, 0, 10.0f, null, 0.0f);
    }
}
