package com.efiAnalytics.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/aL.class */
class aL extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ BinTableView f10747a;

    aL(BinTableView binTableView) {
        this.f10747a = binTableView;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == 3) {
            this.f10747a.a(mouseEvent.getX(), mouseEvent.getY());
        }
        this.f10747a.f10665r = true;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseReleased(MouseEvent mouseEvent) {
        this.f10747a.f10665r = false;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == 3) {
            this.f10747a.b(mouseEvent.getX(), mouseEvent.getY());
        }
        this.f10747a.O();
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseWheelListener
    public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
        if (BinTableView.f10656U && BinTableView.W()) {
            if (mouseWheelEvent.getWheelRotation() > 0) {
                this.f10747a.o();
            } else {
                this.f10747a.n();
            }
        }
        this.f10747a.O();
    }
}
