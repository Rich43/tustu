package bt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: TunerStudioMS.jar:bt/bF.class */
public class bF {

    /* renamed from: a, reason: collision with root package name */
    private static bF f8906a = null;

    /* renamed from: b, reason: collision with root package name */
    private final List f8907b = new ArrayList();

    private bF() {
    }

    public static bF a() {
        if (f8906a == null) {
            f8906a = new bF();
        }
        return f8906a;
    }

    public void a(bE bEVar) {
        this.f8907b.add(bEVar);
    }

    public void b(bE bEVar) {
        this.f8907b.remove(bEVar);
    }

    public void a(boolean z2) {
        Iterator it = this.f8907b.iterator();
        while (it.hasNext()) {
            ((bE) it.next()).c(z2);
        }
    }
}
