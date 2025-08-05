package bt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: TunerStudioMS.jar:bt/bT.class */
class bT extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f8961a;

    /* renamed from: b, reason: collision with root package name */
    long f8962b;

    /* renamed from: c, reason: collision with root package name */
    final List f8963c;

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ bS f8964d;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    bT(bS bSVar) {
        super("ValThrottle 1D");
        this.f8964d = bSVar;
        this.f8961a = true;
        this.f8962b = 250L;
        this.f8963c = new ArrayList();
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.f8961a) {
            try {
                this.f8961a = false;
                Thread.sleep(this.f8962b);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
                return;
            }
        }
        this.f8964d.f8958g = null;
        Iterator it = this.f8963c.iterator();
        while (it.hasNext()) {
            this.f8964d.a((String) it.next());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str) {
        if (!this.f8963c.contains(str)) {
            this.f8963c.add(str);
        }
        a();
    }

    private void a() {
        this.f8961a = true;
    }
}
