package com.efiAnalytics.tunerStudio.panels;

import G.C0096cb;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/E.class */
class E {

    /* renamed from: b, reason: collision with root package name */
    private C0096cb f9937b;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1466o f9938a;

    E(C1466o c1466o, C0096cb c0096cb) {
        this.f9938a = c1466o;
        this.f9937b = null;
        this.f9937b = c0096cb;
    }

    public String toString() {
        return a() == null ? "[none]" : a().g() == null ? C1818g.b("Unknown") : C1818g.b(a().g());
    }

    public C0096cb a() {
        return this.f9937b;
    }
}
