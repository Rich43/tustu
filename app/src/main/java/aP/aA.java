package aP;

import W.C0200z;
import java.io.File;

/* loaded from: TunerStudioMS.jar:aP/aA.class */
class aA {

    /* renamed from: a, reason: collision with root package name */
    File f2796a;

    /* renamed from: c, reason: collision with root package name */
    private String f2797c = null;

    /* renamed from: d, reason: collision with root package name */
    private String f2798d;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0224at f2799b;

    public aA(C0224at c0224at, File file) {
        this.f2799b = c0224at;
        this.f2796a = null;
        this.f2798d = null;
        this.f2796a = file;
        if (this.f2796a != null) {
            a(C0200z.a(this.f2796a));
            if (this.f2798d == null && this.f2796a.exists()) {
                this.f2798d = bH.W.b(this.f2796a.getName(), ".ini", "");
            } else {
                this.f2798d = b();
            }
        }
    }

    public aA(C0224at c0224at, File file, String str) {
        this.f2799b = c0224at;
        this.f2796a = null;
        this.f2798d = null;
        this.f2796a = file;
        a(str);
        if (this.f2796a != null) {
            if (this.f2798d == null && this.f2796a.exists()) {
                this.f2798d = bH.W.b(this.f2796a.getName(), ".ini", "");
            } else {
                this.f2798d = b();
            }
        }
    }

    public File a() {
        return this.f2796a;
    }

    public String toString() {
        return this.f2796a == null ? "" : this.f2798d;
    }

    public boolean equals(Object obj) {
        return (!(obj instanceof String) || this.f2797c == null) ? super.equals(obj) : this.f2797c.trim().equals(((String) obj).trim());
    }

    public String b() {
        return this.f2797c;
    }

    public void a(String str) {
        if (str != null && !str.equals("20") && !str.equals("\u0014")) {
            this.f2798d = str;
        }
        this.f2797c = str;
    }

    public void b(String str) {
        this.f2798d = str;
    }
}
