package G;

import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: TunerStudioMS.jar:G/X.class */
public class X {

    /* renamed from: b, reason: collision with root package name */
    private static X f503b = null;

    /* renamed from: a, reason: collision with root package name */
    ArrayList f504a = new ArrayList();

    private X() {
    }

    public static X a() {
        if (f503b == null) {
            f503b = new X();
        }
        return f503b;
    }

    public void a(cV cVVar) {
        this.f504a.add(cVVar);
    }

    public boolean a(bS bSVar) {
        Iterator it = this.f504a.iterator();
        while (it.hasNext()) {
            if (((cV) it.next()).a(bSVar)) {
                return true;
            }
        }
        return false;
    }
}
