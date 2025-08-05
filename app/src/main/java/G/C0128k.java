package G;

import java.io.Serializable;

/* renamed from: G.k, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/k.class */
public class C0128k implements Serializable {

    /* renamed from: a, reason: collision with root package name */
    public static final C0128k f1250a = new C0128k(255, 255, 255);

    /* renamed from: b, reason: collision with root package name */
    public static final C0128k f1251b = f1250a;

    /* renamed from: c, reason: collision with root package name */
    public static final C0128k f1252c = new C0128k(192, 192, 192);

    /* renamed from: d, reason: collision with root package name */
    public static final C0128k f1253d = f1252c;

    /* renamed from: e, reason: collision with root package name */
    public static final C0128k f1254e = new C0128k(128, 128, 128);

    /* renamed from: f, reason: collision with root package name */
    public static final C0128k f1255f = f1254e;

    /* renamed from: g, reason: collision with root package name */
    public static final C0128k f1256g = new C0128k(64, 64, 64);

    /* renamed from: h, reason: collision with root package name */
    public static final C0128k f1257h = f1256g;

    /* renamed from: i, reason: collision with root package name */
    public static final C0128k f1258i = new C0128k(0, 0, 0);

    /* renamed from: j, reason: collision with root package name */
    public static final C0128k f1259j = f1258i;

    /* renamed from: k, reason: collision with root package name */
    public static final C0128k f1260k = new C0128k(255, 0, 0);

    /* renamed from: l, reason: collision with root package name */
    public static final C0128k f1261l = f1260k;

    /* renamed from: m, reason: collision with root package name */
    public static final C0128k f1262m = new C0128k(255, 175, 175);

    /* renamed from: n, reason: collision with root package name */
    public static final C0128k f1263n = f1262m;

    /* renamed from: o, reason: collision with root package name */
    public static final C0128k f1264o = new C0128k(255, 200, 0);

    /* renamed from: p, reason: collision with root package name */
    public static final C0128k f1265p = f1264o;

    /* renamed from: q, reason: collision with root package name */
    public static final C0128k f1266q = new C0128k(255, 255, 0);

    /* renamed from: r, reason: collision with root package name */
    public static final C0128k f1267r = f1266q;

    /* renamed from: s, reason: collision with root package name */
    public static final C0128k f1268s = new C0128k(0, 255, 0);

    /* renamed from: t, reason: collision with root package name */
    public static final C0128k f1269t = f1268s;

    /* renamed from: u, reason: collision with root package name */
    public static final C0128k f1270u = new C0128k(255, 0, 255);

    /* renamed from: v, reason: collision with root package name */
    public static final C0128k f1271v = f1270u;

    /* renamed from: w, reason: collision with root package name */
    public static final C0128k f1272w = new C0128k(0, 255, 255);

    /* renamed from: x, reason: collision with root package name */
    public static final C0128k f1273x = f1272w;

    /* renamed from: y, reason: collision with root package name */
    public static final C0128k f1274y = new C0128k(0, 0, 255);

    /* renamed from: z, reason: collision with root package name */
    public static final C0128k f1275z = f1274y;

    /* renamed from: A, reason: collision with root package name */
    public static C0128k f1276A = new C0128k(0, 0, 0, 0);

    /* renamed from: B, reason: collision with root package name */
    private int f1277B;

    public C0128k(int i2, int i3, int i4) {
        this(i2, i3, i4, 255);
    }

    public C0128k(int i2) {
        this.f1277B = 0;
        this.f1277B = i2;
    }

    public C0128k(int i2, int i3, int i4, int i5) {
        this.f1277B = 0;
        this.f1277B = ((i5 & 255) << 24) | ((i2 & 255) << 16) | ((i3 & 255) << 8) | ((i4 & 255) << 0);
        a(i2, i3, i4, i5);
    }

    private static void a(int i2, int i3, int i4, int i5) {
        boolean z2 = false;
        String str = "";
        if (i5 < 0 || i5 > 255) {
            z2 = true;
            str = str + " Alpha";
        }
        if (i2 < 0 || i2 > 255) {
            z2 = true;
            str = str + " Red";
        }
        if (i3 < 0 || i3 > 255) {
            z2 = true;
            str = str + " Green";
        }
        if (i4 < 0 || i4 > 255) {
            z2 = true;
            str = str + " Blue";
        }
        if (z2) {
            throw new IllegalArgumentException("Color parameter outside of expected range:" + str);
        }
    }

    public int a() {
        return this.f1277B;
    }

    public int b() {
        return (this.f1277B >> 16) & 255;
    }

    public int c() {
        return (this.f1277B >> 8) & 255;
    }

    public int d() {
        return (this.f1277B >> 0) & 255;
    }

    public int e() {
        return (this.f1277B >> 24) & 255;
    }
}
