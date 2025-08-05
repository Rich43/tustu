package com.efiAnalytics.apps.ts.dashboard.renderers;

import com.efiAnalytics.apps.ts.dashboard.Gauge;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/renderers/GaugePainter.class */
public interface GaugePainter extends d {
    void initialize(Gauge gauge);

    boolean isShapeLockedToAspect();
}
