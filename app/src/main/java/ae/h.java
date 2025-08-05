package ae;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:ae/h.class */
class h extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ w f4365a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0502f f4366b;

    h(C0502f c0502f, w wVar) {
        this.f4366b = c0502f;
        this.f4365a = wVar;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            C0500d c0500dD = this.f4366b.d();
            ArrayList arrayList = new ArrayList();
            arrayList.add(c0500dD);
            this.f4365a.a(arrayList);
        } catch (IOException e2) {
            Logger.getLogger(C0502f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            C0500d c0500d = new C0500d();
            c0500d.a(C0500d.f4347b);
            c0500d.a("Unable to communicate with the controller.");
            ArrayList arrayList2 = new ArrayList();
            arrayList2.add(c0500d);
            this.f4365a.a(arrayList2);
        }
    }
}
