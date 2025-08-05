package com.efiAnalytics.apps.ts.tuningViews;

import com.efiAnalytics.ui.bV;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: com.efiAnalytics.apps.ts.tuningViews.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/c.class */
class C1430c implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1429b f9774a;

    C1430c(C1429b c1429b) {
        this.f9774a = c1429b;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (!(this.f9774a.f9768c.getSelectedItem() instanceof C1438k)) {
            bH.C.c("Didn't show TuningView");
            return;
        }
        C1438k c1438k = (C1438k) this.f9774a.f9768c.getSelectedItem();
        try {
            this.f9774a.a(c1438k.d());
        } catch (V.a e2) {
            bV.d(e2.getMessage(), this.f9774a.f9768c);
            Logger.getLogger(C1429b.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        this.f9774a.f9766a = c1438k.a();
    }
}
