package com.efiAnalytics.tuningwidgets.panels;

import G.C0069bb;
import java.util.ArrayList;
import java.util.Iterator;

/* renamed from: com.efiAnalytics.tuningwidgets.panels.am, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/am.class */
class C1496am {

    /* renamed from: a, reason: collision with root package name */
    C0069bb f10405a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1488ae f10406b;

    public C1496am(C1488ae c1488ae, C0069bb c0069bb) {
        this.f10406b = c1488ae;
        this.f10405a = null;
        this.f10405a = c0069bb;
    }

    public boolean a(ArrayList arrayList) {
        int i2 = 0;
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            if (this.f10405a.equals((C0069bb) it.next())) {
                i2++;
                if (i2 == 1) {
                    return true;
                }
            }
        }
        return false;
    }

    public C0069bb a() {
        return this.f10405a;
    }

    public String toString() {
        return this.f10405a != null ? this.f10405a.aJ() : "Error";
    }
}
