package ap;

import i.C1746f;
import i.C1748h;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.internal.dynalink.CallSiteDescriptor;

/* renamed from: ap.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ap/d.class */
class C0832d extends Thread {

    /* renamed from: b, reason: collision with root package name */
    private int f6196b;

    /* renamed from: c, reason: collision with root package name */
    private String f6197c;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0831c f6198a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    C0832d(C0831c c0831c) {
        super("PIPE_ACTION_INDEX_CHANGED PubThread");
        this.f6198a = c0831c;
        this.f6196b = -1;
        this.f6197c = null;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (true) {
            try {
                Thread.sleep(C1746f.f12349a * 2);
            } catch (InterruptedException e2) {
                Logger.getLogger(C0831c.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            if (this.f6196b >= 0 && this.f6197c != null) {
                C1748h.a().a("indexChanged", this.f6197c + CallSiteDescriptor.OPERATOR_DELIMITER + this.f6196b);
                this.f6196b = -1;
                this.f6197c = null;
            }
        }
    }

    public void a(int i2) {
        this.f6196b = i2;
    }

    public void a(String str) {
        this.f6197c = str;
    }
}
