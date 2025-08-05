package ay;

import bH.C;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* renamed from: ay.i, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ay/i.class */
public class C0932i {

    /* renamed from: c, reason: collision with root package name */
    private static C0932i f6451c = null;

    /* renamed from: a, reason: collision with root package name */
    List f6452a = new ArrayList();

    /* renamed from: d, reason: collision with root package name */
    private boolean f6453d = false;

    /* renamed from: e, reason: collision with root package name */
    private boolean f6454e = false;

    /* renamed from: b, reason: collision with root package name */
    C0933j f6455b = null;

    public static C0932i a() {
        if (f6451c == null) {
            f6451c = new C0932i();
        }
        return f6451c;
    }

    public boolean a(InterfaceC0931h interfaceC0931h) {
        this.f6452a.add(interfaceC0931h);
        return this.f6453d;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(boolean z2) {
        Iterator it = this.f6452a.iterator();
        while (it.hasNext()) {
            ((InterfaceC0931h) it.next()).a(z2);
        }
    }

    public boolean b() {
        return this.f6455b != null && this.f6455b.isAlive() && this.f6455b.f6456a;
    }

    public void c() {
        if (this.f6455b == null || !this.f6455b.isAlive()) {
            this.f6455b = new C0933j(this);
            this.f6455b.start();
            C.d("Starting Internet Monitor");
        }
    }

    public void d() {
        if (this.f6455b != null) {
            this.f6455b.a();
            this.f6455b = null;
            C.d("Stopping Internet Monitor");
        }
    }

    public void a(boolean z2) {
        this.f6454e = z2;
    }
}
