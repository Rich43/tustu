package bV;

import bH.C;
import bH.C0995c;
import bN.l;
import bN.t;
import bN.u;
import bT.o;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bV/e.class */
public class e implements h {

    /* renamed from: a, reason: collision with root package name */
    public static int f7638a = 0;

    @Override // bT.a
    public int a() {
        return 165;
    }

    @Override // bT.a
    public void a(o oVar, t tVar) throws bT.h {
        if (tVar.a() != a()) {
            throw new bT.h("PID:USER_CMD_RUNTIME_READ_CONTROL Wrong handler! this handler is for 0x" + Integer.toHexString(a()).toUpperCase());
        }
        byte[] bArrC = tVar.c();
        if (bArrC == null) {
            throw new bT.h("PID:USER_CMD_RUNTIME_READ_CONTROL valid packet data not found.");
        }
        if (bArrC.length < 5) {
            throw new bT.h("PID:USER_CMD_RUNTIME_READ_CONTROL invalid packet data length, must have atleast 5 bytes");
        }
        byte b2 = bArrC[0];
        if (b2 != f7638a) {
            throw new bT.h("PID:USER_CMD_RUNTIME_READ_CONTROL Unknown Action: 0x" + Integer.toHexString(b2).toUpperCase());
        }
        int iA = C0995c.a(bArrC, 1, 4, oVar.d().g(), false);
        C.c("Pause runtime reads for: " + iA + ", packet: " + C0995c.d(bArrC));
        oVar.f().C().d(System.currentTimeMillis() + iA);
        C.c("---   Ignore runtimeReads for " + iA);
        try {
            Thread.sleep(20L);
        } catch (InterruptedException e2) {
            Logger.getLogger(e.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        try {
            l lVarB = u.a().b();
            lVarB.a(255);
            oVar.a(lVarB);
        } catch (bN.o e3) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
        }
    }
}
