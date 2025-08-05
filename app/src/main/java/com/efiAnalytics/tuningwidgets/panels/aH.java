package com.efiAnalytics.tuningwidgets.panels;

import bt.bX;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/aH.class */
class aH implements bX {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aF f10341a;

    aH(aF aFVar) {
        this.f10341a = aFVar;
    }

    @Override // bt.bX
    public void b(String str) throws NumberFormatException {
        if (!this.f10341a.a(str)) {
            this.f10341a.n();
        } else if (!this.f10341a.f10337e.getText().trim().equals("")) {
            this.f10341a.b();
        }
        if (this.f10341a.f10334b != null) {
            this.f10341a.f10334b.h().g();
        }
    }
}
