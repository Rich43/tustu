package com.efiAnalytics.ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/bO.class */
class bO implements MouseListener, MouseMotionListener {

    /* renamed from: a, reason: collision with root package name */
    bP f10959a = null;

    /* renamed from: b, reason: collision with root package name */
    Point f10960b = null;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ bN f10961c;

    bO(bN bNVar) {
        this.f10961c = bNVar;
    }

    @Override // java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
        if (this.f10961c.isEnabled()) {
            this.f10961c.requestFocus();
            for (int i2 = 0; i2 < this.f10961c.f10890I.size(); i2++) {
                bQ bQVar = (bQ) this.f10961c.f10890I.get(i2);
                for (int i3 = 0; i3 < bQVar.size(); i3++) {
                    bP bPVar = (bP) bQVar.get(i3);
                    if (Math.abs(bPVar.d() - mouseEvent.getX()) < this.f10961c.f10906b / 3 && Math.abs(bPVar.f() - mouseEvent.getY()) < this.f10961c.f10906b / 3) {
                        this.f10959a = new bP(this.f10961c, i2, i3);
                        this.f10959a.c(mouseEvent.getY());
                        this.f10959a.b(mouseEvent.getX());
                        this.f10961c.b(i2, i3);
                        this.f10961c.o();
                        this.f10961c.S();
                        this.f10961c.repaint();
                        return;
                    }
                }
            }
            if (!this.f10961c.f10907c.isEmpty()) {
                this.f10961c.f10907c.clear();
                this.f10961c.S();
                this.f10961c.o();
                this.f10961c.repaint();
            }
            if (this.f10961c.f10943am) {
                this.f10960b = mouseEvent.getPoint();
            }
        }
    }

    @Override // java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent mouseEvent) {
        if (this.f10961c.isEnabled()) {
            if (this.f10959a == null) {
                if (this.f10960b != null) {
                    int iMin = Math.min(this.f10960b.f12370x, mouseEvent.getPoint().f12370x);
                    int iMin2 = Math.min(this.f10960b.f12371y, mouseEvent.getPoint().f12371y);
                    int iAbs = Math.abs(mouseEvent.getPoint().f12370x - this.f10960b.f12370x);
                    int iAbs2 = Math.abs(mouseEvent.getPoint().f12371y - this.f10960b.f12371y);
                    this.f10961c.f10950D = new Rectangle(iMin, iMin2, iAbs, iAbs2);
                    this.f10961c.repaint();
                    return;
                }
                return;
            }
            bP bPVar = (bP) this.f10961c.m(this.f10961c.x()).get(this.f10961c.w());
            if (!this.f10961c.l()) {
                int iB = this.f10961c.b(mouseEvent.getX(), this.f10961c.x(), this.f10961c.w());
                if (iB == bN.f10944x) {
                    bPVar.b(mouseEvent.getX());
                } else if (iB == bN.f10948B) {
                    bPVar.a(this.f10961c.h());
                } else if (iB == bN.f10947A) {
                    bPVar.a(this.f10961c.i());
                }
            }
            int iC = this.f10961c.c(mouseEvent.getY(), this.f10961c.x(), this.f10961c.w());
            if (iC == bN.f10944x) {
                bPVar.c(mouseEvent.getY());
            } else if (iC == bN.f10945y) {
                bPVar.b(this.f10961c.d(bPVar.a(), bPVar.b()));
            } else if (iC == bN.f10946z) {
                bPVar.b(this.f10961c.k());
            } else if (iC == bN.f10949C) {
                bH.C.c("Range Out");
            }
            this.f10961c.o();
            this.f10961c.repaint();
        }
    }

    @Override // java.awt.event.MouseListener
    public void mouseReleased(MouseEvent mouseEvent) {
        if (this.f10961c.isEnabled()) {
            if (this.f10959a != null) {
                this.f10961c.f(this.f10961c.x(), this.f10961c.w());
            }
            this.f10959a = null;
            if (this.f10961c.f10950D != null) {
                this.f10961c.a(this.f10961c.f10950D);
                this.f10961c.f10950D = null;
            }
            this.f10960b = null;
        }
    }

    @Override // java.awt.event.MouseMotionListener
    public void mouseMoved(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseListener
    public void mouseEntered(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseListener
    public void mouseExited(MouseEvent mouseEvent) {
    }
}
