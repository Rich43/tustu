package ai;

/* renamed from: ai.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ai/b.class */
public class C0512b {

    /* renamed from: a, reason: collision with root package name */
    private String f4513a;

    /* renamed from: b, reason: collision with root package name */
    private String f4514b;

    public C0512b() {
        this.f4513a = "";
        this.f4514b = "";
    }

    public C0512b(String str, String str2) {
        this.f4513a = "";
        this.f4514b = "";
        this.f4513a = str;
        this.f4514b = str2;
    }

    public String a() {
        return this.f4513a;
    }

    public void a(String str) {
        this.f4513a = str;
    }

    public String b() {
        return this.f4514b;
    }

    public void b(String str) {
        this.f4514b = str;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof C0512b)) {
            return false;
        }
        C0512b c0512b = (C0512b) obj;
        return c0512b.a().equals(this.f4513a) && c0512b.b().equals(this.f4514b);
    }
}
