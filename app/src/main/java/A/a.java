package A;

import bH.C;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: TunerStudioMS.jar:A/a.class */
public abstract class a implements f {

    /* renamed from: b, reason: collision with root package name */
    private final Collection f1b = new CopyOnWriteArrayList();

    /* renamed from: a, reason: collision with root package name */
    ArrayList f2a = new ArrayList();

    @Override // A.f
    public void a(y yVar) {
        if (this.f2a.contains(yVar)) {
            return;
        }
        this.f2a.add(yVar);
    }

    @Override // A.f
    public void b(y yVar) {
        this.f2a.remove(yVar);
    }

    @Override // A.f
    public void a(e eVar) {
        if (this.f1b.contains(eVar)) {
            return;
        }
        this.f1b.add(eVar);
    }

    @Override // A.f
    public void b(e eVar) {
        this.f1b.remove(eVar);
    }

    protected void a() {
        Iterator it = this.f1b.iterator();
        while (it.hasNext()) {
            try {
                ((e) it.next()).c();
            } catch (Exception e2) {
                C.a("Exception in notifyConnected()");
                C.a(e2);
            }
        }
    }

    protected void b() {
        Iterator it = this.f1b.iterator();
        while (it.hasNext()) {
            try {
                ((e) it.next()).d();
            } catch (Exception e2) {
                C.a("Exception in notifyConnectionAttemptFailed()");
                C.a(e2);
            }
        }
    }

    protected void c() {
        Iterator it = this.f1b.iterator();
        while (it.hasNext()) {
            try {
                ((e) it.next()).b();
            } catch (Exception e2) {
                C.a("Exception in notifyConnectionAttemptStarting()");
                C.a(e2);
            }
        }
    }

    protected void d() {
        Iterator it = this.f1b.iterator();
        while (it.hasNext()) {
            try {
                ((e) it.next()).a();
            } catch (Exception e2) {
                C.a("Exception in notifyConnectionLost()");
                C.a(e2);
            }
        }
    }

    protected void e() {
        Iterator it = this.f1b.iterator();
        while (it.hasNext()) {
            try {
                ((e) it.next()).e();
            } catch (Exception e2) {
                C.a("Exception in notifyConnectionClosing()");
                C.a(e2);
            }
        }
    }
}
