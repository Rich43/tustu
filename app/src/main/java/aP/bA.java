package aP;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/* loaded from: TunerStudioMS.jar:aP/bA.class */
class bA implements S.d {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0256by f2963a;

    bA(C0256by c0256by) {
        this.f2963a = c0256by;
    }

    @Override // S.d
    public void a() {
        bB bBVar = new bB(this);
        if (SwingUtilities.isEventDispatchThread()) {
            bBVar.run();
            return;
        }
        try {
            SwingUtilities.invokeAndWait(bBVar);
        } catch (InterruptedException e2) {
            Logger.getLogger(C0256by.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        } catch (InvocationTargetException e3) {
            Logger.getLogger(C0256by.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
        }
    }

    @Override // S.d
    public void b() {
    }
}
