package G;

import bH.C0995c;
import javafx.fxml.FXMLLoader;

/* loaded from: TunerStudioMS.jar:G/bY.class */
public class bY {

    /* renamed from: b, reason: collision with root package name */
    private String f876b = null;

    /* renamed from: c, reason: collision with root package name */
    private byte[] f877c = null;

    /* renamed from: d, reason: collision with root package name */
    private int f878d = 0;

    /* renamed from: e, reason: collision with root package name */
    private int f879e = 1;

    /* renamed from: f, reason: collision with root package name */
    private String f880f = null;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bX f881a;

    public bY(bX bXVar) {
        this.f881a = bXVar;
    }

    public String a() {
        return this.f876b;
    }

    public void a(String str) {
        this.f876b = str;
    }

    public byte[] b() {
        return this.f877c;
    }

    public void a(byte[] bArr) {
        this.f877c = bArr;
    }

    public String c() {
        return this.f880f;
    }

    public void b(String str) {
        this.f880f = str;
    }

    public byte[] a(int[] iArr) {
        byte[] bArr = null;
        if (iArr != null) {
            bArr = new byte[iArr.length];
            for (int i2 = 0; i2 < bArr.length; i2++) {
                bArr[i2] = (byte) iArr[i2];
            }
        }
        int length = b().length + e();
        if (bArr != null) {
            length += bArr.length;
        }
        byte[] bArr2 = new byte[length];
        System.arraycopy(b(), 0, bArr2, 0, b().length);
        int length2 = 0 + b().length;
        if (e() > 0) {
            byte[] bArrA = C0995c.a(d(), new byte[e()], this.f881a.b());
            System.arraycopy(bArrA, 0, bArr2, length2, bArrA.length);
            length2 += bArrA.length;
        }
        if (bArr != null) {
            System.arraycopy(bArr, 0, bArr2, length2, bArr.length);
        }
        return bArr2;
    }

    public int d() {
        return this.f878d;
    }

    public void a(int i2) {
        this.f878d = i2;
    }

    public int e() {
        return this.f879e;
    }

    public void b(int i2) {
        this.f879e = i2;
    }

    public boolean f() {
        return this.f876b.indexOf(FXMLLoader.EXPRESSION_PREFIX) != -1;
    }
}
