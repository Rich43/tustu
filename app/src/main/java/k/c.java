package K;

import G.InterfaceC0109co;

/* loaded from: TunerStudioMS.jar:K/c.class */
public class c implements InterfaceC0109co {

    /* renamed from: a, reason: collision with root package name */
    private double f1499a = 0.0d;

    /* renamed from: b, reason: collision with root package name */
    private static c f1500b = null;

    public static c a() {
        if (f1500b == null) {
            f1500b = new c();
        }
        return f1500b;
    }

    @Override // G.InterfaceC0109co
    public void setCurrentOutputChannelValue(String str, double d2) {
        this.f1499a = d2;
    }

    public double b() {
        return this.f1499a;
    }
}
