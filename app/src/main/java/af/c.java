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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:af/c.class */
public class c implements n {

    /* renamed from: h, reason: collision with root package name */
    public static String f4448h = "http://www.msextra.com/forums/viewforum.php?f=101";

    /* renamed from: i, reason: collision with root package name */
    public static String f4449i = "http://www.msextra.com/forums/viewforum.php?f=138";

    /* renamed from: a, reason: collision with root package name */
    cT f4441a = null;

    /* renamed from: b, reason: collision with root package name */
    cP f4442b = null;

    /* renamed from: c, reason: collision with root package name */
    cT f4443c = new J.g();

    /* renamed from: d, reason: collision with root package name */
    cP f4444d = new J.f();

    /* renamed from: e, reason: collision with root package name */
    List f4445e = new ArrayList();

    /* renamed from: f, reason: collision with root package name */
    k f4446f = new k(this);

    /* renamed from: g, reason: collision with root package name */
    b f4447g = new b();

    /* renamed from: k, reason: collision with root package name */
    private String f4450k = f4448h;

    /* renamed from: j, reason: collision with root package name */
    boolean f4451j = true;

    @Override // ae.q
    public String a() {
        return "MS2 Extra Firmware Loader";
    }

    @Override // ae.q
    public String b() {
        return "Firmware loader for loading MS2 Extra version 3.3 and up Firmware.";
    }

    @Override // ae.q
    public List c() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.f4446f);
        if (this.f4451j) {
            arrayList.add(this.f4447g);
        }
        return arrayList;
    }

    @Override // ae.q
    public List d() {
        ArrayList arrayList = new ArrayList();
        if (this.f4451j) {
            arrayList.add(this.f4447g);
        }
        arrayList.add(this.f4446f);
        return arrayList;
    }

    @Override // ae.q
    public cP a(bS bSVar) {
        return this.f4442b;
    }

    @Override // ae.q
    public cT b(bS bSVar) {
        return this.f4441a;
    }

    @Override // af.n
    public void h() {
        this.f4441a = this.f4443c;
        this.f4442b = this.f4444d;
    }

    @Override // af.n
    public void i() {
        this.f4441a = null;
        this.f4442b = null;
    }

    @Override // af.n
    public boolean j() {
        return this.f4441a == null && this.f4442b == null;
    }

    @Override // ae.q
    public boolean a(ae.m mVar) {
        return (mVar.b() | 1) == mVar.b();
    }

    @Override // ae.q
    public boolean a(ae.k kVar) {
        File file;
        List listD = kVar.d();
        for (int i2 = 0; i2 < listD.size(); i2++) {
            try {
                C0493a c0493aC = kVar.c((File) listD.get(i2));
                if (c0493aC.a().size() > 0) {
                    String str = new String(C0995c.a(((C0496d) c0493aC.a().get(0)).e()));
                    if (str.contains("megasquirt2.s19") || str.contains("microsquirt.s19") || str.contains("microsquirt-module.s19") || str.contains("ms2_extra.s19") || str.contains("ms2_extra_us.s19") || str.contains("mspnp2.s19")) {
                        this.f4451j = true;
                        this.f4446f.a(false);
                        return true;
                    }
                    if (str.contains("trans.s19")) {
                        this.f4451j = false;
                    }
                }
                file = (File) listD.get(i2);
            } catch (C0495c e2) {
                Logger.getLogger(c.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            if (file.getName().startsWith("iobox") && file.getName().endsWith(".s19")) {
                return true;
            }
            if (file.getName().startsWith("trans") && file.getName().endsWith(".s19")) {
                return true;
            }
        }
        C.b("Selected Firmware Package not suitable for MS2e Firmware Loader.");
        return false;
    }

    @Override // ae.q
    public boolean a(ae.m mVar, File file) {
        if (file.getName().startsWith("trans")) {
            this.f4450k = f4449i;
        } else {
            this.f4450k = f4448h;
        }
        return (file.getName().equals("microsquirt.s19") || (file.getName().startsWith("iobox") && file.getName().endsWith(".s19")) || (file.getName().startsWith("trans") && file.getName().endsWith(".s19"))) ? (mVar.b() & ae.o.f4413C) > 0 : file.getName().equals("microsquirt-module.s19") ? (mVar.b() & ae.o.f4416F) > 0 : file.getName().equals("mspnp2.s19") ? (mVar.b() & ae.o.f4414D) > 0 : file.getName().equals("megasquirt2.s19") && (mVar.b() & ae.o.f4406v) > 0;
    }

    @Override // ae.q
    public boolean b(ae.m mVar, File file) {
        return file.getName().equals("microsquirt.s19") ? (mVar.b() & ae.o.f4413C) > 0 : file.getName().equals("microsquirt-module.s19") ? (mVar.b() & ae.o.f4416F) > 0 : file.getName().equals("mspnp2.s19") ? (mVar.b() & ae.o.f4414D) > 0 : file.getName().equals("megasquirt2.s19") ? (mVar.b() & ae.o.f4412B) > 0 : file.getName().equals("trans.s19") ? (mVar.b() & ae.o.f4406v) > 0 : file.getName().equals("iobox.s19") && (mVar.b() & ae.o.f4406v) > 0;
    }

    @Override // ae.q
    public C0500d a(ae.k kVar, p pVar, u uVar) {
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
            c0500dC = j.a(kVar.c(fileG), pVar, uVar, new d(this));
            return c0500dC;
        } catch (C0495c e2) {
            c0500dC.a(C0500d.f4347b);
            c0500dC.a("Unable to read S19 File.");
            C.a("Unable to parse S19 File: " + fileG.getAbsolutePath());
            e2.printStackTrace();
            return c0500dC;
        } catch (v e3) {
            Logger.getLogger(c.class.getName()).log(Level.SEVERE, "Unexpected Protocol Error", (Throwable) e3);
            c0500dC.a(C0500d.f4347b);
            c0500dC.a("Unexpected Protocol Error");
            return c0500dC;
        }
    }

    @Override // ae.q
    public String e() {
        return this.f4450k;
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
        return file.getName().equalsIgnoreCase("megasquirt2.s19") ? ae.o.f4390f : (file.getName().equalsIgnoreCase("microsquirt.s19") || file.getName().equalsIgnoreCase("ms2_extra_us.s19")) ? ae.o.f4389e : file.getName().equalsIgnoreCase("microsquirt-module.s19") ? ae.o.f4391g : file.getName().equalsIgnoreCase("ms2_extra.s19") ? ae.o.f4387c : file.getName().equalsIgnoreCase("ms2_extra_us.s19") ? ae.o.f4391g : file.getName().equalsIgnoreCase("mspnp2.s19") ? ae.o.f4393i : file.getName().equalsIgnoreCase("trans.s19") ? ae.o.f4387c : "";
    }

    @Override // ae.q
    public void a(String str) {
    }

    @Override // ae.q
    public boolean g() {
        return false;
    }

    @Override // ae.q
    public String b(ae.m mVar) {
        return null;
    }
}
