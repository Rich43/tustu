package ak;

import org.icepdf.core.util.PdfOps;

/* renamed from: ak.s, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ak/s.class */
public class C0559s extends C0546f {
    public C0559s() {
        super("\t", false);
    }

    @Override // ak.C0546f
    protected boolean c(String str, String str2) {
        try {
            if (str.startsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
                str = str.substring(1);
            }
            Double.parseDouble(bH.W.b(str.substring(0, str.indexOf(str2)), ",", "."));
            return true;
        } catch (Exception e2) {
            return false;
        }
    }

    @Override // ak.C0546f, W.V
    public String i() {
        return W.X.f1977s;
    }
}
