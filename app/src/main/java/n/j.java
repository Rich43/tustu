package n;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: TunerStudioMS.jar:n/j.class */
public class j {

    /* renamed from: b, reason: collision with root package name */
    private static j f12942b = null;

    /* renamed from: a, reason: collision with root package name */
    List f12943a = new ArrayList();

    private j() {
    }

    public static j a() {
        if (f12942b == null) {
            f12942b = new j();
        }
        return f12942b;
    }

    public void a(String str) {
        Iterator it = this.f12943a.iterator();
        while (it.hasNext()) {
            ((i) it.next()).a(str);
        }
    }

    public void b(String str) {
        Iterator it = this.f12943a.iterator();
        while (it.hasNext()) {
            ((i) it.next()).b(str);
        }
    }

    public void a(i iVar) {
        this.f12943a.add(iVar);
    }
}
