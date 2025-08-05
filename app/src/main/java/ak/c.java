package aK;

import java.util.Iterator;

/* loaded from: TunerStudioMS.jar:aK/c.class */
class c implements A.e {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ a f2561a;

    c(a aVar) {
        this.f2561a = aVar;
    }

    @Override // A.e
    public void a() {
        this.f2561a.f2545m = a.f2536a;
        synchronized (this.f2561a.f2551s) {
            Iterator it = this.f2561a.f2551s.iterator();
            while (it.hasNext()) {
                ((g) it.next()).a(this.f2561a.f2543k);
            }
        }
    }

    @Override // A.e
    public void b() {
        synchronized (this.f2561a.f2551s) {
            Iterator it = this.f2561a.f2551s.iterator();
            while (it.hasNext()) {
                ((g) it.next()).b(this.f2561a.f2543k);
            }
        }
    }

    @Override // A.e
    public void c() {
        synchronized (this.f2561a.f2551s) {
            Iterator it = this.f2561a.f2551s.iterator();
            while (it.hasNext()) {
                ((g) it.next()).b(this.f2561a.f2543k);
            }
        }
    }

    @Override // A.e
    public void d() {
        synchronized (this.f2561a.f2551s) {
            Iterator it = this.f2561a.f2551s.iterator();
            while (it.hasNext()) {
                ((g) it.next()).a(this.f2561a.f2543k);
            }
        }
    }

    @Override // A.e
    public void e() {
    }
}
