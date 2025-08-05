package W;

import java.io.File;

/* loaded from: TunerStudioMS.jar:W/aE.class */
class aE extends Thread {

    /* renamed from: a, reason: collision with root package name */
    File f2038a;

    /* renamed from: b, reason: collision with root package name */
    int f2039b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ aB f2040c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    aE(aB aBVar, File file, int i2) {
        super("LoadTriggerLog");
        this.f2040c = aBVar;
        setDaemon(true);
        this.f2038a = file;
        this.f2039b = i2;
        setPriority(1);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            this.f2040c.a(this.f2038a, this.f2039b);
        } catch (V.a e2) {
            this.f2040c.a(e2);
        }
    }
}
