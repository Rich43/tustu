package com.efiAnalytics.tunerStudio.panels;

import java.awt.Cursor;
import javax.swing.SwingUtilities;

/* renamed from: com.efiAnalytics.tunerStudio.panels.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/d.class */
class C1455d implements aP.V {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1452a f10106a;

    C1455d(C1452a c1452a) {
        this.f10106a = c1452a;
    }

    @Override // aP.V
    public void a(aP.U u2) {
        if (u2.b() != null) {
            this.f10106a.f10052g.setCursor(Cursor.getPredefinedCursor(3));
            SwingUtilities.invokeLater(new RunnableC1456e(this, u2));
        }
    }
}
