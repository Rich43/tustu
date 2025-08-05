package bG;

/* loaded from: TunerStudioMS.jar:bG/k.class */
public class k {

    /* renamed from: a, reason: collision with root package name */
    private double f6939a = Double.NaN;

    /* renamed from: b, reason: collision with root package name */
    private double f6940b = Double.NaN;

    public double a() {
        return this.f6939a;
    }

    public void a(double d2) {
        this.f6939a = d2;
    }

    public double b() {
        return this.f6940b;
    }

    public void b(double d2) {
        this.f6940b = d2;
    }

    public boolean c() {
        return (Double.isNaN(this.f6939a) || Double.isNaN(this.f6940b)) ? false : true;
    }
}
