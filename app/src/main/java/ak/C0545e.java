package ak;

import org.apache.commons.net.ftp.FTP;

/* renamed from: ak.e, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ak/e.class */
public class C0545e extends C0546f {
    public C0545e() {
        super(",", false);
        j(FTP.DEFAULT_CONTROL_ENCODING);
    }

    @Override // ak.C0546f, W.V
    public String i() {
        return W.X.f1994J;
    }

    @Override // ak.C0546f
    protected C0543c a(C0543c c0543c) {
        C0543c c0543cA = super.a(c0543c);
        String strA = c0543cA.a();
        if (strA.startsWith("AP Info:")) {
            this.f4841w = strA;
            c0543cA.a("AP Info");
        }
        return c0543cA;
    }
}
