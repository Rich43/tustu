package ag;

import G.bS;
import G.cP;
import G.cT;
import ad.C0493a;
import ad.C0495c;
import ad.C0496d;
import ae.C0500d;
import ae.k;
import ae.m;
import ae.o;
import ae.p;
import ae.q;
import ae.u;
import ae.v;
import af.j;
import bH.C;
import bH.C0995c;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: ag.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ag/a.class */
public class C0505a implements q {

    /* renamed from: a, reason: collision with root package name */
    List f4494a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    C0507c f4495b = new C0507c();

    @Override // ae.q
    public String a() {
        return "MS2 B&G Firmware Loader";
    }

    @Override // ae.q
    public String b() {
        return "Firmware Loader for B&G Firmwares including MegaSquirt-II B&G, MShift and GPIO";
    }

    @Override // ae.q
    public List c() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.f4495b);
        return arrayList;
    }

    @Override // ae.q
    public List d() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.f4495b);
        return arrayList;
    }

    @Override // ae.q
    public cP a(bS bSVar) {
        return null;
    }

    @Override // ae.q
    public cT b(bS bSVar) {
        return null;
    }

    @Override // ae.q
    public boolean a(m mVar) {
        return (mVar.b() | 1) == mVar.b();
    }

    @Override // ae.q
    public boolean a(k kVar) {
        List listD = kVar.d();
        for (int i2 = 0; i2 < listD.size(); i2++) {
            try {
                C0493a c0493aC = kVar.c((File) listD.get(i2));
                if (c0493aC.a().size() > 0) {
                    String str = new String(C0995c.a(((C0496d) c0493aC.a().get(0)).e()));
                    if (!str.contains("megasquirt2.s19") && !str.contains("microsquirt.s19") && !str.contains("microsquirt-module.s19") && !str.contains("ms2_extra.s19") && !str.contains("ms2_extra_us.s19") && !str.contains("trans.s19") && !str.contains("mspnp2.s19")) {
                        File file = (File) listD.get(i2);
                        if (file.getName().toLowerCase().startsWith("monitor") && file.getName().endsWith(".s19")) {
                            return true;
                        }
                        if (file.getName().toLowerCase().startsWith("lct") && file.getName().endsWith(".s19")) {
                            return true;
                        }
                        if (file.getName().toLowerCase().startsWith("pico") && file.getName().endsWith(".s19")) {
                            return true;
                        }
                    }
                } else {
                    continue;
                }
            } catch (C0495c e2) {
                Logger.getLogger(C0505a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
        C.b("Selected Firmware Package not suitable for MS2 B&G Firmware Loader Firmware Loader.");
        return false;
    }

    @Override // ae.q
    public C0500d a(k kVar, p pVar, u uVar) {
        C0500d c0500dA = j.a(pVar);
        if (c0500dA.a() == C0500d.f4347b) {
            return c0500dA;
        }
        C0500d c0500dC = j.c(pVar);
        if (c0500dC.a() == C0500d.f4347b) {
            return c0500dC;
        }
        File fileG = kVar.g();
        if (fileG == null) {
            c0500dC.a(C0500d.f4347b);
            c0500dC.a("No Firmware File Selected!");
            return c0500dC;
        }
        C.d("Loading Firmware File: " + fileG.getAbsolutePath());
        try {
            c0500dC = j.a(kVar.c(fileG), pVar, uVar, new C0506b(this));
            return c0500dC;
        } catch (C0495c e2) {
            c0500dC.a(C0500d.f4347b);
            c0500dC.a("Unable to read S19 File.");
            C.a("Unable to parse S19 File: " + fileG.getAbsolutePath());
            e2.printStackTrace();
            return c0500dC;
        } catch (v e3) {
            Logger.getLogger(af.c.class.getName()).log(Level.SEVERE, "Unexpected Protocol Error", (Throwable) e3);
            c0500dC.a(C0500d.f4347b);
            c0500dC.a("Unexpected Protocol Error");
            return c0500dC;
        }
    }

    @Override // ae.q
    public boolean a(m mVar, File file) {
        return (mVar.b() & o.f4406v) > 0 && (mVar.b() & o.f4414D) <= 0;
    }

    @Override // ae.q
    public boolean b(m mVar, File file) {
        return (mVar.b() & o.f4406v) > 0 && (mVar.b() & o.f4414D) <= 0;
    }

    @Override // ae.q
    public String a(File file) {
        return file.getName().toLowerCase().startsWith("monitor") ? o.f4387c : "";
    }

    @Override // ae.q
    public String e() {
        return "http://www.msefi.com/";
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
    public void a(String str) {
    }

    @Override // ae.q
    public boolean g() {
        return false;
    }

    @Override // ae.q
    public String b(m mVar) {
        return null;
    }
}
