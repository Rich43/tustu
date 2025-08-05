package com.efiAnalytics.ui;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.SwingUtilities;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/W.class */
class W extends FocusAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1705w f10709a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ S f10710b;

    W(S s2, C1705w c1705w) {
        this.f10710b = s2;
        this.f10709a = c1705w;
    }

    @Override // java.awt.event.FocusAdapter, java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
        SwingUtilities.invokeLater(new X(this, focusEvent));
    }
}
