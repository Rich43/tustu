package G;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* loaded from: TunerStudioMS.jar:G/dc.class */
public class dc extends dk implements Serializable {

    /* renamed from: a, reason: collision with root package name */
    List f1189a = new ArrayList();

    public void a(dd ddVar) {
        this.f1189a.add(ddVar);
    }

    public int a() {
        return this.f1189a.size();
    }

    public String a(int i2) {
        return ((dd) this.f1189a.get(i2)).b();
    }

    public dd a(String str) {
        for (dd ddVar : this.f1189a) {
            if (ddVar.b().equals(str)) {
                return ddVar;
            }
        }
        return null;
    }

    @Override // G.dk
    public String b() {
        return this.f1189a.isEmpty() ? super.b() : a(0);
    }
}
