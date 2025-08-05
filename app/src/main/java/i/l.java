package I;

import G.dh;

/* loaded from: TunerStudioMS.jar:I/l.class */
class l {

    /* renamed from: b, reason: collision with root package name */
    private String f1386b;

    /* renamed from: c, reason: collision with root package name */
    private String f1387c;

    /* renamed from: d, reason: collision with root package name */
    private dh f1388d;

    /* renamed from: e, reason: collision with root package name */
    private double f1389e = Double.NaN;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ k f1390a;

    l(k kVar, String str, String str2, dh dhVar) {
        this.f1390a = kVar;
        this.f1386b = str;
        this.f1387c = str2;
        this.f1388d = dhVar;
    }

    public String a() {
        return this.f1386b;
    }

    public String b() {
        return this.f1387c;
    }

    public dh c() {
        return this.f1388d;
    }

    public boolean equals(Object obj) {
        return obj instanceof String ? obj.equals(this.f1387c) : super.equals(obj);
    }

    public double d() {
        return this.f1389e;
    }

    public void a(double d2) {
        this.f1389e = d2;
    }
}
