package bP;

import G.R;
import G.T;
import G.cG;
import bH.C;
import bN.l;
import bN.u;
import bT.o;
import bV.g;

/* loaded from: TunerStudioMS.jar:bP/b.class */
public class b implements cG {

    /* renamed from: a, reason: collision with root package name */
    o f7382a;

    public b(o oVar) {
        this.f7382a = oVar;
    }

    @Override // G.cG
    public void a(String str, String str2) {
        try {
            a(T.a().c(str), str2);
        } catch (bN.o e2) {
            C.c("Failed to send Slave SERV Refresh Pc var packet: " + e2.getLocalizedMessage());
        }
    }

    private void a(R r2, String str) {
        l lVarB = u.a().b();
        lVarB.a(252);
        byte[] bytes = str.getBytes();
        byte[] bArr = new byte[bytes.length + 2];
        bArr[0] = -29;
        bArr[1] = (byte) g.f7642b;
        System.arraycopy(bytes, 0, bArr, 2, bytes.length);
        lVarB.b(bArr);
        this.f7382a.a(lVarB);
    }
}
