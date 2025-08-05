package ax;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:ax/S.class */
public class S implements Cloneable {

    /* renamed from: a, reason: collision with root package name */
    List f6363a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    int f6364b = 0;

    /* renamed from: c, reason: collision with root package name */
    int f6365c = 0;

    void a(T t2) {
        this.f6363a.add(t2);
    }

    public int a() {
        return this.f6364b;
    }

    public int b() {
        return this.f6365c;
    }

    public Object clone() {
        Object objClone = null;
        try {
            objClone = super.clone();
        } catch (CloneNotSupportedException e2) {
            Logger.getLogger(S.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        return objClone;
    }
}
