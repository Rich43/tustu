package af;

import G.bS;
import G.cP;
import G.cT;
import ad.C0493a;
import ad.C0495c;
import ad.C0496d;
import ae.C0500d;
import ae.p;
import ae.u;
import ae.v;
import bH.C;
import bH.C0995c;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:af/f.class */
public class f implements n {

    /* renamed from: a, reason: collision with root package name */
    cT f4467a = null;

    /* renamed from: b, reason: collision with root package name */
    cP f4468b = null;

    /* renamed from: c, reason: collision with root package name */
    cT f4469c = new J.g();

    /* renamed from: d, reason: collision with root package name */
    cP f4470d = new J.f();

    /* renamed from: e, reason: collision with root package name */
    List f4471e = new ArrayList();

    /* renamed from: f, reason: collision with root package name */
    m f4472f = new m(this);

    /* renamed from: g, reason: collision with root package name */
    e f4473g = new e();

    /* renamed from: h, reason: collision with root package name */
    String f4474h = null;

    @Override // ae.q
    public String a() {
        return "MS3 Firmware Loader";
    }

    @Override // ae.q
    public String b() {
        return "Firmware loader for loading MS3 and MS3-Pro with version 1.1 and up Firmware.";
    }

    @Override // ae.q
    public List c() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.f4472f);
        arrayList.add(this.f4473g);
        return arrayList;
    }

    @Override // ae.q
    public List d() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.f4473g);
        arrayList.add(this.f4472f);
        return arrayList;
    }

    @Override // ae.q
    public boolean a(ae.m mVar) {
        return (mVar.b() | ae.o.f4407w) == mVar.b();
    }

    @Override // ae.q
    public boolean a(ae.k kVar) {
        List listD = kVar.d();
        for (int i2 = 0; i2 < listD.size(); i2++) {
            try {
                C0493a c0493aC = kVar.c((File) listD.get(i2));
                if (c0493aC.a().size() > 0) {
                    String str = new String(C0995c.a(((C0496d) c0493aC.a().get(0)).e()));
                    if (str.contains("ms3.s19") || str.contains("ms3pro-ult.s19") || str.contains("ms3pro-evo.s19") || str.contains("ms3pro-mini.s19") || str.contains("ms3pro-mod2.s19") || str.contains("ms3pro-plus.s19") || str.contains("ms3pro.s19")) {
                        return true;
                    }
                } else {
                    continue;
                }
            } catch (C0495c e2) {
                Logger.getLogger(c.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
        C.b("Selected Firmware Package not suitable for MS3 Firmware Loader.");
        return false;
    }

    @Override // ae.q
    public boolean a(ae.m mVar, File file) {
        return file.getName().equals("ms3pro.s19") ? (mVar.b() & ae.o.f4411A) > 0 : file.getName().equals("ms3pro-evo.s19") ? (mVar.b() & ae.o.f4419I) > 0 : file.getName().equals("ms3pro-ult.s19") ? (mVar.b() & ae.o.f4420J) > 0 : file.getName().equals("ms3pro-mini.s19") ? (mVar.b() & ae.o.f4421K) > 0 : (file.getName().equals("ms3pro-mod2.s19") || file.getName().equals("ms3pro-plus.s19")) ? (mVar.b() & ae.o.f4422L) > 0 : file.getName().equals("ms3.s19") && (mVar.b() & ae.o.f4411A) == 0 && (mVar.b() & ae.o.f4419I) == 0 && (mVar.b() & ae.o.f4420J) == 0 && (mVar.b() & ae.o.f4421K) == 0 && (mVar.b() & ae.o.f4422L) == 0 && (mVar.b() & ae.o.f4407w) > 0;
    }

    @Override // ae.q
    public boolean b(ae.m mVar, File file) {
        return file.getName().equals("ms3pro.s19") ? (mVar.b() & ae.o.f4411A) > 0 : file.getName().equals("ms3pro-evo.s19") ? (mVar.b() & ae.o.f4419I) > 0 : file.getName().equals("ms3pro-ult.s19") ? (mVar.b() & ae.o.f4420J) > 0 : file.getName().equals("ms3pro-mini.s19") ? (mVar.b() & ae.o.f4421K) > 0 : (file.getName().equals("ms3pro-mod2.s19") || file.getName().equals("ms3pro-plus.s19")) ? (mVar.b() & ae.o.f4422L) > 0 : file.getName().equals("ms3.s19") && (mVar.b() & ae.o.f4411A) == 0 && (mVar.b() & ae.o.f4419I) == 0 && (mVar.b() & ae.o.f4420J) == 0 && (mVar.b() & ae.o.f4421K) == 0 && (mVar.b() & ae.o.f4422L) == 0 && (mVar.b() & ae.o.f4407w) > 0;
    }

    @Override // ae.q
    public C0500d a(ae.k kVar, p pVar, u uVar) {
        C0500d c0500dA = j.a(pVar);
        if (c0500dA.a() == C0500d.f4347b) {
            return c0500dA;
        }
        File fileG = kVar.g();
        if (fileG == null) {
            c0500dA.a(C0500d.f4347b);
            c0500dA.a("No Firmware File Selected!");
            return c0500dA;
        }
        C.d("Loading Firmware File: " + fileG.getAbsolutePath());
        try {
            C0493a c0493aC = kVar.c(fileG);
            h hVarA = h.a(c0493aC.c(), this.f4474h);
            uVar.a(0.0d);
            uVar.a("Erasing main flash");
            C0500d c0500dA2 = j.a(pVar, a(c0493aC), uVar, this.f4473g);
            if (c0500dA2.a() == C0500d.f4347b) {
                return c0500dA2;
            }
            g gVar = new g(this, this.f4473g.c(), hVarA);
            uVar.a("Loading firmware");
            c0500dA = j.b(c0493aC, pVar, uVar, gVar);
            uVar.a("");
            return c0500dA;
        } catch (C0495c e2) {
            c0500dA.a(C0500d.f4347b);
            c0500dA.a("Unable to read S19 File.");
            C.a("Unable to parse S19 File: " + fileG.getAbsolutePath());
            e2.printStackTrace();
            return c0500dA;
        } catch (v e3) {
            Logger.getLogger(c.class.getName()).log(Level.SEVERE, "Unexpected Protocol Error", (Throwable) e3);
            c0500dA.a(C0500d.f4347b);
            c0500dA.a("Unexpected Protocol Error");
            return c0500dA;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private List a(C0493a c0493a) {
        ArrayList arrayList = new ArrayList();
        HashMap map = new HashMap();
        for (C0496d c0496d : c0493a.b()) {
            if (c0496d.d() > 2097152 && c0496d.d() < 8126464) {
                int iD = c0496d.d() >>> 16;
                if (map.get(Integer.valueOf(iD)) == 0) {
                    l lVar = new l(iD);
                    map.put(Integer.valueOf(iD), lVar);
                    lVar.a(c0496d.d());
                    arrayList.add(lVar);
                } else {
                    (0 == 0 ? (l) map.get(Integer.valueOf(iD)) : null).a(c0496d.d());
                }
            }
        }
        return arrayList;
    }

    @Override // ae.q
    public String e() {
        return "http://www.msextra.com/forums/viewforum.php?f=101";
    }

    @Override // ae.q
    public bS a(p pVar) {
        return j.f(pVar);
    }

    @Override // ae.q
    public void f() {
        j.a();
    }

    @Override // ae.q
    public String a(File file) {
        return file.getName().equalsIgnoreCase("ms3pro.s19") ? ae.o.f4396l : file.getName().equalsIgnoreCase("ms3pro-ult.s19") ? ae.o.f4398n : file.getName().equalsIgnoreCase("ms3pro-evo.s19") ? ae.o.f4397m : file.getName().equalsIgnoreCase("ms3pro-mini.s19") ? ae.o.f4399o : (file.getName().equalsIgnoreCase("ms3pro-mod2.s19") || file.getName().equals("ms3pro-plus.s19")) ? ae.o.f4400p : file.getName().equalsIgnoreCase("ms3.s19") ? ae.o.f4395k : "";
    }

    @Override // ae.q
    public void a(String str) {
        this.f4474h = str;
        this.f4473g.a(this.f4474h);
    }

    @Override // ae.q
    public cP a(bS bSVar) {
        return this.f4468b;
    }

    @Override // ae.q
    public cT b(bS bSVar) {
        return this.f4467a;
    }

    @Override // af.n
    public void h() {
        this.f4467a = this.f4469c;
        this.f4468b = this.f4470d;
    }

    @Override // af.n
    public void i() {
        this.f4467a = null;
        this.f4468b = null;
    }

    @Override // af.n
    public boolean j() {
        return this.f4467a == null && this.f4468b == null;
    }

    @Override // ae.q
    public boolean g() {
        return this.f4473g.d();
    }

    @Override // ae.q
    public String b(ae.m mVar) {
        if ((mVar.b() & ae.o.f4411A) > 0) {
            return "ms3pro.s19";
        }
        if ((mVar.b() & ae.o.f4419I) > 0) {
            return "ms3pro-evo.s19";
        }
        if ((mVar.b() & ae.o.f4420J) > 0) {
            return "ms3pro-ult.s19";
        }
        if ((mVar.b() & ae.o.f4421K) > 0) {
            return "ms3pro-mini.s19";
        }
        if ((mVar.b() & ae.o.f4422L) > 0) {
            return "ms3pro-plus.s19";
        }
        if ((mVar.b() & ae.o.f4411A) == 0 && (mVar.b() & ae.o.f4419I) == 0 && (mVar.b() & ae.o.f4420J) == 0 && (mVar.b() & ae.o.f4421K) == 0 && (mVar.b() & ae.o.f4422L) == 0 && (mVar.b() & ae.o.f4407w) > 0) {
            return "ms3.s19";
        }
        return null;
    }
}
