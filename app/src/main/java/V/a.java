package V;

/* loaded from: TunerStudioMS.jar:V/a.class */
public class a extends Exception {

    /* renamed from: a, reason: collision with root package name */
    private Exception f1896a;

    public a() {
        this.f1896a = null;
    }

    public a(String str) {
        this(str, null);
    }

    public a(String str, Exception exc) {
        super(str);
        this.f1896a = null;
        a(exc);
    }

    public void a(Exception exc) {
        this.f1896a = exc;
    }
}
