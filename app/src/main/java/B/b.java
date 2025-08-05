package B;

import java.util.ArrayList;
import java.util.List;

/* loaded from: TunerStudioMS.jar:B/b.class */
public class b implements A.i {

    /* renamed from: b, reason: collision with root package name */
    private A.q f97b;

    /* renamed from: c, reason: collision with root package name */
    private static b f98c = null;

    /* renamed from: a, reason: collision with root package name */
    private final List f96a = new ArrayList();

    /* renamed from: d, reason: collision with root package name */
    private i f99d = null;

    private b() {
        this.f97b = null;
        A.q qVar = new A.q();
        qVar.a(a.f90c);
        qVar.b("Auto WiFi / Ethernet");
        qVar.a(a.class);
        this.f96a.add(qVar);
        this.f97b = qVar;
        A.q qVar2 = new A.q();
        qVar2.a(o.f184b);
        qVar2.b("UDP WiFi / Ethernet");
        qVar2.a(o.class);
        this.f96a.add(qVar2);
        A.q qVar3 = new A.q();
        qVar3.a(l.f168b);
        qVar3.b("TCP/IP - WiFi / Ethernet");
        qVar3.a(l.class);
        this.f96a.add(qVar3);
    }

    public static b c() {
        if (f98c == null) {
            f98c = new b();
        }
        return f98c;
    }

    @Override // A.i
    public List a() {
        return this.f96a;
    }

    @Override // A.i
    public A.q b() {
        return this.f97b;
    }

    @Override // A.i
    public A.f a(String str, String str2) {
        if (this.f99d != null) {
            a aVar = new a();
            aVar.b(this.f99d.e());
            return aVar;
        }
        for (A.q qVar : this.f96a) {
            if (qVar.a().equals(str)) {
                return qVar.c(str2);
            }
        }
        return null;
    }

    @Override // A.i
    public A.f a(String str, i iVar, String str2) {
        if (this.f99d != null) {
            a aVar = new a();
            aVar.a(this.f99d, str2);
            return aVar;
        }
        for (A.q qVar : this.f96a) {
            if (qVar.a().equals(str)) {
                return qVar.c(str2);
            }
        }
        return null;
    }

    public i d() {
        return this.f99d;
    }

    public void a(i iVar) {
        this.f99d = iVar;
    }
}
