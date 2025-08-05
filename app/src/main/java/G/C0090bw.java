package G;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/* renamed from: G.bw, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/bw.class */
public class C0090bw extends AbstractC0093bz implements Serializable {

    /* renamed from: a, reason: collision with root package name */
    ArrayList f1022a = new ArrayList();

    @Override // G.AbstractC0093bz
    public String b() {
        return null;
    }

    public void a(C0089bv c0089bv) {
        this.f1022a.add(c0089bv);
    }

    public Iterator a() {
        return this.f1022a.iterator();
    }

    public C0089bv a(String str) {
        Iterator itA = a();
        while (itA.hasNext()) {
            C0089bv c0089bv = (C0089bv) itA.next();
            if (c0089bv.a().equals(str)) {
                return c0089bv;
            }
        }
        return null;
    }
}
