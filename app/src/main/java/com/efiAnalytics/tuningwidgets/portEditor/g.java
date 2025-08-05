package com.efiAnalytics.tuningwidgets.portEditor;

import G.C0083bp;
import G.R;
import bt.aT;
import com.efiAnalytics.ui.bV;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/portEditor/g.class */
class g extends aT {

    /* renamed from: u, reason: collision with root package name */
    C0083bp f10558u;

    /* renamed from: v, reason: collision with root package name */
    final /* synthetic */ OutputPortEditor f10559v;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public g(OutputPortEditor outputPortEditor, R r2, C0083bp c0083bp) {
        super(r2, c0083bp);
        this.f10559v = outputPortEditor;
        this.f10558u = c0083bp;
    }

    @Override // bt.aT, bt.bX
    public void b(String str) throws IllegalArgumentException {
        super.b(str);
    }

    @Override // bt.aT, javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) throws IllegalArgumentException {
        super.setEnabled(z2);
        bV.a(this, z2);
    }

    public void c(int i2) {
        h();
        this.f10558u.a(i2);
        f();
    }
}
