package bt;

import G.C0113cs;
import G.InterfaceC0109co;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: bt.ak, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/ak.class */
class C1302ak extends Thread implements InterfaceC0109co {

    /* renamed from: a, reason: collision with root package name */
    boolean f8842a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1300ai f8843b;

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.f8842a) {
            try {
                Thread.sleep(250L);
                this.f8843b.a();
            } catch (Exception e2) {
                Logger.getLogger(C1300ai.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a() {
        this.f8842a = false;
        C0113cs.a().a(this);
    }

    @Override // G.InterfaceC0109co
    public void setCurrentOutputChannelValue(String str, double d2) {
    }
}
