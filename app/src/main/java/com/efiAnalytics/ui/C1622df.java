package com.efiAnalytics.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* renamed from: com.efiAnalytics.ui.df, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/df.class */
class C1622df extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1621de f11416a;

    C1622df(C1621de c1621de) {
        this.f11416a = c1621de;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseEntered(MouseEvent mouseEvent) {
        this.f11416a.repaint();
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseExited(MouseEvent mouseEvent) {
        this.f11416a.repaint();
    }
}
