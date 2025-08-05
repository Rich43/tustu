package com.efiAnalytics.tuningwidgets.panels;

import G.C0043ac;
import G.C0052al;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* renamed from: com.efiAnalytics.tuningwidgets.panels.z, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/z.class */
class C1528z {

    /* renamed from: a, reason: collision with root package name */
    C0043ac f10507a;

    /* renamed from: b, reason: collision with root package name */
    C0052al f10508b;

    /* renamed from: c, reason: collision with root package name */
    ArrayList f10509c;

    /* renamed from: e, reason: collision with root package name */
    private boolean f10510e;

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ C1516n f10511d;

    public C1528z(C1516n c1516n, C0043ac c0043ac, ArrayList arrayList) {
        this.f10511d = c1516n;
        this.f10507a = null;
        this.f10508b = null;
        this.f10509c = null;
        this.f10510e = false;
        this.f10507a = c0043ac;
        this.f10509c = arrayList;
    }

    public C1528z(C1516n c1516n, C0052al c0052al, ArrayList arrayList) {
        this.f10511d = c1516n;
        this.f10507a = null;
        this.f10508b = null;
        this.f10509c = null;
        this.f10510e = false;
        this.f10508b = c0052al;
        this.f10509c = arrayList;
    }

    public boolean a(List list) {
        int i2 = 0;
        for (int i3 = 0; i3 < list.size(); i3++) {
            if (this.f10509c.contains(list.get(i3))) {
                i2++;
                if (i2 == this.f10509c.size()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean b(List list) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            if (this.f10509c.contains((G.aH) it.next())) {
                return true;
            }
        }
        return false;
    }

    public ArrayList a() {
        return this.f10509c;
    }

    public String toString() {
        return this.f10507a != null ? this.f10507a.b() : this.f10508b.aJ();
    }

    public boolean b() {
        return this.f10510e;
    }

    public void a(boolean z2) {
        this.f10510e = z2;
    }
}
