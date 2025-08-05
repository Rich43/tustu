package L;

import G.aM;
import java.util.HashMap;

/* renamed from: L.l, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:L/l.class */
public class C0155l {

    /* renamed from: c, reason: collision with root package name */
    private static C0155l f1670c = null;

    /* renamed from: a, reason: collision with root package name */
    HashMap f1671a = new HashMap();

    /* renamed from: b, reason: collision with root package name */
    int f1672b = 0;

    private C0155l() {
    }

    public static C0155l a() {
        if (f1670c == null) {
            f1670c = new C0155l();
        }
        return f1670c;
    }

    public void b() {
        this.f1671a.clear();
        this.f1672b = 0;
    }

    public int a(aM aMVar) {
        this.f1671a.put(Integer.valueOf(this.f1672b), aMVar);
        int i2 = this.f1672b;
        this.f1672b = i2 + 1;
        return i2;
    }

    public aM a(int i2) {
        return (aM) this.f1671a.get(Integer.valueOf(i2));
    }
}
