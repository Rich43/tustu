package G;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/* renamed from: G.cb, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/cb.class */
public class C0096cb implements Serializable {

    /* renamed from: a, reason: collision with root package name */
    public static String f1065a = "composite";

    /* renamed from: b, reason: collision with root package name */
    public static String f1066b = "tooth";

    /* renamed from: c, reason: collision with root package name */
    public static String f1067c = "trigger";

    /* renamed from: d, reason: collision with root package name */
    public static String f1068d = "csv";

    /* renamed from: e, reason: collision with root package name */
    public static String f1069e = "stdMS2Composite";

    /* renamed from: f, reason: collision with root package name */
    public static String f1070f = "stdMS3Composite";

    /* renamed from: g, reason: collision with root package name */
    public static String f1071g = "stdMS2SyncComposite";

    /* renamed from: h, reason: collision with root package name */
    public static String f1072h = "stdMS3SyncComposite";

    /* renamed from: i, reason: collision with root package name */
    public static String f1073i = "stdMS2Tooth";

    /* renamed from: j, reason: collision with root package name */
    public static String f1074j = "stdMS3Tooth";

    /* renamed from: k, reason: collision with root package name */
    public static String f1075k = "stdMS2Trigger";

    /* renamed from: l, reason: collision with root package name */
    public static String f1076l = "stdMS3Trigger";

    /* renamed from: n, reason: collision with root package name */
    private String f1077n = "Standard";

    /* renamed from: o, reason: collision with root package name */
    private C0098cd f1078o = null;

    /* renamed from: p, reason: collision with root package name */
    private String f1079p = null;

    /* renamed from: q, reason: collision with root package name */
    private String f1080q = null;

    /* renamed from: r, reason: collision with root package name */
    private String f1081r = null;

    /* renamed from: s, reason: collision with root package name */
    private String f1082s = null;

    /* renamed from: t, reason: collision with root package name */
    private String f1083t = null;

    /* renamed from: u, reason: collision with root package name */
    private String f1084u = null;

    /* renamed from: v, reason: collision with root package name */
    private String f1085v = "";

    /* renamed from: w, reason: collision with root package name */
    private int f1086w = -1;

    /* renamed from: x, reason: collision with root package name */
    private int f1087x = -1;

    /* renamed from: y, reason: collision with root package name */
    private boolean f1088y = true;

    /* renamed from: z, reason: collision with root package name */
    private boolean f1089z = false;

    /* renamed from: A, reason: collision with root package name */
    private InterfaceC0099ce f1090A = null;

    /* renamed from: B, reason: collision with root package name */
    private String f1091B = null;

    /* renamed from: m, reason: collision with root package name */
    Map f1092m = new HashMap();

    /* renamed from: C, reason: collision with root package name */
    private int f1093C = -1;

    /* renamed from: D, reason: collision with root package name */
    private int f1094D = -1;

    /* renamed from: E, reason: collision with root package name */
    private int f1095E = 1;

    public String a() {
        return this.f1085v;
    }

    public C0098cd b() {
        return this.f1078o;
    }

    public void c() {
        if (this.f1090A != null) {
            this.f1078o = this.f1090A.a(this.f1078o);
        }
    }

    public void a(C0098cd c0098cd) {
        this.f1078o = c0098cd;
    }

    public String d() {
        return this.f1079p;
    }

    public void a(String str) throws V.g {
        if (!str.equals(f1065a) && !str.equals(f1066b) && !str.equals(f1067c) && !str.equals(f1068d)) {
            throw new V.g("Invalid logger type! Valid types:" + f1065a + ", " + f1066b + " and " + f1067c + " and " + f1068d);
        }
        this.f1079p = str;
    }

    public String e() {
        return this.f1081r;
    }

    public void b(String str) {
        this.f1081r = str;
    }

    public int f() {
        return this.f1087x;
    }

    public void a(int i2) {
        this.f1087x = i2;
    }

    public String g() {
        return this.f1080q;
    }

    public void c(String str) {
        this.f1080q = str;
    }

    public String h() {
        return this.f1084u;
    }

    public void d(String str) {
        this.f1084u = str;
    }

    public int i() {
        return this.f1094D;
    }

    public void b(int i2) {
        this.f1094D = i2;
    }

    public String j() {
        return this.f1082s;
    }

    public void e(String str) {
        this.f1082s = str;
    }

    public String k() {
        return this.f1083t;
    }

    public void f(String str) {
        this.f1083t = str;
    }

    public boolean l() {
        return this.f1088y;
    }

    public void a(boolean z2) {
        this.f1088y = z2;
    }

    public int m() {
        return this.f1093C;
    }

    public void c(int i2) {
        this.f1093C = i2;
    }

    public void a(InterfaceC0099ce interfaceC0099ce) {
        this.f1090A = interfaceC0099ce;
    }

    public String n() {
        return this.f1077n;
    }

    public void g(String str) throws V.g {
        if (!str.equals("UDP_Stream") && !str.equals("Standard")) {
            throw new V.g("Unknown logProcessorType! Supported types: Standard, UDP_Stream");
        }
        this.f1077n = str;
    }

    public int o() {
        return this.f1086w;
    }

    public void d(int i2) {
        this.f1086w = i2;
    }

    public void a(C0101cg c0101cg) {
        this.f1092m.put(c0101cg.a(), c0101cg);
    }

    public C0101cg h(String str) {
        return (C0101cg) this.f1092m.get(str);
    }

    public int p() {
        return this.f1095E;
    }

    public void e(int i2) {
        this.f1095E = i2;
    }

    public void i(String str) {
        this.f1091B = str;
    }

    public String q() {
        return this.f1091B;
    }

    public boolean r() {
        return this.f1089z;
    }

    public void b(boolean z2) {
        this.f1089z = z2;
    }
}
