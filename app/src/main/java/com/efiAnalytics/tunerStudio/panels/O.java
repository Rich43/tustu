package com.efiAnalytics.tunerStudio.panels;

import G.C0132o;
import G.InterfaceC0131n;
import G.aB;
import com.efiAnalytics.ui.bV;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/O.class */
class O implements InterfaceC0131n {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ J f9963a;

    O(J j2) {
        this.f9963a = j2;
    }

    @Override // G.InterfaceC0131n
    public void a(double d2) {
        this.f9963a.f9953i.b(d2);
    }

    @Override // G.InterfaceC0131n
    public void a(C0132o c0132o) throws IllegalArgumentException {
        aB.a().e();
        if (c0132o.a() != 3) {
            this.f9963a.f9953i.b(1.0d);
            this.f9963a.c(C1818g.b("Write Complete!"));
            this.f9963a.g();
        } else {
            if (c0132o.c() == null) {
                this.f9963a.c(C1818g.b("Calibration Table Write Failed!") + "\nUnable to complete write.");
                return;
            }
            String strC = c0132o.c();
            if (strC.length() > 150) {
                strC = strC.substring(0, 150);
            }
            bV.d(C1818g.b("Calibration Table Write Failed!") + "\n" + strC, this.f9963a.f9949e);
            this.f9963a.c(C1818g.b("Calibration Table Write Failed!") + "\n" + strC);
            bH.C.b("Calibration Table Write Failed!\n" + c0132o.c());
        }
    }

    @Override // G.InterfaceC0131n
    public void e() {
        aB.a().d();
    }
}
