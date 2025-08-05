package P;

import G.bK;
import java.util.ArrayList;
import java.util.List;

/* loaded from: TunerStudioMS.jar:P/b.class */
public class b extends bK {

    /* renamed from: b, reason: collision with root package name */
    private String f1756b = null;

    /* renamed from: c, reason: collision with root package name */
    private String f1757c = null;

    /* renamed from: d, reason: collision with root package name */
    private String f1758d = null;

    /* renamed from: e, reason: collision with root package name */
    private String f1759e = null;

    public String b() {
        return this.f1756b;
    }

    public void b(String str) {
        this.f1756b = str;
    }

    public String c() {
        return this.f1757c;
    }

    public void c(String str) {
        this.f1757c = str;
    }

    public String d() {
        return this.f1758d;
    }

    public void d(String str) {
        this.f1758d = str;
    }

    public String e() {
        return this.f1759e;
    }

    public void e(String str) {
        this.f1759e = str;
    }

    @Override // G.bK
    public List a() {
        ArrayList arrayList = new ArrayList();
        if (this.f1756b != null && !this.f1756b.isEmpty()) {
            arrayList.add(this.f1756b);
        }
        if (this.f1757c != null && !this.f1757c.isEmpty()) {
            arrayList.add(this.f1757c);
        }
        if (this.f1758d != null && !this.f1758d.isEmpty()) {
            arrayList.add(this.f1758d);
        }
        if (this.f1759e != null && !this.f1759e.isEmpty()) {
            arrayList.add(this.f1759e);
        }
        return arrayList;
    }
}
