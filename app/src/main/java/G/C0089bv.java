package G;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

/* renamed from: G.bv, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/bv.class */
public class C0089bv extends Q implements Serializable {

    /* renamed from: b, reason: collision with root package name */
    private String f1020b = "";

    /* renamed from: a, reason: collision with root package name */
    HashMap f1021a = new HashMap();

    public String a() {
        return this.f1020b;
    }

    public void a(String str, double d2) {
        this.f1021a.put(str, Double.valueOf(d2));
    }

    public Iterator b() {
        return this.f1021a.keySet().iterator();
    }

    public double a(String str) {
        return ((Double) this.f1021a.get(str)).doubleValue();
    }

    public void b(String str) {
        this.f1020b = str;
    }
}
