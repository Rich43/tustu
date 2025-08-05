package J;

import G.C0123f;
import G.cP;
import bH.C;
import bH.C0995c;
import java.io.Serializable;

/* loaded from: TunerStudioMS.jar:J/a.class */
public class a implements cP, Serializable {

    /* renamed from: a, reason: collision with root package name */
    C0123f f1413a = new C0123f();

    /* renamed from: b, reason: collision with root package name */
    int f1414b = 2;

    /* renamed from: c, reason: collision with root package name */
    int f1415c = 1;

    /* renamed from: d, reason: collision with root package name */
    int f1416d = 1;

    /* renamed from: e, reason: collision with root package name */
    i f1417e = new i();

    /* renamed from: f, reason: collision with root package name */
    public static int f1418f = 1;

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v10, types: [int] */
    @Override // G.cP
    public boolean a(byte[] bArr, byte[] bArr2) {
        if (!b(bArr, bArr2)) {
            C.c("Invalid Header data recieved: " + C0995c.d(bArr2));
            return false;
        }
        byte b2 = 0;
        for (int i2 = 0; i2 < bArr2.length - 1; i2++) {
            b2 += bArr2[i2];
        }
        return (b2 & 255) == C0995c.a(bArr2[bArr2.length - 1]);
    }

    public boolean b(byte[] bArr, byte[] bArr2) {
        if (bArr == null || bArr.length <= 0) {
            return true;
        }
        return 256 - C0995c.a(bArr[0]) == C0995c.a(bArr2[0]);
    }

    @Override // G.cP
    public byte[] a(byte[] bArr) {
        if (bArr.length - this.f1414b < 0) {
            C.c("Invalid envelope size. Minimum: " + this.f1414b + ", actual: " + bArr.length);
            return new byte[0];
        }
        byte[] bArrA = this.f1413a.a(bArr.length - this.f1414b);
        System.arraycopy(bArr, 1, bArrA, 0, bArrA.length);
        return bArrA;
    }

    @Override // G.cP
    public boolean b(byte[] bArr) {
        return false;
    }

    @Override // G.cP
    public String c(byte[] bArr) {
        return "";
    }

    @Override // G.cP
    public int d(byte[] bArr) {
        return bArr[0];
    }

    @Override // G.cP
    public int a() {
        return this.f1414b;
    }

    @Override // G.cP
    public int b() {
        return this.f1415c;
    }

    @Override // G.cP
    public String c() {
        return null;
    }

    @Override // G.cP
    public int a(byte[] bArr, int i2) {
        if (i2 >= 0) {
            return i2 + this.f1416d;
        }
        return -1;
    }

    @Override // G.cP
    public h d() {
        return this.f1417e;
    }

    @Override // G.cP
    public boolean a(int i2) {
        return true;
    }

    @Override // G.cP
    public void e() {
    }

    @Override // G.cP
    public boolean b(int i2) {
        return false;
    }

    @Override // G.cP
    public boolean c(int i2) {
        return false;
    }

    @Override // G.cP
    public void a(boolean z2) {
    }
}
