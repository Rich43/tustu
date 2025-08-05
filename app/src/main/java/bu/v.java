package bU;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.telnet.TelnetCommand;

/* loaded from: TunerStudioMS.jar:bU/v.class */
public class v implements bT.a {
    @Override // bT.a
    public int a() {
        return TelnetCommand.GA;
    }

    @Override // bT.a
    public void a(bT.o oVar, bN.t tVar) throws bT.h {
        if (tVar.a() != a()) {
            throw new bT.h("PID:STD_SET_REQUEST Wrong handler! this handler is for 0x" + Integer.toHexString(a()).toUpperCase());
        }
        byte[] bArrC = tVar.c();
        if (bArrC == null) {
            throw new bT.h("PID:STD_SET_REQUEST valid packet data not found.");
        }
        if (bArrC.length != 3) {
            throw new bT.h("PID:STD_SET_REQUEST invalid packet data length, expected 4 bytes, found: " + (bArrC.length + 1));
        }
        if ((bArrC[0] & 1) != 0) {
            oVar.f().I();
        }
        bN.l lVarB = bN.u.a().b();
        lVarB.a(255);
        lVarB.b(new byte[0]);
        try {
            oVar.a(lVarB);
        } catch (bN.o e2) {
            Logger.getLogger(u.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            throw new bT.h(e2.getLocalizedMessage());
        }
    }
}
