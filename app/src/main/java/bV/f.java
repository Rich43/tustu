package bV;

import G.df;
import bN.l;
import bN.t;
import bN.u;
import bT.o;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bV/f.class */
public class f implements h {

    /* renamed from: a, reason: collision with root package name */
    public static int f7639a = 0;

    /* renamed from: b, reason: collision with root package name */
    public static int f7640b = 1;

    @Override // bT.a
    public int a() {
        return 167;
    }

    @Override // bT.a
    public void a(o oVar, t tVar) throws bT.h {
        if (tVar.a() != a()) {
            throw new bT.h("PID:USER_CMD_TURBO_BAUD Wrong handler! this handler is for 0x" + Integer.toHexString(a()).toUpperCase());
        }
        byte[] bArrC = tVar.c();
        if (bArrC == null) {
            throw new bT.h("PID:USER_CMD_TURBO_BAUD valid packet data not found.");
        }
        if (bArrC.length < 1) {
            throw new bT.h("PID:USER_CMD_TURBO_BAUD invalid packet data length, must have atleast 1 byte");
        }
        byte b2 = bArrC[0];
        if (b2 == f7639a) {
            try {
                df.a(oVar.f());
            } catch (V.g e2) {
                Logger.getLogger(f.class.getName()).log(Level.WARNING, "Failed to activate Turbo baud", (Throwable) e2);
            }
            try {
                l lVarB = u.a().b();
                lVarB.a(255);
                oVar.a(lVarB);
                return;
            } catch (bN.o e3) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                return;
            }
        }
        if (b2 != f7640b) {
            throw new bT.h("PID:USER_CMD_TURBO_BAUD Unknown Action: 0x" + Integer.toHexString(b2).toUpperCase());
        }
        try {
            df.b(oVar.f());
        } catch (V.g e4) {
            Logger.getLogger(f.class.getName()).log(Level.WARNING, "Failed to deactivate Turbo baud", (Throwable) e4);
        }
        try {
            l lVarB2 = u.a().b();
            lVarB2.a(255);
            oVar.a(lVarB2);
        } catch (bN.o e5) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
        }
    }
}
