package W;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* loaded from: TunerStudioMS.jar:W/V.class */
public abstract class V {

    /* renamed from: c, reason: collision with root package name */
    Map f1954c = new HashMap();

    /* renamed from: d, reason: collision with root package name */
    public static boolean f1955d = true;

    public abstract String i();

    public abstract boolean a(String str);

    public abstract void a();

    public abstract Iterator b();

    public abstract float[] c();

    public abstract long d();

    public abstract boolean e();

    public abstract boolean f();

    public abstract HashMap g();

    public abstract String h();

    public boolean j() {
        return false;
    }

    public Z a(int i2) {
        return (Z) this.f1954c.get(Integer.valueOf(i2));
    }

    protected Z b(int i2) {
        Z zA = a(i2);
        if (zA == null) {
            zA = new Z();
            this.f1954c.put(Integer.valueOf(i2), zA);
        }
        return zA;
    }

    public boolean k() {
        return f1955d;
    }
}
