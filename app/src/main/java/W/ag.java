package W;

import java.io.File;

/* loaded from: TunerStudioMS.jar:W/ag.class */
public class ag {

    /* renamed from: a, reason: collision with root package name */
    private File f2071a = null;

    /* renamed from: b, reason: collision with root package name */
    private String f2072b = "";

    /* renamed from: c, reason: collision with root package name */
    private String f2073c = "";

    /* renamed from: d, reason: collision with root package name */
    private String f2074d = "";

    /* renamed from: e, reason: collision with root package name */
    private String f2075e = "";

    public File a() {
        return this.f2071a;
    }

    public void a(File file) {
        this.f2071a = file;
    }

    public String b() {
        return this.f2072b;
    }

    public void a(String str) {
        this.f2072b = str;
    }

    public String c() {
        return this.f2073c;
    }

    public void b(String str) {
        this.f2073c = str;
    }

    public String d() {
        return this.f2074d;
    }

    public void c(String str) {
        this.f2074d = str;
    }

    public String e() {
        return this.f2075e;
    }

    public String d(String str) {
        String name = a() != null ? a().getName() : "";
        return name.toLowerCase().endsWith(new StringBuilder().append(".").append(str).toString()) ? name.substring(0, name.length() - 4) : name;
    }

    public void e(String str) {
        this.f2075e = str;
    }
}
