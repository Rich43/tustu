package P;

import G.bK;
import java.util.ArrayList;
import java.util.List;

/* loaded from: TunerStudioMS.jar:P/c.class */
public class c extends bK {

    /* renamed from: b, reason: collision with root package name */
    private double f1760b = 0.0d;

    /* renamed from: c, reason: collision with root package name */
    private double[] f1761c = null;

    /* renamed from: d, reason: collision with root package name */
    private double[] f1762d = null;

    /* renamed from: e, reason: collision with root package name */
    private String f1763e = "rising";

    /* renamed from: f, reason: collision with root package name */
    private String f1764f = null;

    public void b(String str) {
        this.f1764f = str;
    }

    @Override // G.bK
    public List a() {
        ArrayList arrayList = new ArrayList();
        if (this.f1764f != null && !this.f1764f.isEmpty()) {
            arrayList.add(this.f1764f);
        }
        return arrayList;
    }
}
