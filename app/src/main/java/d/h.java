package d;

/* loaded from: TunerStudioMS.jar:d/h.class */
public class h {

    /* renamed from: a, reason: collision with root package name */
    private String f12099a;

    /* renamed from: b, reason: collision with root package name */
    private String f12100b;

    public h(String str, String str2) {
        this.f12099a = str;
        this.f12100b = str2;
    }

    public String a() {
        return this.f12099a;
    }

    public String toString() {
        return this.f12100b;
    }

    public boolean equals(Object obj) {
        return obj instanceof String ? ((String) obj).equals(this.f12099a) : obj instanceof h ? ((h) obj).a().equals(this.f12099a) : super.equals(obj);
    }
}
