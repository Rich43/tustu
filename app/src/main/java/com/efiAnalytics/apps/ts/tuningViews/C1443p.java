package com.efiAnalytics.apps.ts.tuningViews;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/* renamed from: com.efiAnalytics.apps.ts.tuningViews.p, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/p.class */
class C1443p extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ List f9801a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1441n f9802b;

    C1443p(C1441n c1441n, List list) {
        this.f9802b = c1441n;
        this.f9801a = list;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.f9801a);
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            try {
                SwingUtilities.invokeAndWait(new RunnableC1449v(this.f9802b, this.f9801a, (C1438k) arrayList.get(i2)));
                sleep(20L);
            } catch (InterruptedException e2) {
                Logger.getLogger(C1441n.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            } catch (InvocationTargetException e3) {
                Logger.getLogger(C1441n.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            }
        }
    }
}
