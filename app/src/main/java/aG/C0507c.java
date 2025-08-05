package ag;

import ae.C0500d;
import ae.k;
import ae.m;
import ae.o;
import ae.p;
import ae.s;
import ae.u;
import ae.v;
import af.InterfaceC0504a;
import af.j;
import bH.C0995c;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: ag.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ag/c.class */
public class C0507c implements s, InterfaceC0504a {

    /* renamed from: d, reason: collision with root package name */
    List f4497d = new ArrayList();

    @Override // ae.s
    public List a(k kVar) {
        return this.f4497d;
    }

    @Override // ae.s
    public List b(k kVar) {
        return this.f4497d;
    }

    @Override // ae.s
    public void a(String str, Object obj) {
    }

    @Override // ae.s
    public C0500d a(k kVar, p pVar, u uVar) {
        C0500d c0500d = new C0500d();
        int iA = a(pVar);
        if (iA == 0) {
            c0500d.a(C0500d.f4347b);
            c0500d.a("No MegaSquirt 2 Found.");
            return c0500d;
        }
        if (!a(iA)) {
            c0500d.a(C0500d.f4347b);
            c0500d.a("Warning!! Wrong ECU TYPE found. MS2 expected");
            return c0500d;
        }
        if (!j.b(pVar)) {
            while (uVar.b("Turn off Megasquirt/Microsquirt.")) {
                if (!uVar.b("Install boot jumper shunt onto B/LD on the CPU card, or ground the bootload wire.")) {
                    c0500d.a(C0500d.f4347b);
                    c0500d.a("Cancelled by user.");
                    return c0500d;
                }
                if (!uVar.b("Turn on Megasquirt/Microsquirt.")) {
                    c0500d.a(C0500d.f4347b);
                    c0500d.a("Cancelled by user.");
                    return c0500d;
                }
                if (j.b(pVar)) {
                }
            }
            c0500d.a(C0500d.f4347b);
            c0500d.a("Cancelled by user.");
            return c0500d;
        }
        return j.a(pVar);
    }

    @Override // ae.s
    public C0500d a(p pVar, u uVar) {
        C0500d c0500d = new C0500d();
        c0500d.a(C0500d.f4346a);
        j.e(pVar);
        if (!uVar.b("Turn off Megasquirt/Microsquirt.")) {
            c0500d.a(C0500d.f4347b);
            c0500d.a("Cancelled by user.");
            return c0500d;
        }
        if (!uVar.b("Please remove the boot jumper or disconnect the boot wire from ground.")) {
            c0500d.a(C0500d.f4347b);
            c0500d.a("Cancelled by user.");
            return c0500d;
        }
        if (uVar.b("Turn on Megasquirt/Microsquirt.")) {
            return c0500d;
        }
        c0500d.a(C0500d.f4347b);
        c0500d.a("Cancelled by user.");
        return c0500d;
    }

    public int a(p pVar) {
        int iB = o.a(b(pVar)).b();
        if (iB > 0) {
            return iB;
        }
        m mVarB = o.b(j.f(pVar));
        if (mVarB != null) {
            iB |= mVarB.b();
        }
        return iB;
    }

    private int b(p pVar) throws IOException {
        j.a(pVar);
        try {
            byte[] bArrA = pVar.a(new byte[]{-73}, 6, 200);
            if (bArrA == null || bArrA.length == 0 || bArrA[0] != -36) {
                return -1;
            }
            return C0995c.a(bArrA, 1, 2, true, false);
        } catch (v e2) {
            Logger.getLogger(C0507c.class.getName()).log(Level.SEVERE, "Protocol Error probing for monitor", (Throwable) e2);
            return -1;
        }
    }

    private boolean a(int i2) {
        return (o.f4406v | i2) == i2;
    }
}
