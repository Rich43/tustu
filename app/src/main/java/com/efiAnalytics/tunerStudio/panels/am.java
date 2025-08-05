package com.efiAnalytics.tunerStudio.panels;

import com.efiAnalytics.ui.eJ;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/am.class */
class am extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    int f10073a = eJ.a(4);

    /* renamed from: b, reason: collision with root package name */
    boolean f10074b = false;

    /* renamed from: c, reason: collision with root package name */
    boolean f10075c = false;

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ TriggerLoggerPanel f10076d;

    am(TriggerLoggerPanel triggerLoggerPanel) {
        this.f10076d = triggerLoggerPanel;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
        if (mouseEvent.isPopupTrigger()) {
            this.f10076d.a(mouseEvent.getX(), mouseEvent.getY());
        }
        if (mouseEvent.getButton() == 1) {
            if (!this.f10076d.f9982f.v()) {
                this.f10076d.f9982f.k(mouseEvent.getX());
                this.f10075c = true;
            } else if (Math.abs(mouseEvent.getX() - this.f10076d.f9982f.w()) < this.f10073a) {
                this.f10074b = true;
            } else if (Math.abs(mouseEvent.getX() - this.f10076d.f9982f.x()) < this.f10073a) {
                this.f10075c = true;
            } else {
                this.f10076d.f9982f.u();
            }
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseReleased(MouseEvent mouseEvent) {
        if (mouseEvent.isPopupTrigger()) {
            this.f10076d.a(mouseEvent.getX(), mouseEvent.getY());
            return;
        }
        this.f10074b = false;
        this.f10075c = false;
        this.f10076d.f9982f.repaint();
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent mouseEvent) {
        if (this.f10074b) {
            this.f10076d.f9982f.k(mouseEvent.getX());
            this.f10076d.f9982f.repaint();
        } else {
            if (!this.f10075c || mouseEvent.getX() <= this.f10076d.f9982f.w()) {
                return;
            }
            this.f10076d.f9982f.l(mouseEvent.getX());
            this.f10076d.f9982f.repaint();
        }
    }
}
