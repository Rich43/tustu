package aP;

import bj.C1174a;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/* loaded from: TunerStudioMS.jar:aP/iI.class */
class iI extends WindowAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1174a f3648a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ iG f3649b;

    iI(iG iGVar, C1174a c1174a) {
        this.f3649b = iGVar;
        this.f3648a = c1174a;
    }

    @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
    public void windowClosing(WindowEvent windowEvent) {
        this.f3648a.close();
    }
}
