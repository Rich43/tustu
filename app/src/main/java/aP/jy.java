package aP;

import java.awt.Frame;
import java.awt.Window;

/* loaded from: TunerStudioMS.jar:aP/jy.class */
class jy implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ Frame f3817a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ jx f3818b;

    jy(jx jxVar, Frame frame) {
        this.f3818b = jxVar;
        this.f3817a = frame;
    }

    @Override // java.lang.Runnable
    public void run() {
        C0338f.a().c((Window) this.f3817a);
    }
}
