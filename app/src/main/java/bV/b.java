package bV;

import G.R;
import bH.C;
import bH.C0995c;
import bN.l;
import bN.t;
import bN.u;
import bT.o;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bV/b.class */
public class b implements h {

    /* renamed from: a, reason: collision with root package name */
    public static int f7629a = 0;

    /* renamed from: b, reason: collision with root package name */
    public static int f7630b = 1;

    /* renamed from: c, reason: collision with root package name */
    public static int f7631c = 2;

    @Override // bT.a
    public int a() {
        return 166;
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
        if (b2 == f7629a) {
            try {
                l lVarB = u.a().b();
                lVarB.a(255);
                String strP = oVar.f().P();
                if (strP == null || strP.getBytes() == null) {
                    C.b("No Firmware Infor to give client?");
                } else {
                    lVarB.b(strP.getBytes());
                }
                oVar.a(lVarB);
                return;
            } catch (bN.o e2) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                return;
            }
        }
        if (b2 != f7630b) {
            if (b2 != f7631c) {
                throw new bT.h("PID:USER_CMD_RUNTIME_READ_CONTROL Unknown Action: 0x" + Integer.toHexString(b2).toUpperCase());
            }
            try {
                l lVarB2 = u.a().b();
                lVarB2.a(255);
                lVarB2.b(new byte[]{(byte) oVar.f().O().P()});
                oVar.a(lVarB2);
                return;
            } catch (bN.o e3) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                return;
            }
        }
        try {
            l lVarB3 = u.a().b();
            lVarB3.a(255);
            R rF = oVar.f();
            byte[] bArr = new byte[4];
            C0995c.a(bArr, rF.O().G(0), 0, 2, true);
            C0995c.a(bArr, rF.O().ax(), 2, 2, true);
            lVarB3.b(bArr);
            oVar.a(lVarB3);
        } catch (bN.o e4) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
        }
    }
}
