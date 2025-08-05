package G;

/* loaded from: TunerStudioMS.jar:G/cQ.class */
public class cQ implements aN {
    cQ(R r2) {
        String[] strArrH;
        String strY = r2.O().Y();
        if (strY == null || (strArrH = C0126i.h(strY, r2)) == null) {
            return;
        }
        for (String str : strArrH) {
            aR.a().a(r2.c(), str, this);
        }
    }

    @Override // G.aN
    public void a(String str, String str2) {
        R rC = T.a().c(str);
        if (rC != null) {
            C0113cs.a().a(rC);
        }
    }
}
