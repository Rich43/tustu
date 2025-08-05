package com.efiAnalytics.tunerStudio.panels;

import com.efiAnalytics.ui.aQ;
import com.efiAnalytics.ui.eB;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/C.class */
class C implements W.Y, aQ {

    /* renamed from: a, reason: collision with root package name */
    long f9931a = 0;

    /* renamed from: b, reason: collision with root package name */
    eB f9932b = null;

    /* renamed from: c, reason: collision with root package name */
    boolean f9933c = true;

    /* renamed from: d, reason: collision with root package name */
    final aQ f9934d = this;

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ C1466o f9935e;

    C(C1466o c1466o) {
        this.f9935e = c1466o;
    }

    @Override // W.Y
    public void a(ArrayList arrayList, long j2) {
        try {
            this.f9931a = j2;
            SwingUtilities.invokeAndWait(new D(this));
        } catch (InterruptedException e2) {
            Logger.getLogger(C1466o.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        } catch (InvocationTargetException e3) {
            Logger.getLogger(C1466o.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
        }
    }

    @Override // W.Y
    public boolean a(long j2) {
        this.f9932b.a(j2 / this.f9931a);
        return this.f9933c;
    }

    @Override // W.Y
    public void a(ArrayList arrayList) {
        this.f9935e.f10137k.a(arrayList);
        this.f9932b.setVisible(false);
    }

    @Override // W.Y
    public void a(V.a aVar) {
        try {
            this.f9932b.setVisible(false);
        } catch (Exception e2) {
        }
        bH.C.a("Unable to load Ignition Log File.", aVar, this.f9935e.f10137k);
    }

    @Override // com.efiAnalytics.ui.aQ
    public void a() {
        this.f9933c = false;
    }
}
