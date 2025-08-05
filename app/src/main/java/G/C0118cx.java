package G;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: G.cx, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/cx.class */
class C0118cx extends Thread {

    /* renamed from: a, reason: collision with root package name */
    List f1169a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    final Object f1170b = new Object();

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C0117cw f1171c;

    C0118cx(C0117cw c0117cw) {
        this.f1171c = c0117cw;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (true) {
            synchronized (this.f1170b) {
                try {
                    this.f1170b.wait(10000L);
                } catch (InterruptedException e2) {
                    Logger.getLogger(C0117cw.class.getName()).log(Level.SEVERE, "ChannelListenerRemover died?", (Throwable) e2);
                }
            }
            if (!this.f1169a.isEmpty()) {
                ArrayList arrayList = new ArrayList();
                while (this.f1169a.size() > 0) {
                    arrayList.add(this.f1169a.remove(0));
                }
                Iterator<E> it = arrayList.iterator();
                while (it.hasNext()) {
                    C0113cs.a().a((InterfaceC0109co) it.next());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(InterfaceC0109co interfaceC0109co) {
        this.f1169a.add(interfaceC0109co);
        synchronized (this.f1170b) {
            this.f1170b.notifyAll();
        }
    }
}
