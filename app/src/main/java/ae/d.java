package aE;

import java.io.File;

/* loaded from: TunerStudioMS.jar:aE/d.class */
public class d implements Cloneable {

    /* renamed from: a, reason: collision with root package name */
    private String f2367a = "";

    /* renamed from: b, reason: collision with root package name */
    private String f2368b = "";

    /* renamed from: c, reason: collision with root package name */
    private String f2369c = "";

    /* renamed from: d, reason: collision with root package name */
    private String[] f2370d = null;

    /* renamed from: e, reason: collision with root package name */
    private int f2371e = -1;

    /* renamed from: f, reason: collision with root package name */
    private boolean f2372f = true;

    /* renamed from: g, reason: collision with root package name */
    private String f2373g = "";

    public String a() {
        return this.f2367a;
    }

    public void a(String str) {
        this.f2367a = str;
    }

    public String b() {
        return this.f2368b;
    }

    public void b(String str) {
        this.f2368b = str;
    }

    public String c() {
        return this.f2369c;
    }

    public void c(String str) {
        if (str.indexOf(File.separatorChar) != -1) {
            str = str.substring(str.lastIndexOf(File.separatorChar) + 1);
        }
        this.f2369c = str;
    }

    public String a(a aVar) {
        return aVar.t() + a.f2348h + c();
    }

    public String[] d() {
        return this.f2370d;
    }

    public void a(String[] strArr) {
        this.f2370d = strArr;
    }

    public String toString() {
        return b() + " (" + a() + ")";
    }

    public int e() {
        return this.f2371e;
    }

    public void a(int i2) {
        this.f2371e = i2;
    }

    public boolean f() {
        return this.f2372f;
    }

    public void a(boolean z2) {
        this.f2372f = z2;
    }

    /* renamed from: g, reason: merged with bridge method [inline-methods] */
    public d clone() {
        return (d) super.clone();
    }

    public String h() {
        return this.f2373g;
    }
}
