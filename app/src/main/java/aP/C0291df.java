package aP;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/* renamed from: aP.df, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/df.class */
class C0291df extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ Runnable f3221a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0289dd f3222b;

    C0291df(C0289dd c0289dd, Runnable runnable) {
        this.f3222b = c0289dd;
        this.f3221a = runnable;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        setName("LogTabBuilderThread");
        try {
            if (!SwingUtilities.isEventDispatchThread()) {
                SwingUtilities.invokeAndWait(this.f3221a);
            } else {
                this.f3221a.run();
            }
        } catch (InterruptedException e2) {
            Logger.getLogger(C0289dd.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        } catch (InvocationTargetException e3) {
            Logger.getLogger(C0289dd.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
        }
        this.f3222b.e();
        this.f3222b.f3215b.a();
        cZ.a().a(this.f3222b.f3215b);
    }
}
