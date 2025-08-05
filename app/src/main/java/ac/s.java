package ac;

/* loaded from: TunerStudioMS.jar:ac/s.class */
public abstract class s {

    /* renamed from: a, reason: collision with root package name */
    public static byte f4250a = 0;

    /* renamed from: b, reason: collision with root package name */
    public static byte f4251b = 1;

    /* renamed from: c, reason: collision with root package name */
    public static byte f4252c = 2;

    /* renamed from: d, reason: collision with root package name */
    public static byte f4253d = 3;

    /* renamed from: e, reason: collision with root package name */
    public static byte f4254e = 4;

    /* renamed from: f, reason: collision with root package name */
    public static byte f4255f = 5;

    /* renamed from: g, reason: collision with root package name */
    public static byte f4256g = 6;

    /* renamed from: h, reason: collision with root package name */
    public static byte f4257h = 7;

    /* renamed from: i, reason: collision with root package name */
    protected byte[] f4258i = new byte[15];

    /* renamed from: j, reason: collision with root package name */
    private byte[] f4259j = new byte[6];

    /* renamed from: k, reason: collision with root package name */
    private byte f4260k = f4255f;

    /* renamed from: l, reason: collision with root package name */
    private byte[] f4261l = new byte[4];

    /* renamed from: m, reason: collision with root package name */
    private byte[] f4262m = new byte[4];

    /* renamed from: n, reason: collision with root package name */
    private byte f4263n = 0;

    /* renamed from: o, reason: collision with root package name */
    private float f4264o = 1.0f;

    /* renamed from: p, reason: collision with root package name */
    private float f4265p = 0.0f;

    public void a(String str) {
        a(str, this.f4258i);
    }

    public byte[] d() {
        return this.f4258i;
    }

    public void b(String str) {
        a(str, this.f4259j);
    }

    public byte[] e() {
        return this.f4259j;
    }

    public void a(byte b2) {
        this.f4260k = b2;
    }

    public byte f() {
        return this.f4260k;
    }

    public void a(float f2) {
        this.f4264o = f2;
        this.f4261l = a(f2, this.f4261l);
    }

    public double g() {
        return this.f4264o;
    }

    public void b(float f2) {
        this.f4265p = f2;
        this.f4262m = a(f2, this.f4262m);
    }

    public double h() {
        return this.f4265p;
    }

    public void b(byte b2) {
        this.f4263n = b2;
    }

    public byte i() {
        return this.f4263n;
    }

    private byte[] a(byte[] bArr) {
        for (int i2 = 0; i2 < bArr.length; i2++) {
            bArr[i2] = 0;
        }
        return bArr;
    }

    private byte[] a(String str, byte[] bArr) {
        byte[] bArrA = a(bArr);
        for (int i2 = 0; i2 < bArrA.length && i2 < str.length(); i2++) {
            bArrA[i2] = (byte) str.charAt(i2);
        }
        return bArrA;
    }

    private byte[] a(float f2, byte[] bArr) {
        return a(Float.floatToIntBits(f2), bArr, true);
    }

    public static byte[] a(int i2, byte[] bArr, boolean z2) {
        boolean z3 = false;
        if (i2 < 0) {
            i2 = (i2 ^ (-1)) + 0;
            z3 = true;
        }
        for (int i3 = 0; i3 < bArr.length; i3++) {
            if (z2) {
                bArr[(bArr.length - i3) - 1] = (byte) (i2 & (2147483392 ^ (-1)));
                if (z3) {
                    bArr[(bArr.length - i3) - 1] = (byte) (255 - bArr[(bArr.length - i3) - 1]);
                }
            } else {
                bArr[i3] = (byte) (i2 & (2147483392 ^ (-1)));
                if (z3) {
                    bArr[i3] = (byte) (255 - bArr[i3]);
                }
            }
            i2 >>= 8;
        }
        return bArr;
    }
}
