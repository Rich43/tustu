package ak;

import org.icepdf.core.util.PdfOps;

/* loaded from: TunerStudioMS.jar:ak/av.class */
public class av extends C0546f {

    /* renamed from: a, reason: collision with root package name */
    float f4800a;

    public av() {
        super(",", false);
        this.f4800a = -1.0f;
    }

    @Override // ak.C0546f
    protected String l() throws V.f {
        String strL = super.l();
        if (strL.startsWith(",\"")) {
            strL = "Time" + strL;
        }
        return bH.W.b(strL, PdfOps.DOUBLE_QUOTE__TOKEN, "");
    }

    @Override // ak.C0546f
    protected int b(String str) {
        return 0;
    }

    @Override // ak.C0546f, W.V
    public String i() {
        return W.X.f1979u;
    }
}
