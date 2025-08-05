package G;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: TunerStudioMS.jar:G/aX.class */
public class aX extends C0088bu implements Serializable {

    /* renamed from: f, reason: collision with root package name */
    private ArrayList f667f = new ArrayList();

    /* renamed from: g, reason: collision with root package name */
    private int f668g = -1;

    /* renamed from: h, reason: collision with root package name */
    private String f669h = null;

    /* renamed from: i, reason: collision with root package name */
    private int f670i = 1;

    /* renamed from: j, reason: collision with root package name */
    private String f671j = "";

    /* renamed from: k, reason: collision with root package name */
    private int f672k = 0;

    /* renamed from: l, reason: collision with root package name */
    private double f673l = 1.0d;

    /* renamed from: a, reason: collision with root package name */
    HashMap f674a = new HashMap();

    /* renamed from: m, reason: collision with root package name */
    private ArrayList f675m = new ArrayList();

    /* renamed from: n, reason: collision with root package name */
    private ArrayList f676n = new ArrayList();

    public int a() {
        return this.f667f.size();
    }

    public Iterator b() {
        return this.f667f.iterator();
    }

    public void a(String str, String str2) {
        this.f667f.add(new aY(this, str, str2));
    }

    public int c() {
        return this.f668g;
    }

    public void a(int i2) {
        this.f668g = i2;
    }

    public int d() {
        return this.f670i;
    }

    public void b(int i2) {
        this.f670i = i2;
    }

    public Iterator f() {
        return this.f675m.iterator();
    }

    public void a(C0068ba c0068ba) {
        this.f675m.add(c0068ba);
    }

    public Iterator g() {
        return this.f676n.iterator();
    }

    public void a(bE bEVar) {
        this.f676n.add(bEVar);
    }

    public void a(String str) {
        this.f669h = str;
    }

    public int h() {
        return this.f672k;
    }

    public void c(int i2) {
        this.f672k = i2;
    }

    public String i() {
        return this.f671j;
    }

    public void b(String str) {
        this.f671j = str;
    }

    public int j() {
        return this.f675m.size();
    }

    public double k() {
        return this.f673l;
    }

    public void a(double d2) {
        this.f673l = d2;
    }

    public void a(String str, double d2, double d3, double d4) {
        aZ aZVar = new aZ(this);
        aZVar.a(d2);
        aZVar.b(d3);
        aZVar.c(d4);
        this.f674a.put(str, aZVar);
    }

    public double c(String str) {
        aZ aZVar = (aZ) this.f674a.get(str);
        if (aZVar == null) {
            return Double.MIN_VALUE;
        }
        return aZVar.a();
    }

    public double d(String str) {
        aZ aZVar = (aZ) this.f674a.get(str);
        if (aZVar == null) {
            return Double.MAX_VALUE;
        }
        return aZVar.b();
    }

    public double e(String str) {
        aZ aZVar = (aZ) this.f674a.get(str);
        if (aZVar == null) {
            return 0.0d;
        }
        return aZVar.c();
    }
}
