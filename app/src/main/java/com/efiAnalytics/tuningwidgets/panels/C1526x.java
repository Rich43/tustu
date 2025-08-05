package com.efiAnalytics.tuningwidgets.panels;

import java.util.Comparator;

/* renamed from: com.efiAnalytics.tuningwidgets.panels.x, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/x.class */
class C1526x implements Comparator {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1516n f10505a;

    C1526x(C1516n c1516n) {
        this.f10505a = c1516n;
    }

    @Override // java.util.Comparator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compare(C1528z c1528z, C1528z c1528z2) {
        return c1528z.toString().compareToIgnoreCase(c1528z2.toString());
    }
}
