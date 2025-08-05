package com.efiAnalytics.ui;

import java.awt.Container;
import javax.swing.JDialog;

/* renamed from: com.efiAnalytics.ui.dw, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/dw.class */
class C1638dw implements fR {

    /* renamed from: a, reason: collision with root package name */
    JDialog f11453a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1634ds f11454b;

    public C1638dw(C1634ds c1634ds, JDialog jDialog) {
        this.f11454b = c1634ds;
        this.f11453a = null;
        this.f11453a = jDialog;
    }

    @Override // com.efiAnalytics.ui.fR
    public boolean b() {
        this.f11453a.dispose();
        this.f11454b.f11449f = true;
        return true;
    }

    @Override // com.efiAnalytics.ui.fR
    public void c() {
        this.f11453a.dispose();
        this.f11454b.f11449f = true;
    }

    @Override // com.efiAnalytics.ui.fR
    public boolean a(Container container) {
        return true;
    }

    @Override // com.efiAnalytics.ui.fR
    public boolean b(Container container) {
        return true;
    }
}
