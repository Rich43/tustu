package com.efiAnalytics.tunerStudio.search;

import G.aG;
import G.bS;
import javax.swing.SwingUtilities;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/search/w.class */
class w implements aG {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ q f10229a;

    w(q qVar) {
        this.f10229a = qVar;
    }

    @Override // G.aG
    public boolean a(String str, bS bSVar) {
        SwingUtilities.invokeLater(new x(this));
        return true;
    }

    @Override // G.aG
    public void a(String str) {
        SwingUtilities.invokeLater(new y(this));
    }
}
