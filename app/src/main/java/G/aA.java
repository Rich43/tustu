package G;

import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: TunerStudioMS.jar:G/aA.class */
public class aA extends Q {

    /* renamed from: a, reason: collision with root package name */
    private String f519a = null;

    /* renamed from: b, reason: collision with root package name */
    private String f520b = null;

    /* renamed from: c, reason: collision with root package name */
    private int f521c = -1;

    /* renamed from: d, reason: collision with root package name */
    private ArrayList f522d = new ArrayList();

    /* renamed from: e, reason: collision with root package name */
    private String f523e = null;

    /* renamed from: f, reason: collision with root package name */
    private String f524f = null;

    /* renamed from: g, reason: collision with root package name */
    private String f525g = null;

    public void a(aA aAVar) {
        this.f522d.add(aAVar);
    }

    public Iterator a() {
        return this.f522d.iterator();
    }

    public boolean b() {
        return this.f522d.size() > 0;
    }

    public boolean c() {
        return this.f519a != null && this.f519a.equals("std_separator");
    }

    public void a(boolean z2) {
        if (z2) {
            this.f519a = "std_separator";
        } else {
            this.f519a = null;
        }
    }

    public String d() {
        return this.f519a;
    }

    public void a(String str) {
        this.f519a = str;
    }

    public String e() {
        return this.f520b;
    }

    public void b(String str) {
        this.f520b = str;
    }

    public int f() {
        return this.f521c;
    }

    public void a(int i2) {
        this.f521c = i2;
    }

    public String g() {
        return this.f523e;
    }

    public void c(String str) {
        this.f523e = str;
    }

    public String h() {
        return this.f524f;
    }

    public void d(String str) {
        this.f524f = str;
    }

    public String i() {
        return this.f525g;
    }

    public void e(String str) {
        this.f525g = str;
    }
}
