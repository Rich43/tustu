package com.efiAnalytics.apps.ts.tuningViews;

import java.awt.HeadlessException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/* renamed from: com.efiAnalytics.apps.ts.tuningViews.o, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/o.class */
class C1442o implements ChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1441n f9800a;

    C1442o(C1441n c1441n) {
        this.f9800a = c1441n;
    }

    @Override // javax.swing.event.ChangeListener
    public void stateChanged(ChangeEvent changeEvent) throws HeadlessException {
        if (this.f9800a.getSelectedIndex() != this.f9800a.getTabCount() - 1) {
            this.f9800a.f9789a = this.f9800a.getSelectedIndex();
            this.f9800a.c(this.f9800a.f9789a);
        } else {
            if (this.f9800a.f9789a >= this.f9800a.getTabCount() - 1) {
                this.f9800a.setSelectedIndex(0);
                return;
            }
            this.f9800a.setSelectedIndex(this.f9800a.f9789a);
            if (this.f9800a.f9795f) {
                return;
            }
            this.f9800a.g();
        }
    }
}
