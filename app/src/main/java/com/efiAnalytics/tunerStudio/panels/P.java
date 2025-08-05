package com.efiAnalytics.tunerStudio.panels;

import G.C0132o;
import G.InterfaceC0131n;
import G.aB;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/P.class */
class P implements InterfaceC0131n {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ J f9964a;

    P(J j2) {
        this.f9964a = j2;
    }

    @Override // G.InterfaceC0131n
    public void a(double d2) {
        this.f9964a.f9953i.b(d2);
    }

    @Override // G.InterfaceC0131n
    public void a(C0132o c0132o) throws IllegalArgumentException {
        aB.a().e();
        if (c0132o.a() != 3) {
            this.f9964a.f9953i.b(1.0d);
            this.f9964a.c(C1818g.b("Write Complete!"));
            this.f9964a.g();
        } else if (c0132o.c() != null) {
            this.f9964a.c(C1818g.b("Calibration Table Write Failed!") + "\n" + c0132o.c());
        } else {
            this.f9964a.c(C1818g.b("Calibration Table Write Failed!") + "\nUnable to complete write.");
        }
    }

    @Override // G.InterfaceC0131n
    public void e() {
        aB.a().d();
    }
}
