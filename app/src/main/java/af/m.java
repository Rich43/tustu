package af;

import ae.C0500d;
import ae.p;
import ae.s;
import ae.u;
import ae.v;
import bH.C;
import bH.C0995c;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:af/m.class */
public class m implements s, InterfaceC0504a {

    /* renamed from: d, reason: collision with root package name */
    List f4492d = new ArrayList();

    /* renamed from: e, reason: collision with root package name */
    n f4493e;

    public m(n nVar) {
        this.f4493e = nVar;
    }

    @Override // ae.s
    public List a(ae.k kVar) {
        return this.f4492d;
    }

    @Override // ae.s
    public List b(ae.k kVar) {
        return this.f4492d;
    }

    @Override // ae.s
    public void a(String str, Object obj) {
    }

    @Override // ae.s
    public C0500d a(ae.k kVar, p pVar, u uVar) throws IOException {
        C0500d c0500d = new C0500d();
        int iA = a(pVar);
        if (iA == 0) {
            c0500d.a(C0500d.f4347b);
            c0500d.a("No MegaSquirt Found.");
            return c0500d;
        }
        if (!a(iA)) {
            c0500d.a(C0500d.f4347b);
            c0500d.a("Warning!! Wrong ECU TYPE found. MS3 expected, found MS2");
            return c0500d;
        }
        if (!j.b(pVar)) {
            try {
                this.f4493e.j();
                this.f4493e.i();
                c0500d = j.a(pVar, j.a(pVar, this.f4493e));
            } catch (v e2) {
                c0500d.a(C0500d.f4347b);
                c0500d.a("Protocol Exception, likely a bug in the firmware loader.");
                return c0500d;
            }
        }
        return j.a(pVar);
    }

    @Override // ae.s
    public C0500d a(p pVar, u uVar) {
        C0500d c0500d = new C0500d();
        c0500d.a(C0500d.f4346a);
        j.e(pVar);
        return c0500d;
    }

    public int a(p pVar) throws IOException {
        int iC = c(pVar);
        return (iC == 0 || iC == 4) ? ae.o.a(d(pVar)).b() : iC | b(pVar);
    }

    public int b(p pVar) {
        byte[] bArr = {77};
        boolean zJ = this.f4493e.j();
        this.f4493e.h();
        try {
            try {
                byte[] bArrA = pVar.a(bArr, 2);
                if (zJ) {
                    this.f4493e.i();
                }
                if (bArrA == null || bArrA.length < 2) {
                    C.c("No Response to M command");
                    return 0;
                }
                int i2 = bArrA[0] + (256 * bArrA[1]);
                int i3 = 0 | i2;
                C.c("M command returned: " + Integer.toHexString(i2));
                return i3;
            } catch (v e2) {
                C.c("protocol error for M command: " + e2.getLocalizedMessage());
                if (zJ) {
                    this.f4493e.i();
                }
                return 0;
            }
        } catch (Throwable th) {
            if (zJ) {
                this.f4493e.i();
            }
            throw th;
        }
    }

    public int c(p pVar) throws IOException {
        byte[] bArrA = null;
        try {
            bArrA = pVar.a(new byte[]{81}, 20, 600);
        } catch (v e2) {
            Logger.getLogger(k.class.getName()).log(Level.SEVERE, "Protocol Error querying signature", (Throwable) e2);
        }
        if (bArrA == null || bArrA.length == 0) {
            return 0;
        }
        if (bArrA[0] == 77 && bArrA[1] == 83 && bArrA[2] == 51) {
            return 2;
        }
        if (bArrA[0] == 77 && bArrA[1] == 83 && bArrA[2] == 50 && bArrA[3] == 69 && bArrA[4] == 120) {
            return 1;
        }
        if (bArrA[0] == 84 && bArrA[1] == 114 && bArrA[2] == 97 && bArrA[3] == 110 && bArrA[4] == 115) {
            return 1;
        }
        if (bArrA[0] == 77 && bArrA[1] == 83 && bArrA[2] == 73 && bArrA[3] == 73) {
            return 8;
        }
        if (bArrA[0] == 71 && bArrA[1] == 80 && bArrA[2] == 73 && bArrA[3] == 79) {
            return 8;
        }
        if (bArrA[0] == 81) {
            return 0;
        }
        if ((bArrA[0] & 240) == 224 && (bArrA[1] & 240) == 0 && bArrA[2] == 62) {
            return 4;
        }
        C.c(" ..Garbled reply.. ");
        return 0;
    }

    private int d(p pVar) throws IOException {
        try {
            byte[] bArrA = pVar.a(new byte[]{-73}, 6, 200);
            if (bArrA == null || bArrA.length == 0 || bArrA[0] != -36) {
                return -1;
            }
            return C0995c.a(bArrA, 1, 2, true, false);
        } catch (v e2) {
            Logger.getLogger(k.class.getName()).log(Level.SEVERE, "Protocol Error probing for monitor", (Throwable) e2);
            return -1;
        }
    }

    private boolean a(int i2) {
        return (ae.o.f4407w | i2) == i2;
    }
}
