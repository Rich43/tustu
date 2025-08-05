package aP;

import java.io.File;

/* renamed from: aP.y, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/y.class */
class RunnableC0469y implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ File f3842a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0338f f3843b;

    RunnableC0469y(C0338f c0338f, File file) {
        this.f3843b = c0338f;
        this.f3842a = file;
    }

    @Override // java.lang.Runnable
    public void run() {
        cZ.a().i().a().g(gW.f3467r);
        new E(this.f3843b, new File[]{this.f3842a}).start();
    }
}
