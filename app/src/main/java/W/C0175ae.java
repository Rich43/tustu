package W;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

/* renamed from: W.ae, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:W/ae.class */
class C0175ae {

    /* renamed from: a, reason: collision with root package name */
    private File f2065a;

    /* renamed from: b, reason: collision with root package name */
    private long f2066b = -1;

    /* renamed from: c, reason: collision with root package name */
    private ArrayList f2067c = new ArrayList();

    /* renamed from: d, reason: collision with root package name */
    private long f2068d = -1;

    /* renamed from: e, reason: collision with root package name */
    private long f2069e = -2;

    /* renamed from: f, reason: collision with root package name */
    private long f2070f = 0;

    public C0175ae(File file) {
        this.f2065a = null;
        this.f2065a = file;
    }

    public long a() {
        return this.f2066b;
    }

    public void a(long j2) {
        this.f2066b = j2;
    }

    public void a(B b2) {
        this.f2067c.add(b2);
    }

    public void b() {
        this.f2066b = this.f2068d;
    }

    public void c() {
        this.f2066b = this.f2069e;
    }

    public void d() {
        a(this.f2065a.lastModified());
    }

    public boolean e() {
        if (a() == this.f2069e) {
            return false;
        }
        if (a() == this.f2068d) {
            a(this.f2065a.lastModified());
        }
        return this.f2065a.lastModified() != a();
    }

    void f() {
        Iterator it = this.f2067c.iterator();
        while (it.hasNext()) {
            ((B) it.next()).a(g());
        }
    }

    public File g() {
        return this.f2065a;
    }

    public void h() {
        this.f2067c.clear();
    }
}
