package bN;

import java.io.IOException;
import org.apache.commons.net.tftp.TFTP;

/* loaded from: TunerStudioMS.jar:bN/s.class */
class s extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f7324a = true;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ r f7325b;

    s(r rVar) {
        this.f7325b = rVar;
        if (rVar.f7320k) {
            setName("XCP Writer_Master" + Math.random());
        } else {
            setName("XCP Writer_Slave" + Math.random());
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.f7324a) {
            try {
                this.f7325b.g();
                this.f7325b.a(TFTP.DEFAULT_TIMEOUT);
            } catch (IOException e2) {
                this.f7325b.a(e2);
            }
        }
    }
}
