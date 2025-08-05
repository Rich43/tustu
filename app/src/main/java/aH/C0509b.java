package ah;

import G.bS;
import G.cP;
import G.cT;
import ae.C0500d;
import ae.k;
import ae.m;
import ae.p;
import ae.q;
import ae.u;
import bH.C;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: ah.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ah/b.class */
public class C0509b implements q {

    /* renamed from: a, reason: collision with root package name */
    List f4503a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    List f4504b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    C0510c f4505c = new C0510c();

    public C0509b() {
        this.f4504b.add(this.f4505c);
    }

    @Override // ae.q
    public String a() {
        return "BigStuff3 Gen4 Firmware Loader";
    }

    @Override // ae.q
    public String b() {
        return "BigStuff3 Gen4 Type 1 Firmware Loader.";
    }

    @Override // ae.q
    public List c() {
        return this.f4504b;
    }

    @Override // ae.q
    public List d() {
        return this.f4504b;
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
        return true;
    }

    @Override // ae.q
    public boolean a(k kVar) {
        return true;
    }

    @Override // ae.q
    public C0500d a(k kVar, p pVar, u uVar) {
        C0500d c0500d = new C0500d();
        uVar.a("Installing Firmware. Do not power off!");
        uVar.a(0.0d);
        long jCurrentTimeMillis = System.currentTimeMillis();
        boolean zA = false;
        while (!zA) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e2) {
                Logger.getLogger(C0509b.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            zA = this.f4505c.a(pVar);
            C.c("boxIsBack = " + zA);
            if (zA) {
                uVar.a(1.0d);
            } else {
                if (System.currentTimeMillis() - jCurrentTimeMillis > 120000 * 1.5d) {
                    c0500d.a(C0500d.f4348c);
                    uVar.a("Controller should have responded by now. Disconnect Boot jump and power cycle.");
                    return c0500d;
                }
                uVar.a((System.currentTimeMillis() - jCurrentTimeMillis) / 120000);
            }
        }
        if (System.currentTimeMillis() - jCurrentTimeMillis >= 120000 / 3) {
            c0500d.a(C0500d.f4346a);
            return c0500d;
        }
        c0500d.a(C0500d.f4347b);
        uVar.a("Controller not Boot Strapped.\nMake sure Pin Y2 is to 12 Volts. Then Power Cycle.");
        c0500d.a("Controller not Boot Strapped.\nMake sure Pin Y2 is to 12 Volts. Then Power Cycle.");
        return c0500d;
    }

    @Override // ae.q
    public boolean a(m mVar, File file) {
        return true;
    }

    @Override // ae.q
    public boolean b(m mVar, File file) {
        return true;
    }

    @Override // ae.q
    public String a(File file) {
        return "Unverified BigStuff Gen4 firmware";
    }

    @Override // ae.q
    public String e() {
        return "http://www.bigstuff3.com";
    }

    @Override // ae.q
    public bS a(p pVar) {
        return new bS();
    }

    @Override // ae.q
    public void f() {
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
