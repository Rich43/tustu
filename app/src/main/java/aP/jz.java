package aP;

import java.awt.Frame;
import java.awt.Window;

/* loaded from: TunerStudioMS.jar:aP/jz.class */
class jz implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ Frame f3819a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ jx f3820b;

    jz(jx jxVar, Frame frame) {
        this.f3820b = jxVar;
        this.f3819a = frame;
    }

    @Override // java.lang.Runnable
    public void run() {
        C0338f.a().b((Window) this.f3819a);
    }
}
