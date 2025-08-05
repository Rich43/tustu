package com.efiAnalytics.ui;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/* renamed from: com.efiAnalytics.ui.ch, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/ch.class */
class C1597ch implements MouseListener, MouseMotionListener {

    /* renamed from: a, reason: collision with root package name */
    int f11257a = 0;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1596cg f11258b;

    C1597ch(C1596cg c1596cg) {
        this.f11258b = c1596cg;
    }

    @Override // java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent mouseEvent) {
        if (this.f11257a == 1) {
            this.f11258b.d(this.f11258b.a(mouseEvent.getX(), mouseEvent.getY()));
        } else if (this.f11257a == 2) {
            this.f11258b.c(this.f11258b.a(mouseEvent.getX(), mouseEvent.getY()));
        }
    }

    @Override // java.awt.event.MouseMotionListener
    public void mouseMoved(MouseEvent mouseEvent) {
        if (this.f11258b.f11255m.contains(mouseEvent.getX(), mouseEvent.getY())) {
            this.f11258b.setCursor(Cursor.getPredefinedCursor(10));
        } else if (this.f11258b.f11256n.contains(mouseEvent.getX(), mouseEvent.getY())) {
            this.f11258b.setCursor(Cursor.getPredefinedCursor(11));
        } else {
            this.f11258b.setCursor(Cursor.getDefaultCursor());
        }
    }

    @Override // java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
        if (this.f11258b.f11255m.contains(mouseEvent.getX(), mouseEvent.getY())) {
            this.f11257a = 1;
        } else if (this.f11258b.f11256n.contains(mouseEvent.getX(), mouseEvent.getY())) {
            this.f11257a = 2;
        } else {
            this.f11257a = 0;
        }
    }

    @Override // java.awt.event.MouseListener
    public void mouseReleased(MouseEvent mouseEvent) {
        this.f11257a = 0;
    }

    @Override // java.awt.event.MouseListener
    public void mouseEntered(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseListener
    public void mouseExited(MouseEvent mouseEvent) {
    }
}
