package com.efiAnalytics.apps.ts.tuningViews;

import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/Q.class */
class Q extends MouseAdapter implements MouseMotionListener {

    /* renamed from: b, reason: collision with root package name */
    private Rectangle f9739b = null;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ J f9740a;

    Q(J j2) {
        this.f9740a = j2;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) throws HeadlessException {
        if (this.f9740a.isEnabled()) {
            Component component = (Component) mouseEvent.getSource();
            if (component instanceof TuneViewComponent) {
                if (mouseEvent.getButton() != 3 && !this.f9740a.B()) {
                    this.f9740a.a((TuneViewComponent) component, (mouseEvent.getModifiers() & 2) != 2 && mouseEvent.getButton() == 1);
                }
            } else if (mouseEvent.getButton() != 3 && this.f9740a.B()) {
                this.f9740a.C();
                this.f9739b = new Rectangle();
                this.f9739b.f12372x = mouseEvent.getX();
                this.f9739b.f12373y = mouseEvent.getY();
            }
            if (mouseEvent.getClickCount() == 2) {
                this.f9740a.t();
            } else if (mouseEvent.getButton() == 3) {
                this.f9740a.a((Component) mouseEvent.getSource(), mouseEvent.getX(), mouseEvent.getY());
            } else {
                this.f9740a.requestFocus();
            }
            this.f9740a.repaint();
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseReleased(MouseEvent mouseEvent) {
        if (a() != null) {
            a().width = Math.abs(mouseEvent.getX() - a().f12372x);
            a().height = Math.abs(mouseEvent.getY() - a().f12373y);
            a().f12372x = a().f12372x < mouseEvent.getX() ? a().f12372x : mouseEvent.getX();
            a().f12373y = a().f12373y < mouseEvent.getY() ? a().f12373y : mouseEvent.getY();
            this.f9740a.a(a());
            this.f9739b = null;
            this.f9740a.repaint();
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent mouseEvent) {
        if (a() != null) {
            a().width = mouseEvent.getX() - a().f12372x;
            a().height = mouseEvent.getY() - a().f12373y;
            this.f9740a.repaint();
        }
    }

    public Rectangle a() {
        return this.f9739b;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
    public void mouseMoved(MouseEvent mouseEvent) {
    }
}
