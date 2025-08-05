package com.efiAnalytics.apps.ts.dashboard.renderers;

import com.efiAnalytics.apps.ts.dashboard.AbstractC1420s;
import java.awt.Graphics;
import java.awt.geom.Area;
import java.io.Serializable;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/renderers/d.class */
public interface d extends Serializable {
    String getName();

    void paintGauge(Graphics graphics, AbstractC1420s abstractC1420s);

    void updateGauge(Graphics graphics, AbstractC1420s abstractC1420s);

    void paintBackground(Graphics graphics, AbstractC1420s abstractC1420s);

    boolean requiresBackgroundRepaint(AbstractC1420s abstractC1420s);

    void invalidate();

    Area areaPainted(AbstractC1420s abstractC1420s);
}
