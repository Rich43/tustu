package bS;

/* loaded from: TunerStudioMS.jar:bS/a.class */
public class a extends Exception {

    /* renamed from: a, reason: collision with root package name */
    private int f7537a;

    /* renamed from: b, reason: collision with root package name */
    private int f7538b;

    public a(int i2, int i3) {
        super("Unexpected Size! Expected: " + i2 + ", Actual Size: " + i3);
        this.f7538b = i3;
        this.f7537a = i2;
    }
}
