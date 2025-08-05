package G;

import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:G/cJ.class */
public class cJ {

    /* renamed from: b, reason: collision with root package name */
    private static cJ f1050b = null;

    /* renamed from: a, reason: collision with root package name */
    HashMap f1051a = new HashMap();

    private cJ() {
    }

    public static cJ a() {
        if (f1050b == null) {
            f1050b = new cJ();
        }
        return f1050b;
    }

    public void a(cK cKVar) {
        this.f1051a.put(cKVar.c(), cKVar);
    }

    public cT a(String str) {
        cK cKVar = (cK) this.f1051a.get(str);
        if (cKVar.b() == null) {
            return (cT) null;
        }
        try {
            return (cT) cKVar.b().newInstance();
        } catch (IllegalAccessException e2) {
            Logger.getLogger(cJ.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            return null;
        } catch (InstantiationException e3) {
            Logger.getLogger(cJ.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            return null;
        }
    }

    public cP b(String str) {
        cK cKVar = (cK) this.f1051a.get(str);
        if (cKVar.b() == null) {
            return (cP) null;
        }
        try {
            return (cP) cKVar.a().newInstance();
        } catch (IllegalAccessException e2) {
            Logger.getLogger(cJ.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            return null;
        } catch (InstantiationException e3) {
            Logger.getLogger(cJ.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            return null;
        }
    }

    public Iterator b() {
        return this.f1051a.values().iterator();
    }
}
