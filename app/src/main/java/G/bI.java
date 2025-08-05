package G;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: TunerStudioMS.jar:G/bI.class */
public class bI extends C0088bu implements Serializable {

    /* renamed from: a, reason: collision with root package name */
    private final ArrayList f847a = new ArrayList();

    /* renamed from: f, reason: collision with root package name */
    private boolean f848f = false;

    /* renamed from: g, reason: collision with root package name */
    private A f849g = null;

    public void a(String str, bK bKVar) {
        this.f847a.add(new bJ(this, str, bKVar));
    }

    public List a() {
        return this.f847a;
    }

    public A b() {
        return this.f849g;
    }

    public void a(A a2) {
        this.f849g = a2;
    }

    public boolean c() {
        return this.f848f;
    }

    public void a(boolean z2) {
        this.f848f = z2;
    }

    @Override // G.C0088bu
    public List e() {
        ArrayList arrayList = new ArrayList();
        Iterator it = this.f847a.iterator();
        while (it.hasNext()) {
            arrayList.addAll(((bJ) it.next()).b().a());
        }
        return arrayList;
    }
}
