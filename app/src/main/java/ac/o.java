package ac;

import G.aF;
import bH.C0995c;
import java.io.OutputStream;
import java.util.HashMap;
import sun.security.x509.X509CertImpl;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: TunerStudioMS.jar:ac/o.class */
public class o implements aF {

    /* renamed from: a, reason: collision with root package name */
    HashMap f4234a;

    /* renamed from: d, reason: collision with root package name */
    long f4237d;

    /* renamed from: e, reason: collision with root package name */
    private static o f4233e = null;

    /* renamed from: b, reason: collision with root package name */
    public static String f4235b = X509CertImpl.SIGNATURE;

    /* renamed from: c, reason: collision with root package name */
    public static String f4236c = "ochLength";

    @Override // G.aF
    public void a(String str, byte[] bArr) {
        p pVar = (p) this.f4234a.get(str);
        OutputStream outputStreamB = pVar.b();
        if (bArr.length != pVar.a()) {
            bH.C.b("Och Length=" + bArr.length + ", expected " + pVar.a() + ":\n\t" + C0995c.d(bArr));
            return;
        }
        byte[] bArr2 = new byte[bArr.length + 4];
        for (int i2 = 0; i2 < bArr.length; i2++) {
            bArr2[i2] = bArr[i2];
        }
        int iA = a();
        bArr2[bArr.length] = (byte) ((iA & (-16777216)) >> 24);
        bArr2[bArr.length + 1] = (byte) ((iA & 16711680) >> 16);
        bArr2[bArr.length + 2] = (byte) ((iA & NormalizerImpl.CC_MASK) >> 8);
        bArr2[bArr.length + 3] = (byte) (iA & 255);
        try {
            System.out.println(C0995c.d(bArr2));
            outputStreamB.write(bArr2);
            outputStreamB.flush();
        } catch (Exception e2) {
            bH.C.c("Error writing to logfile:" + e2.getMessage());
        }
    }

    private int a() {
        return (int) (System.currentTimeMillis() - this.f4237d);
    }
}
