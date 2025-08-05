package G;

import java.io.Serializable;
import jdk.internal.dynalink.CallSiteDescriptor;

/* renamed from: G.w, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/w.class */
public class C0140w implements Serializable {

    /* renamed from: a, reason: collision with root package name */
    private int f1325a = Integer.MAX_VALUE;

    /* renamed from: b, reason: collision with root package name */
    private int f1326b = 0;

    public String toString() {
        return "dr: " + a() + CallSiteDescriptor.TOKEN_DELIMITER + b();
    }

    public int a() {
        return this.f1325a;
    }

    public void a(int i2) {
        this.f1325a = i2;
    }

    public int b() {
        return this.f1326b;
    }

    public void b(int i2) {
        this.f1326b = i2;
    }

    public int c() {
        return (this.f1326b - this.f1325a) + 1;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof C0140w)) {
            return super.equals(obj);
        }
        C0140w c0140w = (C0140w) obj;
        return c0140w.a() == a() && c0140w.b() == b();
    }
}
