package y;

import jdk.internal.dynalink.CallSiteDescriptor;

/* renamed from: y.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:y/d.class */
public class C1896d {

    /* renamed from: a, reason: collision with root package name */
    private int f14046a = 0;

    /* renamed from: b, reason: collision with root package name */
    private int f14047b = 0;

    /* renamed from: c, reason: collision with root package name */
    private String f14048c = null;

    /* renamed from: d, reason: collision with root package name */
    private String f14049d = null;

    public String a() {
        return bI.a.a((this.f14046a + CallSiteDescriptor.OPERATOR_DELIMITER + this.f14047b + CallSiteDescriptor.OPERATOR_DELIMITER + this.f14048c + CallSiteDescriptor.OPERATOR_DELIMITER + this.f14049d).getBytes());
    }

    public static C1896d a(String str) {
        String[] strArrSplit = new String(bI.a.a(str)).split("\\|");
        C1896d c1896d = new C1896d();
        c1896d.f14046a = Integer.parseInt(strArrSplit[0]);
        c1896d.f14047b = Integer.parseInt(strArrSplit[1]);
        c1896d.f14048c = strArrSplit[2];
        c1896d.f14049d = strArrSplit[3];
        return c1896d;
    }

    public int b() {
        return this.f14046a;
    }

    public void a(int i2) {
        this.f14046a = i2;
    }

    public int c() {
        return this.f14047b;
    }

    public void b(int i2) {
        this.f14047b = i2;
    }

    public String d() {
        return this.f14048c;
    }

    public void b(String str) {
        this.f14048c = str;
    }

    public String e() {
        return this.f14049d;
    }

    public void c(String str) {
        this.f14049d = str;
    }
}
