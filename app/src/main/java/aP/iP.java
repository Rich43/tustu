package aP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:aP/iP.class */
class iP extends Thread {

    /* renamed from: a, reason: collision with root package name */
    List f3676a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ iN f3677b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    iP(iN iNVar) {
        super("StartScreenUpdate");
        this.f3677b = iNVar;
        this.f3676a = Collections.synchronizedList(new ArrayList());
        setDaemon(false);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.f3676a.size() > 0) {
            try {
                Thread.sleep(3000L);
                iO iOVar = (iO) this.f3676a.get(0);
                this.f3676a.remove(0);
                this.f3677b.a(iOVar.f3672a, iOVar.f3673b, iOVar.f3674c);
            } catch (Exception e2) {
                Logger.getLogger(iN.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
    }

    public void a(iO iOVar) {
        this.f3676a.add(iOVar);
        if (isAlive()) {
            return;
        }
        start();
    }
}
