package B;

import A.y;
import G.C0129l;
import bH.C;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:B/a.class */
public final class a implements A.f, j {

    /* renamed from: h, reason: collision with root package name */
    private String f92h;

    /* renamed from: c, reason: collision with root package name */
    public static String f90c = "Auto TCP / UDP by Serial Number";

    /* renamed from: f, reason: collision with root package name */
    public static String f94f = "Serial Number";

    /* renamed from: d, reason: collision with root package name */
    boolean f91d = true;

    /* renamed from: e, reason: collision with root package name */
    List f93e = null;

    /* renamed from: g, reason: collision with root package name */
    String f95g = null;

    /* renamed from: a, reason: collision with root package name */
    l f88a = new l();

    /* renamed from: b, reason: collision with root package name */
    o f89b = new o();

    public void b(String str) {
        if (this.f92h != null && !this.f92h.isEmpty()) {
            g.a().b(this.f92h, this);
        }
        C.d("Setting AutoIpControllerInterface to serial number: " + str);
        i iVarB = g.a().b(str);
        this.f92h = str;
        a(str, iVarB);
        g.a().a(str, this);
    }

    public void a(i iVar, String str) {
        if (this.f92h != null && !this.f92h.isEmpty()) {
            g.a().b(this.f92h, this);
        }
        this.f92h = iVar.e();
        this.f95g = str;
        C.d("Setting AutoIpControllerInterface to serial number: " + this.f92h + ", controller: " + str);
        a(this.f92h, iVar);
        g.a().a(this.f92h, this);
    }

    @Override // A.f
    public void f() throws C0129l {
        if (this.f91d) {
            this.f89b.f();
        } else {
            this.f88a.f();
        }
    }

    @Override // A.f
    public void g() {
        if (this.f91d) {
            this.f89b.g();
        } else {
            this.f88a.g();
        }
    }

    @Override // A.f
    public String h() {
        return f90c;
    }

    @Override // A.f
    public InputStream i() {
        return this.f91d ? this.f89b.i() : this.f88a.i();
    }

    @Override // A.f
    public OutputStream j() {
        return this.f91d ? this.f89b.j() : this.f88a.j();
    }

    @Override // A.f
    public int k() {
        return this.f91d ? this.f89b.k() : this.f88a.k();
    }

    @Override // A.f
    public void a(A.e eVar) {
        this.f89b.a(eVar);
        this.f88a.a(eVar);
    }

    @Override // A.f
    public void b(A.e eVar) {
        this.f89b.b(eVar);
        this.f88a.b(eVar);
    }

    @Override // A.f
    public List l() {
        this.f93e = new ArrayList();
        A.b bVar = new A.b();
        bVar.a(f94f);
        bVar.b("The Serial Number of your ECU. When on the network, it will appear in the dropdown.");
        bVar.a(0);
        boolean z2 = false;
        for (i iVar : g.a().e()) {
            if (iVar.e() != null && !iVar.e().isEmpty()) {
                bVar.a((Object) iVar.e());
                if (this.f92h != null && this.f92h.equals(iVar.e())) {
                    z2 = true;
                }
            }
        }
        if (!z2 && this.f92h != null && !this.f92h.isEmpty()) {
            bVar.a((Object) this.f92h);
        }
        this.f93e.add(bVar);
        return this.f93e;
    }

    @Override // A.f
    public void a(String str, Object obj) {
        if (obj == null || !str.equals(f94f)) {
            C.c("Unknown Setting Name: " + str);
        } else {
            b(obj.toString());
        }
    }

    @Override // A.f
    public Object a(String str) {
        return (str == null || !str.equals(f94f)) ? this.f91d ? this.f89b.a(str) : this.f88a.a(str) : this.f92h;
    }

    @Override // A.f
    public boolean m() {
        return this.f91d ? this.f89b.m() : this.f88a.m();
    }

    @Override // A.f
    public boolean a(int i2) {
        return this.f91d ? this.f89b.a(i2) : this.f88a.a(i2);
    }

    @Override // A.f
    public String n() {
        return this.f91d ? this.f89b.n() : this.f88a.n();
    }

    @Override // A.f
    public int o() {
        return this.f91d ? this.f89b.o() : this.f88a.o();
    }

    @Override // A.f
    public int p() {
        return this.f91d ? this.f89b.p() : this.f88a.p();
    }

    @Override // A.f
    public boolean q() {
        return this.f91d ? this.f89b.q() : this.f88a.q();
    }

    @Override // A.f
    public boolean r() {
        return this.f91d ? this.f89b.r() : this.f88a.r();
    }

    @Override // A.f
    public int s() {
        return this.f91d ? this.f89b.s() : this.f88a.s();
    }

    @Override // B.j
    public void a(String str, i iVar) {
        b.c().a(iVar);
        try {
            i iVarA = a(iVar);
            if (iVarA == null) {
                this.f89b.a(o.f194i, "");
                this.f88a.a(l.f178j, "");
            } else {
                this.f89b.a(o.f194i, iVarA.c());
                this.f88a.a(l.f178j, iVarA.c());
                this.f89b.a(o.f195j, Integer.valueOf(iVarA.g()));
                this.f88a.a(l.f179k, Integer.valueOf(iVarA.g()));
                this.f91d = iVar.d().equalsIgnoreCase("UDP");
            }
        } catch (A.s e2) {
            Logger.getLogger(a.class.getName()).log(Level.SEVERE, "Failed to set IP Setting", (Throwable) e2);
        }
    }

    private i a(i iVar) {
        if (iVar == null) {
            return null;
        }
        if (iVar.m().size() != 0 && this.f95g != null) {
            for (i iVar2 : iVar.m()) {
                if (iVar2.j().equals(this.f95g)) {
                    return iVar2;
                }
            }
            return iVar;
        }
        return iVar;
    }

    public String a() {
        return this.f92h;
    }

    @Override // A.f
    public void a(y yVar) {
    }

    @Override // A.f
    public void b(y yVar) {
    }
}
