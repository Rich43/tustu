package com.efiAnalytics.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/fC.class */
class fC implements MouseListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ fB f11629a;

    fC(fB fBVar) {
        this.f11629a = fBVar;
    }

    @Override // java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getY() > this.f11629a.f11624c && mouseEvent.getY() < 2 * this.f11629a.f11624c) {
            this.f11629a.b();
        } else {
            if (mouseEvent.getY() <= 2 * this.f11629a.f11624c || mouseEvent.getY() >= 3.5d * this.f11629a.f11624c) {
                return;
            }
            this.f11629a.c();
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
