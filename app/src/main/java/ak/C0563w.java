package ak;

import java.util.HashMap;
import java.util.Map;

/* renamed from: ak.w, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ak/w.class */
class C0563w {

    /* renamed from: a, reason: collision with root package name */
    boolean f4912a;

    /* renamed from: b, reason: collision with root package name */
    int f4913b;

    /* renamed from: c, reason: collision with root package name */
    int f4914c;

    /* renamed from: d, reason: collision with root package name */
    float f4915d;

    /* renamed from: e, reason: collision with root package name */
    private static final Map f4916e = new HashMap();

    public C0563w(C0525C c0525c, int i2) throws V.a {
        this.f4913b = 0;
        this.f4914c = 0;
        this.f4915d = 1.0f;
        this.f4912a = "sbyte,sword".contains(c0525c.f4587d);
        Integer num = (Integer) f4916e.get(c0525c.f4587d);
        if (num == null) {
            throw new V.a("Unknown storage mapping " + c0525c.f4587d);
        }
        this.f4914c = num.intValue();
        this.f4915d = "percent7".equals(c0525c.f4587d) ? 0.78125f : 1.0f / Float.parseFloat(c0525c.f4589f);
        this.f4913b = i2;
    }

    static {
        f4916e.put("ubyte", 1);
        f4916e.put("ubyte:1", 1);
        f4916e.put("ubyte:2", 1);
        f4916e.put("ubyte:3", 1);
        f4916e.put("sbyte", 1);
        f4916e.put("percent7", 1);
        f4916e.put("word", 2);
        f4916e.put("sword", 2);
    }
}
