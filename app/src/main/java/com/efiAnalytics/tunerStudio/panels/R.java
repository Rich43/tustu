package com.efiAnalytics.tunerStudio.panels;

import G.C0068ba;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/R.class */
class R {

    /* renamed from: b, reason: collision with root package name */
    private C0068ba f9967b;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ J f9968a;

    public R(J j2, C0068ba c0068ba) {
        this.f9968a = j2;
        this.f9967b = c0068ba;
    }

    public String toString() {
        return C1818g.b(this.f9967b.toString());
    }

    public C0068ba a() {
        return this.f9967b;
    }

    public boolean equals(Object obj) {
        return obj instanceof R ? this.f9967b.toString().equals(((R) obj).a().toString()) : super.equals(obj);
    }
}
