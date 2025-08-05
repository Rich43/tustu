package ax;

/* loaded from: TunerStudioMS.jar:ax/af.class */
public class af extends ac {

    /* renamed from: a, reason: collision with root package name */
    private ab f6381a;

    public af(ab abVar) {
        this.f6381a = null;
        this.f6381a = abVar;
    }

    public double a(S s2) {
        return 1.0d / this.f6381a.b(s2);
    }

    @Override // ax.ab
    public double b(S s2) {
        return a(s2);
    }

    public String toString() {
        return "recip( " + this.f6381a.toString() + " )";
    }
}
