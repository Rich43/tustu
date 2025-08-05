package ah;

import A.f;
import A.r;
import B.l;
import G.C0129l;
import ae.C0500d;
import ae.k;
import ae.p;
import ae.s;
import ae.u;
import ae.v;
import bH.C;
import bH.C0995c;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: ah.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ah/c.class */
public class C0510c implements s {

    /* renamed from: c, reason: collision with root package name */
    private static final byte[] f4507c = {2, 0, 0, 0, -1, 0};

    /* renamed from: d, reason: collision with root package name */
    private static final byte[] f4508d = {1, 0, 1, 0, -2};

    /* renamed from: a, reason: collision with root package name */
    List f4506a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    File f4509b = null;

    @Override // ae.s
    public List a(k kVar) {
        return this.f4506a;
    }

    @Override // ae.s
    public List b(k kVar) {
        return this.f4506a;
    }

    @Override // ae.s
    public void a(String str, Object obj) {
    }

    @Override // ae.s
    public C0500d a(k kVar, p pVar, u uVar) {
        C0500d c0500d = new C0500d();
        c(pVar);
        int i2 = 0;
        while (!a(pVar)) {
            uVar.b("Power On BigStuff3 Gen4");
            int i3 = i2;
            i2++;
            if (i3 > 3) {
                uVar.a("BigStuff3 Gen4 box not found, firmware load cancelled.");
                c0500d.a(C0500d.f4347b);
                c0500d.a("BigStuff3 Gen4 box not found, firmware load cancelled.");
                return c0500d;
            }
        }
        String strB = b(pVar);
        if (strB == null) {
            c0500d.a(C0500d.f4347b);
            c0500d.a("Failed to get IP Address for Controller.");
            return c0500d;
        }
        C0508a c0508a = new C0508a(strB, 21);
        c0508a.a("sdcard");
        c0508a.b("sdcard");
        uVar.a("Sending Firmware to Controller.");
        this.f4509b = kVar.g();
        C0500d c0500dA = c0508a.a(this.f4509b, uVar);
        if (c0500dA.a() != C0500d.f4346a) {
            return c0500dA;
        }
        if (!uVar.b("Power off your BigStuff3 Gen4")) {
            c0500dA.a(C0500d.f4347b);
            c0500dA.a("Cancelled by user.");
            return c0500dA;
        }
        if (!uVar.b("Connect 12 volts to Pin Y2 on Hdr2 Connector (on the opposite side of the Engine Connector)")) {
            c0500dA.a(C0500d.f4347b);
            c0500dA.a("Cancelled by user.");
            return c0500dA;
        }
        if (uVar.b("Power On the BigStuff3 Gen4")) {
            c0500dA.a(C0500d.f4346a);
            return c0500dA;
        }
        c0500dA.a(C0500d.f4347b);
        c0500dA.a("Cancelled by user.");
        return c0500dA;
    }

    private String b(p pVar) {
        return (String) pVar.a().a(l.f178j);
    }

    @Override // ae.s
    public C0500d a(p pVar, u uVar) {
        C0500d c0500d = new C0500d();
        int i2 = 0;
        if (!uVar.b("Power Off the BigStuff3 Gen4")) {
            c0500d.a(C0500d.f4347b);
            c0500d.a("Cancelled by user.");
            return c0500d;
        }
        if (!uVar.b("Disconnect the 12 volts to Pin Y2 on Hdr2 Connector")) {
            c0500d.a(C0500d.f4347b);
            c0500d.a("Cancelled by user.");
            return c0500d;
        }
        while (uVar.b("Power On the BigStuff3 Gen4")) {
            int i3 = i2;
            i2++;
            if (i3 > 2) {
                c0500d.a(C0500d.f4347b);
                c0500d.a("Cannot connect.");
                return c0500d;
            }
            if (a(pVar)) {
                C0508a c0508a = new C0508a("192.168.4.7", 21);
                c0508a.a("sdcard");
                c0508a.b("sdcard");
                c0508a.a(this.f4509b);
                return c0500d;
            }
        }
        c0500d.a(C0500d.f4347b);
        c0500d.a("Cancelled by user.");
        return c0500d;
    }

    public boolean a(p pVar) {
        try {
            pVar.a().g();
            pVar.a().f();
            byte[] bArrA = pVar.a(f4507c, 12);
            if (bArrA != null && bArrA.length == 12 && bArrA[4] == -1) {
                C.c("Connect! " + C0995c.d(bArrA));
                pVar.a(f4508d, 0);
                return true;
            }
            if (bArrA == null) {
                C.a("null response received on connect");
                return false;
            }
            if (bArrA.length != 12) {
                C.a("wrong len response received on connect: " + bArrA.length);
                return false;
            }
            if (bArrA[4] != -1) {
                C.a("Negative response received on connect");
                return false;
            }
            C.a("Unknown failure on connect");
            return false;
        } catch (C0129l e2) {
            Logger.getLogger(C0510c.class.getName()).log(Level.SEVERE, "Unable to connect", (Throwable) e2);
            return false;
        } catch (v e3) {
            Logger.getLogger(C0510c.class.getName()).log(Level.SEVERE, "Shouldn't get a protocol exception", (Throwable) e3);
            return false;
        } catch (IOException e4) {
            Logger.getLogger(C0510c.class.getName()).log(Level.SEVERE, "Unable to send connect", (Throwable) e4);
            return false;
        }
    }

    private p c(p pVar) {
        try {
            f fVarA = pVar.a();
            fVarA.g();
            f fVar = (f) fVarA.getClass().newInstance();
            for (r rVar : fVarA.l()) {
                try {
                    fVar.a(rVar.c(), fVarA.a(rVar.c()));
                } catch (A.s e2) {
                    Logger.getLogger(C0510c.class.getName()).log(Level.SEVERE, "Failed to set: " + rVar.c(), (Throwable) e2);
                }
            }
            pVar.a(fVar);
            fVar.f();
        } catch (C0129l e3) {
            Logger.getLogger(C0510c.class.getName()).log(Level.SEVERE, "Failed to connect.", (Throwable) e3);
        } catch (IllegalAccessException e4) {
            Logger.getLogger(C0510c.class.getName()).log(Level.SEVERE, "Failed to copy ControllerInterface 2", (Throwable) e4);
        } catch (InstantiationException e5) {
            Logger.getLogger(C0510c.class.getName()).log(Level.SEVERE, "Failed to copy ControllerInterface", (Throwable) e5);
        }
        return pVar;
    }
}
