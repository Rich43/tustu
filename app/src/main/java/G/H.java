package G;

import bH.C0995c;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;

/* loaded from: TunerStudioMS.jar:G/H.class */
public class H implements S, aN, Serializable {

    /* renamed from: b, reason: collision with root package name */
    private String f396b = null;

    /* renamed from: c, reason: collision with root package name */
    private byte[] f397c = null;

    /* renamed from: d, reason: collision with root package name */
    private String f398d = null;

    /* renamed from: e, reason: collision with root package name */
    private int f399e = 0;

    /* renamed from: f, reason: collision with root package name */
    private int f400f = 0;

    /* renamed from: g, reason: collision with root package name */
    private int f401g;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ F f402a;

    public H(F f2, int i2) {
        this.f402a = f2;
        this.f401g = 0;
        this.f401g = i2;
    }

    public H a() {
        H h2 = new H(this.f402a, this.f401g);
        h2.a(b());
        h2.a(d());
        h2.b(e());
        h2.a(f());
        h2.b(g());
        return h2;
    }

    public String b() {
        return this.f396b;
    }

    public void a(String str) {
        this.f396b = str;
    }

    public boolean c() {
        return this.f396b.indexOf(FXMLLoader.EXPRESSION_PREFIX) != -1;
    }

    public byte[] d() {
        return this.f397c;
    }

    public void a(byte[] bArr) {
        this.f397c = bArr;
    }

    public String e() {
        return this.f398d;
    }

    public void b(String str) {
        this.f398d = str;
    }

    public int f() {
        return this.f399e;
    }

    public void a(int i2) {
        this.f399e = i2;
    }

    public int g() {
        return this.f400f;
    }

    public void b(int i2) {
        this.f400f = i2;
    }

    public byte[] a(int i2, int i3, int[] iArr) {
        byte[] bArr = null;
        if (iArr != null) {
            bArr = new byte[iArr.length];
            for (int i4 = 0; i4 < bArr.length; i4++) {
                bArr[i4] = (byte) iArr[i4];
            }
        }
        int length = d().length + this.f399e + this.f400f;
        if (bArr != null) {
            length += bArr.length;
        }
        byte[] bArr2 = new byte[length];
        System.arraycopy(d(), 0, bArr2, 0, d().length);
        int length2 = 0 + d().length;
        boolean zE = this.f402a.ao() ? true : this.f402a.e();
        if (this.f399e > 0) {
            byte[] bArr3 = new byte[f()];
            byte[] bArrA = this.f401g == 0 ? C0995c.a(i2, bArr3, zE) : C0995c.a(this.f401g + i2, bArr3, zE);
            System.arraycopy(bArrA, 0, bArr2, length2, bArrA.length);
            length2 += bArrA.length;
        }
        if (this.f400f > 0) {
            byte[] bArr4 = new byte[g()];
            byte[] bArrA2 = this.f401g == 0 ? C0995c.a(i3, bArr4, zE) : C0995c.a(((this.f401g + i2) + i3) - 1, bArr4, zE);
            System.arraycopy(bArrA2, 0, bArr2, length2, bArrA2.length);
            length2 += bArrA2.length;
        }
        if (bArr != null) {
            System.arraycopy(bArr, 0, bArr2, length2, bArr.length);
        }
        return bArr2;
    }

    @Override // G.aN
    public void a(String str, String str2) {
        try {
            this.f402a.a(this);
        } catch (V.g e2) {
            Logger.getLogger(F.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    @Override // G.S
    public void a(R r2) {
    }

    @Override // G.S
    public void b(R r2) {
    }

    @Override // G.S
    public void c(R r2) {
        if (r2.c().equals(this.f402a.f357ah)) {
            try {
                this.f402a.e(this);
                this.f402a.a(this);
            } catch (V.g e2) {
                Logger.getLogger(F.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
    }
}
