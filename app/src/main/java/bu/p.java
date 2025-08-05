package bU;

import bH.C0995c;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bU/p.class */
public class p implements bT.a {
    @Override // bT.a
    public int a() {
        return 250;
    }

    @Override // bT.a
    public void a(bT.o oVar, bN.t tVar) throws bT.h {
        if (tVar.a() != a()) {
            throw new bT.h("PID:GET_ID Wrong handler! this handler is for 0x" + Integer.toHexString(a()).toUpperCase());
        }
        byte[] bArrC = tVar.c();
        if (bArrC.length != 1) {
            throw new bT.h("PID:GET_ID Unexpected data size, expected 1 byte, found: " + bArrC.length);
        }
        bN.l lVarB = bN.u.a().b();
        if (bArrC[0] == 0) {
            lVarB.a(255);
            String strI = oVar.f() == null ? "" : oVar.f().i();
            byte[] bArr = new byte[7 + strI.length()];
            bArr[0] = 1;
            byte[] bytes = strI.getBytes();
            byte[] bArrA = C0995c.a(bytes.length, new byte[4], false);
            System.arraycopy(bArrA, 0, bArr, 3, bArrA.length);
            System.arraycopy(bytes, 0, bArr, 7, bytes.length);
            lVarB.b(bArr);
        } else if (bArrC[0] == 1) {
            lVarB.a(255);
            byte[] bArr2 = new byte[7];
            bArr2[0] = 1;
            byte[] bArrA2 = C0995c.a(0, bArr2, false);
            System.arraycopy(bArrA2, 0, bArr2, 3, bArrA2.length);
            lVarB.b(bArr2);
        } else if (bArrC[0] == 2) {
            lVarB.a(255);
            byte[] bArr3 = new byte[7];
            bArr3[0] = 1;
            byte[] bArrA3 = C0995c.a(0, bArr3, false);
            System.arraycopy(bArrA3, 0, bArr3, 3, bArrA3.length);
            lVarB.b(bArr3);
        } else if (bArrC[0] == 3) {
            lVarB.a(255);
            String str = "";
            try {
                str = "http://www.shadowtuner.com/ShadowTuner/FindEcuDefinitionBySignature?signature=" + URLEncoder.encode(oVar.f().i(), "UTF-8");
            } catch (UnsupportedEncodingException e2) {
                Logger.getLogger(p.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            byte[] bArr4 = new byte[7 + str.length()];
            bArr4[0] = 1;
            byte[] bytes2 = str.getBytes();
            byte[] bArrA4 = C0995c.a(bytes2.length, bArr4, false);
            System.arraycopy(bArrA4, 0, bArr4, 3, bArrA4.length);
            System.arraycopy(bytes2, 0, bArr4, 7, bytes2.length);
            lVarB.b(bArr4);
        } else {
            if (bArrC[0] != 4) {
                throw new bT.h("PID:CONNECT Unsupported connect Mode requested: " + ((int) bArrC[0]));
            }
            lVarB.a(255);
            byte[] bArr5 = new byte[7];
            bArr5[0] = 1;
            byte[] bArrA5 = C0995c.a(0, bArr5, false);
            System.arraycopy(bArrA5, 0, bArr5, 3, bArrA5.length);
            lVarB.b(bArr5);
        }
        try {
            oVar.a(lVarB);
        } catch (bN.o e3) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            throw new bT.h(e3.getLocalizedMessage());
        }
    }
}
