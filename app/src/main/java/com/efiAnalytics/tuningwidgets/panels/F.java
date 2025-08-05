package com.efiAnalytics.tuningwidgets.panels;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/F.class */
class F implements bH.Q {

    /* renamed from: b, reason: collision with root package name */
    private ac.q f10250b;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ A f10251a;

    public F(A a2, ac.q qVar) {
        this.f10251a = a2;
        this.f10250b = null;
        this.f10250b = qVar;
    }

    public String toString() {
        return this.f10250b.a();
    }

    public ac.q a() {
        return this.f10250b;
    }

    @Override // bH.Q
    public String c() {
        return this.f10250b != null ? this.f10250b.a() : "";
    }

    public boolean equals(Object obj) {
        return (!(obj instanceof String) || this.f10250b == null) ? super.equals(obj) : obj.equals(this.f10250b.a());
    }
}
