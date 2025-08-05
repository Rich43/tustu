package aP;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/* loaded from: TunerStudioMS.jar:aP/iH.class */
class iH extends WindowAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bk.d f3646a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ iG f3647b;

    iH(iG iGVar, bk.d dVar) {
        this.f3647b = iGVar;
        this.f3646a = dVar;
    }

    @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
    public void windowClosing(WindowEvent windowEvent) {
        this.f3646a.close();
    }
}
