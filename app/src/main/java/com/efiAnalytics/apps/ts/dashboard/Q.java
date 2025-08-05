package com.efiAnalytics.apps.ts.dashboard;

import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/Q.class */
class Q extends MouseAdapter implements MouseMotionListener {

    /* renamed from: b, reason: collision with root package name */
    private Rectangle f9390b = null;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1425x f9391a;

    Q(C1425x c1425x) {
        this.f9391a = c1425x;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) throws HeadlessException {
        if (this.f9391a.isEnabled()) {
            Component componentA = a(mouseEvent);
            if (componentA instanceof AbstractC1420s) {
                if (mouseEvent.getButton() != 3 && !this.f9391a.L()) {
                    this.f9391a.a((AbstractC1420s) componentA, (mouseEvent.getModifiers() & 2) != 2 && mouseEvent.getButton() == 1);
                }
            } else if (mouseEvent.getButton() != 3 && this.f9391a.L()) {
                this.f9391a.N();
                this.f9390b = new Rectangle();
                this.f9390b.f12372x = mouseEvent.getX();
                this.f9390b.f12373y = mouseEvent.getY();
            }
            if (mouseEvent.getClickCount() == 2) {
                this.f9391a.t();
            } else if (mouseEvent.getButton() == 3) {
                this.f9391a.a((Component) mouseEvent.getSource(), mouseEvent.getX(), mouseEvent.getY());
                mouseEvent.consume();
            } else {
                this.f9391a.requestFocus();
            }
            this.f9391a.repaint();
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseReleased(MouseEvent mouseEvent) {
        if (a() != null) {
            a().width = Math.abs(mouseEvent.getX() - a().f12372x);
            a().height = Math.abs(mouseEvent.getY() - a().f12373y);
            a().f12372x = a().f12372x < mouseEvent.getX() ? a().f12372x : mouseEvent.getX();
            a().f12373y = a().f12373y < mouseEvent.getY() ? a().f12373y : mouseEvent.getY();
            this.f9391a.a(a());
            this.f9390b = null;
            this.f9391a.repaint();
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent mouseEvent) {
        if (a() != null) {
            a().width = mouseEvent.getX() - a().f12372x;
            a().height = mouseEvent.getY() - a().f12373y;
            this.f9391a.repaint();
        }
    }

    public Rectangle a() {
        return this.f9390b;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
    public void mouseMoved(MouseEvent mouseEvent) {
    }

    private Component a(MouseEvent mouseEvent) {
        if (!(mouseEvent.getSource() instanceof AbstractC1420s)) {
            return (Component) mouseEvent.getSource();
        }
        AbstractC1420s abstractC1420s = (AbstractC1420s) mouseEvent.getSource();
        AbstractC1420s abstractC1420sA = this.f9391a.a(abstractC1420s.getX() + mouseEvent.getX(), abstractC1420s.getY() + mouseEvent.getY());
        return abstractC1420sA == null ? abstractC1420s : abstractC1420sA;
    }
}
