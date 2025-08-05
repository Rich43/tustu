package W;

import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: TunerStudioMS.jar:W/N.class */
public class N extends ArrayList {

    /* renamed from: a, reason: collision with root package name */
    private String f1941a = null;

    public String a() {
        return this.f1941a;
    }

    public void a(String str) {
        this.f1941a = str;
    }

    public M b(String str) {
        Iterator it = iterator();
        while (it.hasNext()) {
            M m2 = (M) it.next();
            if (m2.f().equals(str)) {
                return m2;
            }
        }
        return null;
    }
}
