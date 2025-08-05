package aZ;

import bH.C0995c;

/* loaded from: TunerStudioMS.jar:aZ/i.class */
public class i {

    /* renamed from: a, reason: collision with root package name */
    public static byte f4128a = 3;

    /* renamed from: b, reason: collision with root package name */
    public static byte f4129b = 4;

    /* renamed from: c, reason: collision with root package name */
    public static byte f4130c = 5;

    /* renamed from: d, reason: collision with root package name */
    private String f4123d = "";

    /* renamed from: e, reason: collision with root package name */
    private String f4124e = "";

    /* renamed from: f, reason: collision with root package name */
    private double f4125f = 1.0d;

    /* renamed from: g, reason: collision with root package name */
    private float f4126g = 0.0f;

    /* renamed from: h, reason: collision with root package name */
    private int f4127h = 0;

    /* renamed from: i, reason: collision with root package name */
    private int f4131i = f4129b;

    public void a(int[] iArr) {
        int[] iArr2 = new int[15];
        System.arraycopy(iArr, 0, iArr2, 0, iArr2.length);
        int i2 = 0 + 15;
        this.f4123d = b(iArr2);
        int[] iArr3 = new int[6];
        System.arraycopy(iArr, i2, iArr3, 0, iArr3.length);
        int i3 = i2 + 6;
        this.f4124e = b(iArr3);
        int i4 = i3 + 1;
        this.f4131i = iArr[i3];
        int[] iArr4 = new int[4];
        System.arraycopy(iArr, i4, iArr4, 0, iArr4.length);
        int i5 = i4 + 4;
        byte[] bArrA = C0995c.a(iArr4);
        this.f4125f = Float.intBitsToFloat((bArrA[0] << 24) | ((bArrA[1] & 255) << 16) | ((bArrA[2] & 255) << 8) | (bArrA[3] & 255));
        int[] iArr5 = new int[4];
        System.arraycopy(iArr, i5, iArr5, 0, iArr5.length);
        byte[] bArrA2 = C0995c.a(iArr5);
        this.f4126g = Float.intBitsToFloat((bArrA2[0] << 24) | ((bArrA2[1] & 255) << 16) | ((bArrA2[2] & 255) << 8) | (bArrA2[3] & 255));
        this.f4127h = iArr[i5 + 4];
    }

    public double a(byte[] bArr) {
        if (bArr == null || !(bArr.length == 4 || bArr.length == 8)) {
            throw new h("Expected 4 bytes, found: " + (bArr == null ? "0" : Integer.valueOf(bArr.length)));
        }
        return this.f4131i == f4130c ? ((((255 & bArr[0]) << 56) | ((255 & bArr[1]) << 48) | ((255 & bArr[2]) << 40) | ((255 & bArr[3]) << 32) | ((255 & bArr[4]) << 24) | ((255 & bArr[5]) << 16) | ((255 & bArr[6]) << 8) | (255 & bArr[7])) + this.f4126g) * this.f4125f : (((bArr[0] << 24) | ((bArr[1] & 255) << 16) | ((bArr[2] & 255) << 8) | (bArr[3] & 255)) + this.f4126g) * this.f4125f;
    }

    private String b(int[] iArr) {
        return new String(C0995c.c(C0995c.a(iArr)));
    }

    public String a() {
        return this.f4123d;
    }

    public String b() {
        return this.f4124e;
    }

    public int c() {
        return this.f4127h;
    }

    public int d() {
        return this.f4131i;
    }
}
