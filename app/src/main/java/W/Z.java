package W;

import java.util.HashMap;

/* loaded from: TunerStudioMS.jar:W/Z.class */
public class Z {

    /* renamed from: a, reason: collision with root package name */
    private Float f2017a = Float.valueOf(1.0E7f);

    /* renamed from: b, reason: collision with root package name */
    private HashMap f2018b = new HashMap();

    /* renamed from: c, reason: collision with root package name */
    private HashMap f2019c = new HashMap();

    /* renamed from: d, reason: collision with root package name */
    private Float f2020d = Float.valueOf(Float.NaN);

    public HashMap a() {
        return this.f2018b;
    }

    public Float a(String str) {
        Float fValueOf = (Float) this.f2019c.get(str);
        if (fValueOf == null) {
            fValueOf = Float.isNaN(this.f2020d.floatValue()) ? this.f2017a : Float.valueOf(this.f2020d.floatValue() + 1.0f);
            this.f2019c.put(str, fValueOf);
            this.f2018b.put(fValueOf, str);
            this.f2020d = fValueOf;
        }
        return fValueOf;
    }
}
