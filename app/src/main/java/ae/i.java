package ae;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:ae/i.class */
class i extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ w f4367a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0502f f4368b;

    i(C0502f c0502f, w wVar) {
        this.f4368b = c0502f;
        this.f4367a = wVar;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        List arrayList = null;
        try {
            try {
                arrayList = this.f4368b.e();
                if (arrayList == null) {
                    C0500d c0500d = new C0500d();
                    c0500d.a(C0500d.f4347b);
                    c0500d.a("Unhandled Error occurred.");
                    arrayList = new ArrayList();
                    arrayList.add(c0500d);
                }
                this.f4367a.a(arrayList);
            } catch (IOException e2) {
                Logger.getLogger(C0502f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                C0500d c0500d2 = new C0500d();
                c0500d2.a(C0500d.f4347b);
                c0500d2.a("Unable to communicate with the controller.");
                arrayList = new ArrayList();
                arrayList.add(c0500d2);
                if (arrayList == null) {
                    C0500d c0500d3 = new C0500d();
                    c0500d3.a(C0500d.f4347b);
                    c0500d3.a("Unhandled Error occurred.");
                    arrayList = new ArrayList();
                    arrayList.add(c0500d3);
                }
                this.f4367a.a(arrayList);
            }
        } catch (Throwable th) {
            if (arrayList == null) {
                C0500d c0500d4 = new C0500d();
                c0500d4.a(C0500d.f4347b);
                c0500d4.a("Unhandled Error occurred.");
                arrayList = new ArrayList();
                arrayList.add(c0500d4);
            }
            this.f4367a.a(arrayList);
            throw th;
        }
    }
}
