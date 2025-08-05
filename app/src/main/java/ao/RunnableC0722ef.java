package ao;

import java.util.Iterator;

/* renamed from: ao.ef, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/ef.class */
class RunnableC0722ef implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0718eb f5627a;

    RunnableC0722ef(C0718eb c0718eb) {
        this.f5627a = c0718eb;
    }

    @Override // java.lang.Runnable
    public void run() {
        Iterator it = this.f5627a.f5615a.keySet().iterator();
        while (it.hasNext()) {
            C0764fu c0764fu = (C0764fu) this.f5627a.f5615a.get(it.next());
            c0764fu.a(this.f5627a.f5619e);
            c0764fu.repaint();
        }
        this.f5627a.e();
        this.f5627a.f5618d.a(0.0d);
        if (this.f5627a.f5619e != null) {
            this.f5627a.f5618d.b(this.f5627a.f5619e.d());
        }
        this.f5627a.f5618d.c(0.0d);
        if (this.f5627a.f5619e != null) {
            this.f5627a.f5618d.d(this.f5627a.f5619e.d());
        }
        Iterator it2 = this.f5627a.f5615a.keySet().iterator();
        while (it2.hasNext()) {
            C0764fu c0764fu2 = (C0764fu) this.f5627a.f5615a.get(it2.next());
            c0764fu2.c();
            c0764fu2.repaint();
        }
    }
}
