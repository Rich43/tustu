package com.efiAnalytics.tunerStudio.panels;

import java.awt.Cursor;
import java.awt.HeadlessException;

/* renamed from: com.efiAnalytics.tunerStudio.panels.e, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/e.class */
class RunnableC1456e implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aP.U f10107a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1455d f10108b;

    RunnableC1456e(C1455d c1455d, aP.U u2) {
        this.f10108b = c1455d;
        this.f10107a = u2;
    }

    @Override // java.lang.Runnable
    public void run() throws HeadlessException {
        this.f10108b.f10106a.a(this.f10107a.b());
        this.f10108b.f10106a.f10052g.setCursor(Cursor.getDefaultCursor());
    }
}
