package com.efiAnalytics.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* renamed from: com.efiAnalytics.ui.ff, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/ff.class */
class C1675ff extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1672fc f11667a;

    C1675ff(C1672fc c1672fc) {
        this.f11667a = c1672fc;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof C1676fg) {
        }
        this.f11667a.repaint();
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseEntered(MouseEvent mouseEvent) {
        if (this.f11667a.f11659a >= 0 && (mouseEvent.getSource() instanceof C1676fg)) {
            C1676fg c1676fg = (C1676fg) mouseEvent.getSource();
            this.f11667a.f11659a = c1676fg.f11668a;
            this.f11667a.f11660b = c1676fg.f11669b;
        }
        this.f11667a.repaint();
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseExited(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof C1676fg) {
            C1676fg c1676fg = (C1676fg) mouseEvent.getSource();
            this.f11667a.f11659a = c1676fg.f11668a;
            this.f11667a.f11660b = c1676fg.f11669b;
        }
        this.f11667a.repaint();
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseReleased(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof C1676fg) {
        }
        bH.C.c("Selected: " + (this.f11667a.f11660b + 1) + " x " + (this.f11667a.f11659a + 1));
        this.f11667a.b(this.f11667a.f11659a + 1, this.f11667a.f11660b + 1);
        this.f11667a.dispose();
    }
}
