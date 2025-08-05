package com.efiAnalytics.ui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/* renamed from: com.efiAnalytics.ui.dg, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/dg.class */
class C1623dg implements FocusListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1621de f11417a;

    C1623dg(C1621de c1621de) {
        this.f11417a = c1621de;
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        this.f11417a.repaint();
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
        this.f11417a.repaint();
    }
}
