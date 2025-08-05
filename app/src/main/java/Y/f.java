package y;

import G.InterfaceC0139v;
import G.R;
import G.T;
import G.aT;
import G.bN;
import G.da;
import bH.C;
import bH.C0995c;
import bI.g;
import com.sun.imageio.plugins.jpeg.JPEG;
import java.io.IOException;
import java.util.Properties;
import org.apache.commons.net.telnet.TelnetCommand;
import r.q;
import sun.util.locale.LanguageTag;

/* loaded from: TunerStudioMS.jar:y/f.class */
public class f implements bN {

    /* renamed from: k, reason: collision with root package name */
    aT f14051k;

    /* renamed from: l, reason: collision with root package name */
    String f14052l;

    /* renamed from: m, reason: collision with root package name */
    boolean f14053m = false;

    /* renamed from: n, reason: collision with root package name */
    boolean f14054n = false;

    /* renamed from: o, reason: collision with root package name */
    long f14055o = 0;

    /* renamed from: p, reason: collision with root package name */
    boolean f14056p = false;

    /* renamed from: q, reason: collision with root package name */
    int f14057q = 983;

    /* renamed from: r, reason: collision with root package name */
    int f14058r = 2;

    /* renamed from: a, reason: collision with root package name */
    private int f14059a = 1;

    /* renamed from: b, reason: collision with root package name */
    private int f14060b = 28;

    /* renamed from: c, reason: collision with root package name */
    private int f14061c = 172;

    /* renamed from: d, reason: collision with root package name */
    private byte f14062d = 12;

    /* renamed from: e, reason: collision with root package name */
    private byte[] f14063e = {-69, 9, 106, -115, 102, -73, -43, 92, -5};

    /* renamed from: f, reason: collision with root package name */
    private byte[] f14064f = {-95, -77, -12, 5, -114, -74, 45, 36, 90};

    /* renamed from: g, reason: collision with root package name */
    private da f14065g = new da();

    /* renamed from: s, reason: collision with root package name */
    int[] f14066s = {152, 52, 220, 241, 65, 233, 7, 66, 233, 149, 87, 234, 243, 64, 234, TelnetCommand.GA, 61, 223, 239, 62, 223, 245, 67, JPEG.APP8, 150, 60, JPEG.APP8, 252, 68, JPEG.APP8, 254, 75, JPEG.APP8, 1, 70, 223, 154, 69, 223, 4, 72, 234, 6, 67, 222, 252, 62, 192, 14, 111, 146, 107, 188, 218, 97, 112, 217, 44, 98, 212, 43, 55, 211, 43};

    /* renamed from: t, reason: collision with root package name */
    int[] f14067t = {40, 209, 233, 43, 55, 27, 233, 228, 60, 245, 78, 39, 117, 242, 90, 118, 245, 136, 253, 6, 242, 96, 31, 160, 242, 131, 0, 40, 61, 115, 124, 132, 99, 61, JPEG.APP8, 130, 71, 160, 207, JPEG.APP8, 250, 254, 71, 227, 122, 104, 101, 192, 14, 111, 146, 107, 188, 218, 97};

    /* renamed from: u, reason: collision with root package name */
    int[] f14068u = {40, 209, 233, 43, 55, 27, 233, 228, 60, 245, 78, 39, 117, 242, 90, 118, 245, 136, 253, 6, 242, 96, 31, 160, 242, 131};

    public f(String str, aT aTVar) {
        this.f14051k = null;
        this.f14052l = "";
        this.f14051k = aTVar;
        this.f14052l = str;
    }

    @Override // G.bN
    public int a(InterfaceC0139v interfaceC0139v) {
        if (0 != 0 || (this.f14056p && System.currentTimeMillis() - this.f14055o < 1800000)) {
            return e();
        }
        byte[] bArrA = a(new byte[71], (byte) 0);
        byte bA2 = interfaceC0139v.a();
        bArrA[0] = 119;
        bArrA[1] = bA2;
        bArrA[2] = 16;
        bArrA[6] = 64;
        boolean z2 = T.a().c(this.f14052l).O().D() == null;
        int i2 = z2 ? 300 : 200;
        R rC = T.a().c(this.f14052l);
        int iK = rC.O().k();
        if (z2) {
            rC.O().d(0);
        }
        int i3 = z2 ? 1 : 0;
        try {
            try {
                try {
                    int[] iArrA = interfaceC0139v.a(bArrA, i2, i3);
                    if (iArrA.length == 1) {
                        if (iArrA[0] != 126) {
                        }
                    }
                } catch (V.b e2) {
                    if (this.f14054n) {
                        e2.printStackTrace();
                    }
                    C.c(q.a(new Properties(), C.a.f221b));
                    rC.O().d(iK);
                    return 0;
                } catch (IOException e3) {
                    C.c("IOException received");
                    if (this.f14054n) {
                        e3.printStackTrace();
                    }
                    int iE = e();
                    rC.O().d(iK);
                    return iE;
                } catch (Exception e4) {
                    C.c(q.a(new Properties(), C.a.f222c));
                    rC.O().d(iK);
                    return 0;
                }
            } catch (V.b e5) {
            }
            byte[] bArr = {114, bA2, 16, 0, 0, 0, 64};
            a(bArr, (byte) 0);
            int[] iArrA2 = interfaceC0139v.a(bArr, i2, 64);
            if (iArrA2[0] == 114 && iArrA2[1] == 112 && iArrA2[2] == 109 && iArrA2[3] == 33 && iArrA2[4] == 61 && iArrA2[5] == 48) {
                rC.O().d(iK);
                return 0;
            }
            byte[] bArrA2 = g.a(a(iArrA2));
            byte[] bArrA3 = a(64);
            bArrA3[0] = 40;
            System.arraycopy(bArrA2, 0, bArrA3, 21, bArrA2.length);
            System.arraycopy(bArrA3, 0, bArrA, 7, bArrA3.length);
            try {
                int[] iArrA3 = interfaceC0139v.a(bArrA, i2, i3);
                if (iArrA3.length == 1) {
                    if (iArrA3[0] != 126) {
                    }
                }
            } catch (V.b e6) {
            }
            int[] iArrA4 = interfaceC0139v.a(bArr, i2, 64);
            int[] iArr = new int[16];
            System.arraycopy(iArrA4, 7, iArr, 0, 16);
            byte[] bArrA4 = C0995c.a(iArr);
            byte[] bArrA5 = g.a(a(b(bArrA3)));
            for (int i4 = 0; i4 < bArrA4.length; i4++) {
                if (bArrA4[i4] != bArrA5[i4]) {
                    rC.O().d(iK);
                    return 0;
                }
            }
            C.c(LanguageTag.SEP + C.a.f220a + LanguageTag.SEP);
            this.f14055o = System.currentTimeMillis();
            this.f14056p = true;
            int iE2 = e();
            rC.O().d(iK);
            return iE2;
        } catch (Throwable th) {
            rC.O().d(iK);
            throw th;
        }
    }

    private byte[] a(byte[] bArr, byte b2) {
        for (int i2 = 0; i2 < bArr.length; i2++) {
            bArr[i2] = b2;
        }
        return bArr;
    }

    private byte[] a(int[] iArr) {
        d();
        iArr[c()] = this.f14064f[a()] & 255;
        iArr[c()] = this.f14064f[a()] & 255;
        iArr[c()] = this.f14064f[a()] & 255;
        iArr[c()] = this.f14064f[a()] & 255;
        iArr[c()] = this.f14064f[a()] & 255;
        iArr[c()] = this.f14064f[a()] & 255;
        iArr[c()] = this.f14064f[a()] & 255;
        iArr[c()] = this.f14064f[a()] & 255;
        byte[] bArr = new byte[55];
        for (int i2 = 0; i2 < bArr.length; i2++) {
            bArr[i2] = (byte) iArr[i2];
        }
        return bArr;
    }

    private int c() {
        try {
            return this.f14060b;
        } finally {
            this.f14060b += this.f14059a;
        }
    }

    public int a() {
        try {
            return this.f14061c;
        } finally {
            this.f14061c += this.f14059a;
        }
    }

    private void d() {
        this.f14060b = (this.f14058r * 22) + 3;
        this.f14061c = this.f14060b / 100;
        this.f14062d = (byte) (this.f14058r + 3);
        for (int i2 = 0; i2 < this.f14063e.length; i2++) {
            this.f14064f[i2] = (byte) (this.f14063e[i2] + this.f14062d);
        }
    }

    private byte[] a(byte[] bArr) {
        byte[] bArr2 = null;
        int i2 = 0;
        while (true) {
            if (i2 >= bArr.length) {
                break;
            }
            if (bArr[i2] == 0) {
                bArr2 = new byte[i2];
                break;
            }
            i2++;
        }
        if (bArr2 == null) {
            return bArr;
        }
        for (int i3 = 0; i3 < bArr2.length; i3++) {
            bArr2[i3] = bArr[i3];
        }
        return bArr2;
    }

    private byte[] b(byte[] bArr) {
        d();
        System.arraycopy(this.f14064f, 0, bArr, this.f14060b, this.f14064f.length);
        byte[] bArr2 = new byte[55];
        for (int i2 = 0; i2 < bArr2.length; i2++) {
            bArr2[i2] = bArr[i2];
        }
        return bArr2;
    }

    private byte[] a(int i2) {
        byte[] bArr = new byte[i2];
        for (int i3 = 0; i3 < bArr.length; i3++) {
            bArr[i3] = (byte) ((254.0d * Math.random()) + 1.0d);
        }
        return bArr;
    }

    @Override // G.bN
    public boolean b() {
        return this.f14053m;
    }

    private int e() {
        return this.f14057q * this.f14058r;
    }
}
