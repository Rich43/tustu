package bH;

/* renamed from: bH.f, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bH/f.class */
public class C0998f {

    /* renamed from: a, reason: collision with root package name */
    int f7043a = 65535;

    /* renamed from: b, reason: collision with root package name */
    final int f7044b = 4129;

    public void a(byte[] bArr) {
        for (byte b2 : bArr) {
            for (int i2 = 0; i2 < 8; i2++) {
                boolean z2 = ((b2 >> (7 - i2)) & 1) == 1;
                boolean z3 = ((this.f7043a >> 15) & 1) == 1;
                this.f7043a <<= 1;
                if (z3 ^ z2) {
                    this.f7043a ^= 4129;
                }
            }
        }
    }

    public void a() {
        this.f7043a = 65535;
    }

    public int b() {
        return this.f7043a & 65535;
    }
}
