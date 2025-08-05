package R;

import bH.C;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:R/n.class */
public class n {

    /* renamed from: a, reason: collision with root package name */
    g f1815a = g.a();

    public void a() {
        Iterator it = this.f1815a.b().iterator();
        while (it.hasNext()) {
            l lVar = (l) it.next();
            if (a(lVar)) {
                this.f1815a.a(lVar.k());
            }
        }
    }

    private boolean a(l lVar) {
        c cVarA = d.a().a(lVar.a());
        if (cVarA == null) {
            C.c("No MessageProcessor to process message type: " + lVar.a());
            return false;
        }
        try {
            return cVarA.a(lVar);
        } catch (k e2) {
            Logger.getLogger(n.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            return false;
        }
    }

    boolean b() {
        return this.f1815a.d();
    }
}
