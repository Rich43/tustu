package A;

import java.util.ArrayList;
import java.util.List;

/* loaded from: TunerStudioMS.jar:A/b.class */
public class b implements r {

    /* renamed from: b, reason: collision with root package name */
    private String f3b = null;

    /* renamed from: c, reason: collision with root package name */
    private String f4c = null;

    /* renamed from: d, reason: collision with root package name */
    private int f5d = -1;

    /* renamed from: e, reason: collision with root package name */
    private double f6e = 2.147483647E9d;

    /* renamed from: f, reason: collision with root package name */
    private double f7f = -2.147483648E9d;

    /* renamed from: a, reason: collision with root package name */
    List f8a = new ArrayList();

    @Override // A.r
    public String c() {
        return this.f3b;
    }

    public void a(String str) {
        this.f3b = str;
    }

    @Override // A.r
    public String d() {
        return this.f4c;
    }

    public void b(String str) {
        this.f4c = str;
    }

    @Override // c.f
    public int a() {
        return this.f5d;
    }

    public void a(int i2) {
        this.f5d = i2;
    }

    @Override // A.r
    public double e() {
        return this.f6e;
    }

    public void a(double d2) {
        this.f6e = d2;
    }

    @Override // A.r
    public double f() {
        return this.f7f;
    }

    public void b(double d2) {
        this.f7f = d2;
    }

    @Override // c.f
    public List b() {
        return this.f8a;
    }

    public void a(Object obj) {
        this.f8a.add(obj);
    }

    @Override // A.r
    public boolean b(Object obj) {
        if (this.f5d == 2 || this.f5d == 4) {
            return this.f8a.contains(obj);
        }
        if (!(obj instanceof Number)) {
            return true;
        }
        Number number = (Number) obj;
        return number.doubleValue() >= this.f7f && number.doubleValue() <= this.f6e;
    }
}
