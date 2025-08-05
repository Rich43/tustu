package y;

import G.R;
import G.bT;
import V.g;
import ae.m;
import ae.o;
import bH.C;
import bH.C1005m;
import bI.i;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.icepdf.core.util.PdfOps;
import r.C1798a;

/* renamed from: y.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:y/c.class */
public class C1895c implements bT {

    /* renamed from: a, reason: collision with root package name */
    R f14045a;

    public C1895c(R r2) {
        this.f14045a = r2;
    }

    @Override // G.bT
    public void a() {
    }

    @Override // G.bT
    public void a(boolean z2) {
        if (!z2 || this.f14045a.c("inpshare_test1") == null || this.f14045a.c("inpshare_test2") == null) {
            return;
        }
        try {
            int iJ = (int) this.f14045a.c("inpshare_test2").j(this.f14045a.h());
            int iJ2 = (int) this.f14045a.c("inpshare_test1").j(this.f14045a.h());
            C1896d c1896d = new C1896d();
            c1896d.a(iJ);
            c1896d.b(iJ2);
            c1896d.c(this.f14045a.i());
            m mVarA = o.a(iJ);
            if (mVarA != null) {
                c1896d.b(mVarA.a());
            }
            C1798a.a().b(C1798a.f13284r, c1896d.a());
            new e(this).start();
        } catch (g e2) {
            C.a(e2);
        }
    }

    public static void b() {
        String strC = C1798a.a().c(C1798a.f13285s, "");
        String strC2 = C1798a.a().c(C1798a.f13284r, "");
        if (strC2 == null || strC2.equals("") || strC2.equals(strC) || !C1005m.a()) {
            return;
        }
        try {
            C1896d c1896dA = C1896d.a(strC2);
            C1005m.a("https://www.efianalytics.com/register/HwLog?payload=" + a(c1896dA));
            C1798a.a().b(C1798a.f13285s, c1896dA.a());
        } catch (IOException e2) {
            C.a(e2);
        }
    }

    public static String a(C1896d c1896d) throws IOException {
        Properties properties = new Properties();
        properties.setProperty("iId", C1798a.a().c(C1798a.f13358aN, ""));
        properties.setProperty("mId", c1896d.b() + "");
        properties.setProperty(PdfOps.s_TOKEN, c1896d.c() + "");
        properties.setProperty("hw", c1896d.d());
        properties.setProperty("sig", c1896d.e());
        properties.setProperty("rk", C1798a.a().c(C1798a.cF, ""));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        properties.store(byteArrayOutputStream, "");
        return bI.a.a(i.a(byteArrayOutputStream.toByteArray()), 16);
    }
}
