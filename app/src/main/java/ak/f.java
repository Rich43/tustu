package aK;

import java.util.HashMap;

/* loaded from: TunerStudioMS.jar:aK/f.class */
public class f {

    /* renamed from: a, reason: collision with root package name */
    private String f2576a;

    /* renamed from: b, reason: collision with root package name */
    private long f2577b = 0;

    /* renamed from: c, reason: collision with root package name */
    private double f2578c = 0.0d;

    /* renamed from: d, reason: collision with root package name */
    private double f2579d = 0.0d;

    /* renamed from: e, reason: collision with root package name */
    private boolean f2580e = false;

    /* renamed from: f, reason: collision with root package name */
    private double f2581f = 0.0d;

    /* renamed from: g, reason: collision with root package name */
    private boolean f2582g = false;

    /* renamed from: h, reason: collision with root package name */
    private float f2583h = 0.0f;

    /* renamed from: i, reason: collision with root package name */
    private boolean f2584i = false;

    /* renamed from: j, reason: collision with root package name */
    private float f2585j = 0.0f;

    /* renamed from: k, reason: collision with root package name */
    private boolean f2586k = false;

    /* renamed from: l, reason: collision with root package name */
    private float f2587l = 0.0f;

    /* renamed from: m, reason: collision with root package name */
    private int f2588m = 0;

    /* renamed from: n, reason: collision with root package name */
    private double f2589n = 0.0d;

    /* renamed from: o, reason: collision with root package name */
    private double f2590o = 0.0d;

    /* renamed from: p, reason: collision with root package name */
    private double f2591p = 0.0d;

    /* renamed from: q, reason: collision with root package name */
    private double f2592q = 0.0d;

    /* renamed from: r, reason: collision with root package name */
    private float f2593r = 0.0f;

    /* renamed from: s, reason: collision with root package name */
    private float f2594s = 0.0f;

    /* renamed from: t, reason: collision with root package name */
    private float[] f2595t = new float[2];

    /* renamed from: u, reason: collision with root package name */
    private HashMap f2596u = null;

    public f(String str) {
        this.f2576a = str;
    }

    private static void a(double d2, double d3, double d4, double d5, float[] fArr) {
        double d6 = (6378137.0d - 6356752.3142d) / 6378137.0d;
        double d7 = ((6378137.0d * 6378137.0d) - (6356752.3142d * 6356752.3142d)) / (6356752.3142d * 6356752.3142d);
        double d8 = (d5 * 0.017453292519943295d) - (d3 * 0.017453292519943295d);
        double d9 = 0.0d;
        double dAtan = Math.atan((1.0d - d6) * Math.tan(d2 * 0.017453292519943295d));
        double dAtan2 = Math.atan((1.0d - d6) * Math.tan(d4 * 0.017453292519943295d));
        double dCos = Math.cos(dAtan);
        double dCos2 = Math.cos(dAtan2);
        double dSin = Math.sin(dAtan);
        double dSin2 = Math.sin(dAtan2);
        double d10 = dCos * dCos2;
        double d11 = dSin * dSin2;
        double dAtan22 = 0.0d;
        double d12 = 0.0d;
        double dCos3 = 0.0d;
        double dSin3 = 0.0d;
        double d13 = d8;
        for (int i2 = 0; i2 < 20; i2++) {
            double d14 = d13;
            dCos3 = Math.cos(d13);
            dSin3 = Math.sin(d13);
            double d15 = dCos2 * dSin3;
            double d16 = (dCos * dSin2) - ((dSin * dCos2) * dCos3);
            double dSqrt = Math.sqrt((d15 * d15) + (d16 * d16));
            double d17 = d11 + (d10 * dCos3);
            dAtan22 = Math.atan2(dSqrt, d17);
            double d18 = dSqrt == 0.0d ? 0.0d : (d10 * dSin3) / dSqrt;
            double d19 = 1.0d - (d18 * d18);
            double d20 = d19 == 0.0d ? 0.0d : d17 - ((2.0d * d11) / d19);
            double d21 = d19 * d7;
            d9 = 1.0d + ((d21 / 16384.0d) * (4096.0d + (d21 * ((-768.0d) + (d21 * (320.0d - (175.0d * d21)))))));
            double d22 = (d21 / 1024.0d) * (256.0d + (d21 * ((-128.0d) + (d21 * (74.0d - (47.0d * d21))))));
            double d23 = (d6 / 16.0d) * d19 * (4.0d + (d6 * (4.0d - (3.0d * d19))));
            double d24 = d20 * d20;
            d12 = d22 * dSqrt * (d20 + ((d22 / 4.0d) * ((d17 * ((-1.0d) + (2.0d * d24))) - ((((d22 / 6.0d) * d20) * ((-3.0d) + ((4.0d * dSqrt) * dSqrt))) * ((-3.0d) + (4.0d * d24))))));
            d13 = d8 + ((1.0d - d23) * d6 * d18 * (dAtan22 + (d23 * dSqrt * (d20 + (d23 * d17 * ((-1.0d) + (2.0d * d20 * d20)))))));
            if (Math.abs((d13 - d14) / d13) < 1.0E-12d) {
                break;
            }
        }
        fArr[0] = (float) (6356752.3142d * d9 * (dAtan22 - d12));
        if (fArr.length > 1) {
            fArr[1] = (float) (((float) Math.atan2(dCos2 * dSin3, (dCos * dSin2) - ((dSin * dCos2) * dCos3))) * 57.29577951308232d);
            if (fArr.length > 2) {
                fArr[2] = (float) (((float) Math.atan2(dCos * dSin3, ((-dSin) * dCos2) + (dCos * dSin2 * dCos3))) * 57.29577951308232d);
            }
        }
    }

    public float a(f fVar) {
        float f2;
        synchronized (this.f2595t) {
            if (this.f2578c != this.f2589n || this.f2579d != this.f2590o || fVar.f2578c != this.f2591p || fVar.f2579d != this.f2592q) {
                a(this.f2578c, this.f2579d, fVar.f2578c, fVar.f2579d, this.f2595t);
                this.f2589n = this.f2578c;
                this.f2590o = this.f2579d;
                this.f2591p = fVar.f2578c;
                this.f2592q = fVar.f2579d;
                this.f2593r = this.f2595t[0];
                this.f2594s = this.f2595t[1];
            }
            f2 = this.f2593r;
        }
        return f2;
    }

    public float b(f fVar) {
        float f2;
        synchronized (this.f2595t) {
            if (this.f2578c != this.f2589n || this.f2579d != this.f2590o || fVar.f2578c != this.f2591p || fVar.f2579d != this.f2592q) {
                a(this.f2578c, this.f2579d, fVar.f2578c, fVar.f2579d, this.f2595t);
                this.f2589n = this.f2578c;
                this.f2590o = this.f2579d;
                this.f2591p = fVar.f2578c;
                this.f2592q = fVar.f2579d;
                this.f2593r = this.f2595t[0];
                this.f2594s = this.f2595t[1];
            }
            f2 = this.f2594s;
        }
        return f2;
    }

    public long a() {
        return this.f2577b;
    }

    public void a(long j2) {
        this.f2577b = j2;
    }

    public double b() {
        return this.f2578c;
    }

    public void a(double d2) {
        this.f2578c = d2;
    }

    public double c() {
        return this.f2579d;
    }

    public void b(double d2) {
        this.f2579d = d2;
    }

    public double d() {
        return this.f2581f;
    }

    public void c(double d2) {
        this.f2581f = d2;
        this.f2580e = true;
    }

    public float e() {
        return this.f2583h;
    }

    public void a(float f2) {
        this.f2583h = f2;
        this.f2582g = true;
    }

    public float f() {
        return this.f2585j;
    }

    public void b(float f2) {
        while (f2 < 0.0f) {
            f2 += 360.0f;
        }
        while (f2 >= 360.0f) {
            f2 -= 360.0f;
        }
        this.f2585j = f2;
        this.f2584i = true;
    }

    public float g() {
        return this.f2587l;
    }

    public void c(float f2) {
        this.f2587l = f2;
        this.f2586k = true;
    }

    public String toString() {
        return "Location[mProvider=" + this.f2576a + ",mTime=" + this.f2577b + ",mLatitude=" + this.f2578c + ",mLongitude=" + this.f2579d + ",mHasAltitude=" + this.f2580e + ",mAltitude=" + this.f2581f + ",mHasSpeed=" + this.f2582g + ",mSpeed=" + this.f2583h + ",mHasBearing=" + this.f2584i + ",mBearing=" + this.f2585j + ",mHasAccuracy=" + this.f2586k + ",mAccuracy=" + this.f2587l + "]";
    }

    public HashMap h() {
        return this.f2596u;
    }

    public void a(HashMap map) {
        this.f2596u = map;
    }
}
