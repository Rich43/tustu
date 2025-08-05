package com.efiAnalytics.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/cL.class */
class cL implements MouseListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ cK f11076a;

    cL(cK cKVar) {
        this.f11076a = cKVar;
    }

    @Override // java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getX() > this.f11076a.f11071c && mouseEvent.getX() < 2 * this.f11076a.f11071c) {
            this.f11076a.b();
        } else {
            if (mouseEvent.getX() <= 2 * this.f11076a.f11071c || mouseEvent.getX() >= 3.5d * this.f11076a.f11071c) {
                return;
            }
            this.f11076a.c();
        }
    }

    @Override // java.awt.event.MouseListener
    public void mouseEntered(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseListener
    public void mouseExited(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseListener
    public void mouseReleased(MouseEvent mouseEvent) {
    }
}
