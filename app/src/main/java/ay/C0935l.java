package ay;

import W.C0197w;
import W.R;
import bH.C;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* renamed from: ay.l, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ay/l.class */
public class C0935l implements InterfaceC0931h {

    /* renamed from: c, reason: collision with root package name */
    private static C0935l f6458c = null;

    /* renamed from: a, reason: collision with root package name */
    Map f6459a = new HashMap();

    /* renamed from: d, reason: collision with root package name */
    private File f6461d = null;

    /* renamed from: b, reason: collision with root package name */
    boolean f6460b = C0932i.a().a(this);

    private C0935l() {
    }

    public static C0935l a() {
        if (f6458c == null) {
            f6458c = new C0935l();
        }
        return f6458c;
    }

    public void a(String str, InterfaceC0938o interfaceC0938o) throws V.a {
        if (this.f6461d == null || !this.f6461d.isDirectory()) {
            throw new V.a("Offline ECU Definition Download: Download Directory not set!");
        }
        this.f6459a.put(str, interfaceC0938o);
        if (!C0932i.a().b()) {
            C0932i.a().c();
        }
        if (this.f6460b) {
            b();
        }
    }

    @Override // ay.InterfaceC0931h
    public void a(boolean z2) {
        this.f6460b = z2;
        if (this.f6460b) {
            b();
        }
    }

    private void b() {
        new C0936m(this, "Offline IniDownloader").start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void c() {
        if (this.f6459a.isEmpty()) {
            return;
        }
        for (String str : new ArrayList(this.f6459a.keySet())) {
            C.d("About to attempt ini download, signature: " + str);
            R rA = C0197w.a(str, this.f6461d);
            if (rA.a() && (rA.d() == null || rA.d().isEmpty())) {
                C0937n c0937n = new C0937n(this, str, rA.c());
                c0937n.a(0);
                ((InterfaceC0938o) this.f6459a.get(str)).a(c0937n);
            } else if (rA.a()) {
                C0937n c0937n2 = new C0937n(this, str, rA.c());
                c0937n2.a(1);
                c0937n2.a(rA.d());
                InterfaceC0938o interfaceC0938o = (InterfaceC0938o) this.f6459a.get(str);
                C.b("OfflineIniDownloader failed on signature: " + str + ", due to the error: " + rA.d());
                interfaceC0938o.a(c0937n2);
            } else {
                C0937n c0937n3 = new C0937n(this, str, rA.c());
                c0937n3.a(1);
                c0937n3.a(rA.d());
                ((InterfaceC0938o) this.f6459a.get(str)).a(c0937n3);
            }
            this.f6459a.remove(str);
        }
    }

    public void a(File file) {
        this.f6461d = file;
    }
}
