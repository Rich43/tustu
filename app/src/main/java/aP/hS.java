package aP;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aP/hS.class */
class hS implements aE.e {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ hJ f3542a;

    hS(hJ hJVar) {
        this.f3542a = hJVar;
    }

    @Override // aE.e
    public void a(aE.a aVar, G.R r2) {
        try {
            C1818g.a(aVar);
        } catch (IOException e2) {
            bH.C.a("Failed to load User Aliases!");
            Logger.getLogger(hJ.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    @Override // aE.e
    public void e_() {
    }

    @Override // aE.e
    public void a(aE.a aVar) {
        try {
            C1818g.b(aVar);
        } catch (IOException e2) {
            bH.C.a("Failed to save User Aliases!");
            Logger.getLogger(hJ.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }
}
