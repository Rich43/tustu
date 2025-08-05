package com.efiAnalytics.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/dN.class */
class dN extends MouseAdapter implements MouseMotionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ dM f11351a;

    dN(dM dMVar) {
        this.f11351a = dMVar;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseReleased(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
        if (this.f11351a.f11349f) {
            this.f11351a.a(mouseEvent.getX());
            this.f11351a.repaint();
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
    public void mouseMoved(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent mouseEvent) {
        if (this.f11351a.f11349f) {
            this.f11351a.a(mouseEvent.getX());
            this.f11351a.repaint();
        }
    }
}
