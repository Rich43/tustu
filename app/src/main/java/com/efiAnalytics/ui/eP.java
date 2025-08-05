package com.efiAnalytics.ui;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/eP.class */
class eP extends MouseAdapter implements MouseMotionListener {

    /* renamed from: a, reason: collision with root package name */
    Point f11526a = null;

    /* renamed from: b, reason: collision with root package name */
    int f11527b = 0;

    /* renamed from: c, reason: collision with root package name */
    int f11528c = 0;

    /* renamed from: d, reason: collision with root package name */
    boolean f11529d = false;

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ eM f11530e;

    eP(eM eMVar) {
        this.f11530e = eMVar;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
        if (this.f11530e.isEnabled()) {
            this.f11526a = new Point(mouseEvent.getX(), mouseEvent.getY());
            this.f11528c = this.f11530e.l();
            this.f11527b = this.f11530e.m();
            eZ[][] eZVarArrB = this.f11530e.f11492a.b();
            for (int i2 = 0; i2 < this.f11530e.f11491j.o(); i2++) {
                for (int i3 = 0; i3 < this.f11530e.f11491j.p(); i3++) {
                    if (eZVarArrB[i3][i2].f11567a + this.f11530e.f11503u > mouseEvent.getX() && eZVarArrB[i3][i2].f11567a - this.f11530e.f11503u < mouseEvent.getX() && eZVarArrB[i3][i2].f11568b + this.f11530e.f11503u > mouseEvent.getY() && eZVarArrB[i3][i2].f11568b - this.f11530e.f11503u < mouseEvent.getY()) {
                        this.f11530e.f(i3);
                        this.f11530e.e(i2);
                        this.f11530e.repaint();
                        this.f11530e.z();
                        this.f11529d = true;
                        return;
                    }
                }
            }
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent mouseEvent) {
        if (this.f11530e.isEnabled()) {
            this.f11530e.n();
            if (this.f11529d) {
                this.f11530e.f11491j.a(this.f11530e.f11497o, this.f11530e.f11496n, this.f11530e.f11492a.a(this.f11530e.f11491j.a(this.f11530e.f11496n), this.f11530e.f11491j.b(this.f11530e.f11497o), mouseEvent.getY()));
                this.f11530e.z();
                this.f11530e.repaint();
                return;
            }
            int x2 = (((mouseEvent.getX() - this.f11526a.f12370x) * 360) / this.f11530e.getWidth()) / 2;
            int y2 = (((mouseEvent.getY() - this.f11526a.f12371y) * 360) / this.f11530e.getHeight()) / 2;
            this.f11530e.c(this.f11528c + x2);
            this.f11530e.d(this.f11527b + y2);
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseReleased(MouseEvent mouseEvent) {
        this.f11526a = null;
        this.f11529d = false;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
    public void mouseMoved(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseWheelListener
    public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
        if (mouseWheelEvent.getWheelRotation() > 0) {
            this.f11530e.k();
        } else {
            this.f11530e.j();
        }
    }
}
