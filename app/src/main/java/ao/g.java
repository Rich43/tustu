package aO;

import W.Y;
import bH.C;
import com.efiAnalytics.ui.aQ;
import com.efiAnalytics.ui.eB;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/* loaded from: TunerStudioMS.jar:aO/g.class */
class g implements Y, aQ {

    /* renamed from: a, reason: collision with root package name */
    long f2657a = 0;

    /* renamed from: b, reason: collision with root package name */
    eB f2658b = null;

    /* renamed from: c, reason: collision with root package name */
    boolean f2659c = true;

    /* renamed from: d, reason: collision with root package name */
    final aQ f2660d = this;

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ a f2661e;

    g(a aVar) {
        this.f2661e = aVar;
    }

    @Override // W.Y
    public void a(ArrayList arrayList, long j2) {
        try {
            this.f2657a = j2;
            h hVar = new h(this);
            if (SwingUtilities.isEventDispatchThread()) {
                hVar.run();
            } else {
                SwingUtilities.invokeAndWait(hVar);
            }
        } catch (InterruptedException e2) {
            Logger.getLogger(a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        } catch (InvocationTargetException e3) {
            Logger.getLogger(a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
        }
    }

    @Override // W.Y
    public boolean a(long j2) {
        this.f2658b.a(j2 / this.f2657a);
        return this.f2659c;
    }

    @Override // W.Y
    public void a(ArrayList arrayList) {
        this.f2661e.f2649k.b(arrayList);
        this.f2658b.setVisible(false);
    }

    @Override // W.Y
    public void a(V.a aVar) {
        try {
            this.f2658b.setVisible(false);
        } catch (Exception e2) {
        }
        C.a("Unable to load Ignition Log File.", aVar, this.f2661e.f2649k);
    }

    @Override // com.efiAnalytics.ui.aQ
    public void a() {
        this.f2659c = false;
    }
}
