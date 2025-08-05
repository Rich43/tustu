package com.efiAnalytics.ui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/* renamed from: com.efiAnalytics.ui.g, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/g.class */
class C1689g implements FocusListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1642e f11718a;

    C1689g(C1642e c1642e) {
        this.f11718a = c1642e;
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        this.f11718a.repaint();
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
        this.f11718a.repaint();
    }
}
