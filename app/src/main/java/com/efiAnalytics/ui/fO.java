package com.efiAnalytics.ui;

import java.awt.Container;
import javax.swing.JDialog;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/fO.class */
class fO implements fR {

    /* renamed from: a, reason: collision with root package name */
    JDialog f11655a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ fK f11656b;

    public fO(fK fKVar, JDialog jDialog) {
        this.f11656b = fKVar;
        this.f11655a = null;
        this.f11655a = jDialog;
    }

    @Override // com.efiAnalytics.ui.fR
    public boolean b() {
        this.f11655a.dispose();
        this.f11656b.f11649g = true;
        return true;
    }

    @Override // com.efiAnalytics.ui.fR
    public void c() {
        this.f11655a.dispose();
        this.f11656b.f11649g = true;
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
