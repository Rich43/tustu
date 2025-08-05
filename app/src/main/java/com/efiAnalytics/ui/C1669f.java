package com.efiAnalytics.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* renamed from: com.efiAnalytics.ui.f, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/f.class */
class C1669f extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1642e f11618a;

    C1669f(C1642e c1642e) {
        this.f11618a = c1642e;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseEntered(MouseEvent mouseEvent) {
        this.f11618a.repaint();
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseExited(MouseEvent mouseEvent) {
        this.f11618a.repaint();
    }
}
