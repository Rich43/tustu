package ax;

/* loaded from: TunerStudioMS.jar:ax/ae.class */
class ae extends ac {

    /* renamed from: a, reason: collision with root package name */
    private ab f6379a;

    /* renamed from: b, reason: collision with root package name */
    private ab f6380b;

    public ae(ab abVar, ab abVar2) {
        this.f6379a = abVar;
        this.f6380b = abVar2;
    }

    public double a(S s2) {
        return Math.pow(this.f6379a.b(s2), this.f6380b.b(s2));
    }

    @Override // ax.ab
    public double b(S s2) {
        return a(s2);
    }

    public String toString() {
        return "pow( " + this.f6379a.toString() + ", " + this.f6380b.toString() + " )";
    }
}
