package aP;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:aP/iD.class */
class iD extends Thread {

    /* renamed from: a, reason: collision with root package name */
    aE.a f3638a;

    /* renamed from: b, reason: collision with root package name */
    File f3639b;

    /* renamed from: c, reason: collision with root package name */
    String f3640c;

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ iC f3641d;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public iD(iC iCVar, String str, aE.a aVar, File file) {
        super("UpdateConfigFile");
        this.f3641d = iCVar;
        this.f3638a = null;
        this.f3639b = null;
        this.f3640c = null;
        setDaemon(true);
        this.f3638a = aVar;
        this.f3639b = file;
        this.f3640c = str;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e2) {
            Logger.getLogger(iC.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        this.f3641d.a(this.f3640c, this.f3638a, this.f3639b);
    }
}
