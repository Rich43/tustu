package ax;

import sun.util.locale.LanguageTag;

/* loaded from: TunerStudioMS.jar:ax/X.class */
class X extends ac {

    /* renamed from: a, reason: collision with root package name */
    private ab f6370a;

    public X(ab abVar) {
        this.f6370a = null;
        this.f6370a = abVar;
    }

    public double a(S s2) {
        return this.f6370a.b(s2) * (-1.0d);
    }

    @Override // ax.ab
    public double b(S s2) {
        return a(s2);
    }

    public String toString() {
        return LanguageTag.SEP + this.f6370a.toString();
    }
}
