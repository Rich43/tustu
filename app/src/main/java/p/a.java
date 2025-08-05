package P;

import G.bK;
import java.util.ArrayList;
import java.util.List;

/* loaded from: TunerStudioMS.jar:P/a.class */
public class a extends bK {

    /* renamed from: b, reason: collision with root package name */
    private String f1752b = null;

    /* renamed from: c, reason: collision with root package name */
    private String f1753c = "rising";

    /* renamed from: d, reason: collision with root package name */
    private String f1754d = null;

    /* renamed from: e, reason: collision with root package name */
    private String f1755e = null;

    public String b() {
        return this.f1752b;
    }

    public void b(String str) {
        this.f1752b = str;
    }

    public String c() {
        return this.f1754d;
    }

    public void c(String str) {
        this.f1754d = str;
    }

    public String d() {
        return this.f1755e;
    }

    public void d(String str) {
        this.f1755e = str;
    }

    @Override // G.bK
    public List a() {
        ArrayList arrayList = new ArrayList();
        if (this.f1752b != null && !this.f1752b.isEmpty()) {
            arrayList.add(this.f1752b);
        }
        if (this.f1754d != null && !this.f1754d.isEmpty()) {
            arrayList.add(this.f1754d);
        }
        if (this.f1755e != null && !this.f1755e.isEmpty()) {
            arrayList.add(this.f1755e);
        }
        return arrayList;
    }
}
