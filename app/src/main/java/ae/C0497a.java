package ae;

import java.util.ArrayList;
import java.util.List;

/* renamed from: ae.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ae/a.class */
public class C0497a implements t {

    /* renamed from: b, reason: collision with root package name */
    private String f4339b = null;

    /* renamed from: c, reason: collision with root package name */
    private String f4340c = null;

    /* renamed from: d, reason: collision with root package name */
    private int f4341d = -1;

    /* renamed from: e, reason: collision with root package name */
    private double f4342e = 2.147483647E9d;

    /* renamed from: f, reason: collision with root package name */
    private double f4343f = -2.147483648E9d;

    /* renamed from: a, reason: collision with root package name */
    List f4344a = new ArrayList();

    /* renamed from: g, reason: collision with root package name */
    private Object f4345g = null;

    @Override // ae.t
    public String a() {
        return this.f4339b;
    }

    public void a(String str) {
        this.f4339b = str;
    }

    public void b(String str) {
        this.f4340c = str;
    }

    @Override // ae.t
    public int b() {
        return this.f4341d;
    }

    public void a(int i2) {
        this.f4341d = i2;
    }

    @Override // ae.t
    public double c() {
        return this.f4342e;
    }

    @Override // ae.t
    public double d() {
        return this.f4343f;
    }

    @Override // ae.t
    public List e() {
        return this.f4344a;
    }

    @Override // ae.t
    public boolean a(Object obj) {
        if (this.f4341d == 2 || this.f4341d == 0) {
            return this.f4344a.contains(obj);
        }
        if (this.f4341d == 4) {
            return obj instanceof Boolean;
        }
        if (!(obj instanceof Number)) {
            return false;
        }
        Number number = (Number) obj;
        return number.doubleValue() >= this.f4343f && number.doubleValue() <= this.f4342e;
    }

    @Override // ae.t
    public Object f() {
        return this.f4345g;
    }

    public void b(Object obj) {
        this.f4345g = obj;
    }
}
