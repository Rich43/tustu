package com.efiAnalytics.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/aT.class */
public class aT extends MouseAdapter implements MouseListener, MouseMotionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aS f10800a;

    public aT(aS aSVar) {
        this.f10800a = aSVar;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent mouseEvent) {
        if (this.f10800a.k().contains(mouseEvent.getX(), mouseEvent.getY())) {
            this.f10800a.f10791t = mouseEvent.getX();
            this.f10800a.f10792u = mouseEvent.getY();
            this.f10800a.repaint();
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
    public void mouseMoved(MouseEvent mouseEvent) {
        if (this.f10800a.k().contains(mouseEvent.getX(), mouseEvent.getY())) {
            this.f10800a.f10791t = mouseEvent.getX();
            this.f10800a.f10792u = mouseEvent.getY();
            this.f10800a.repaint();
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseEntered(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseExited(MouseEvent mouseEvent) {
    }
}
