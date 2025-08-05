package ac;

import bH.C0995c;
import bH.W;
import sun.util.locale.LanguageTag;

/* loaded from: TunerStudioMS.jar:ac/w.class */
public abstract class w {

    /* renamed from: a, reason: collision with root package name */
    private byte f4311a = f4305h;

    /* renamed from: n, reason: collision with root package name */
    protected final byte[] f4312n = new byte[34];

    /* renamed from: o, reason: collision with root package name */
    protected final byte[] f4313o = new byte[34];

    /* renamed from: p, reason: collision with root package name */
    protected final byte[] f4314p = new byte[10];

    /* renamed from: q, reason: collision with root package name */
    protected final byte[] f4315q = new byte[1];

    /* renamed from: c, reason: collision with root package name */
    public static byte f4300c = 0;

    /* renamed from: d, reason: collision with root package name */
    public static byte f4301d = 1;

    /* renamed from: e, reason: collision with root package name */
    public static byte f4302e = 2;

    /* renamed from: f, reason: collision with root package name */
    public static byte f4303f = 3;

    /* renamed from: g, reason: collision with root package name */
    public static byte f4304g = 4;

    /* renamed from: h, reason: collision with root package name */
    public static byte f4305h = 5;

    /* renamed from: i, reason: collision with root package name */
    public static byte f4306i = 6;

    /* renamed from: j, reason: collision with root package name */
    public static byte f4307j = 7;

    /* renamed from: k, reason: collision with root package name */
    public static byte f4308k = 16;

    /* renamed from: l, reason: collision with root package name */
    public static byte f4309l = 17;

    /* renamed from: m, reason: collision with root package name */
    public static byte f4310m = 18;

    /* renamed from: r, reason: collision with root package name */
    protected static final byte[] f4316r = {0};

    public void b(byte b2) {
        this.f4311a = b2;
    }

    public byte g() {
        return this.f4311a;
    }

    public void b(String str) {
        a(this.f4312n, W.b(W.b(W.b(str, LanguageTag.SEP, " "), "(", "_"), ")", ""));
    }

    public void c(String str) {
        a(this.f4313o, str);
    }

    public String h() {
        return W.k(new String(this.f4312n));
    }

    public void d(String str) {
        a(this.f4314p, str);
    }

    public String i() {
        return W.k(new String(this.f4314p));
    }

    public void e(int i2) {
        this.f4315q[0] = (byte) (255 & i2);
    }

    public byte j() {
        return this.f4315q[0];
    }

    public static byte[] a(byte[] bArr, String str) {
        if (str == null) {
            C0995c.a(bArr, (byte) 0);
            return bArr;
        }
        try {
            byte[] bytes = str.getBytes();
            if (bytes.length > bArr.length) {
                bH.C.c("String too large for buffer! Will truncate. buffer size: " + bArr.length + ", string len: " + bytes.length + ", string val: " + str);
            }
            int length = bytes.length <= bArr.length ? bytes.length : bArr.length;
            System.arraycopy(bytes, 0, bArr, 0, length);
            if (length < bArr.length) {
                System.arraycopy(f4316r, 0, bArr, length, f4316r.length);
            }
        } catch (Exception e2) {
            bH.C.c("Boom 53242");
        }
        return bArr;
    }

    public abstract byte[] a(int i2);

    public abstract int d(int i2);

    public int k() {
        if (this.f4311a == f4300c || this.f4311a == f4301d || this.f4311a == f4308k) {
            return 1;
        }
        if (this.f4311a == f4302e || this.f4311a == f4303f || this.f4311a == f4309l) {
            return 2;
        }
        if (this.f4311a == f4304g || this.f4311a == f4305h || this.f4311a == f4310m || this.f4311a == f4307j) {
            return 4;
        }
        return this.f4311a == f4306i ? 8 : 0;
    }

    public abstract byte[] a(double d2);

    public abstract double a(byte[][] bArr);

    protected byte[] a(float f2, byte[] bArr) {
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

    public static byte[] f(int i2) {
        return new byte[]{(byte) (i2 >>> 24), (byte) (i2 >>> 16), (byte) (i2 >>> 8), (byte) i2};
    }

    public static byte[] a(long j2) {
        return new byte[]{(byte) (j2 >>> 56), (byte) (j2 >>> 48), (byte) (j2 >>> 40), (byte) (j2 >>> 32), (byte) (j2 >>> 24), (byte) (j2 >>> 16), (byte) (j2 >>> 8), (byte) j2};
    }

    public static byte[] a(float f2) {
        return f(Float.floatToIntBits(f2));
    }

    public boolean l() {
        return g() == f4301d || g() == f4303f || g() == f4305h || g() == f4306i || g() == f4307j;
    }
}
