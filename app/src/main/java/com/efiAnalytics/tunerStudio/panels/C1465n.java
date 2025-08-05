package com.efiAnalytics.tunerStudio.panels;

/* renamed from: com.efiAnalytics.tunerStudio.panels.n, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/n.class */
class C1465n {

    /* renamed from: b, reason: collision with root package name */
    private String f10125b;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1458g f10126a;

    C1465n(C1458g c1458g, String str) {
        this.f10126a = c1458g;
        this.f10125b = str;
    }

    public String toString() {
        return this.f10125b.equals("") ? C1458g.f10118i : this.f10125b;
    }

    public String a() {
        return this.f10125b;
    }

    public boolean equals(Object obj) {
        return ((obj instanceof String) || (obj instanceof C1465n)) ? obj.equals(this.f10125b) : super.equals(obj);
    }
}
