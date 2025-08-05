package com.efiAnalytics.apps.ts.dashboard;

import d.InterfaceC1712d;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import javax.swing.TransferHandler;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/N.class */
class N extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    InterfaceC1712d f9380a = null;

    /* renamed from: b, reason: collision with root package name */
    long f9381b = 0;

    /* renamed from: c, reason: collision with root package name */
    int f9382c = 2000;

    /* renamed from: d, reason: collision with root package name */
    U f9383d = null;

    /* renamed from: e, reason: collision with root package name */
    Timer f9384e = null;

    /* renamed from: f, reason: collision with root package name */
    final /* synthetic */ C1425x f9385f;

    N(C1425x c1425x) {
        this.f9385f = c1425x;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseExited(MouseEvent mouseEvent) {
        if (this.f9384e != null) {
            this.f9384e.cancel();
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() != 1) {
            return;
        }
        TransferHandler.HasGetTransferHandler hasGetTransferHandlerA = a(mouseEvent);
        if (this.f9385f.isEnabled() && !this.f9385f.L() && (hasGetTransferHandlerA instanceof InterfaceC1712d)) {
            this.f9380a = (InterfaceC1712d) hasGetTransferHandlerA;
            this.f9381b = System.currentTimeMillis();
            this.f9384e = new Timer();
            this.f9383d = new U(this.f9385f);
            this.f9383d.a(this.f9380a);
            this.f9384e.schedule(this.f9383d, this.f9382c);
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseReleased(MouseEvent mouseEvent) {
        if (this.f9384e != null) {
            this.f9384e.cancel();
        }
        TransferHandler.HasGetTransferHandler hasGetTransferHandlerA = a(mouseEvent);
        if (this.f9385f.isEnabled() && !this.f9385f.L() && (hasGetTransferHandlerA instanceof InterfaceC1712d) && this.f9380a != null) {
            InterfaceC1712d interfaceC1712d = (InterfaceC1712d) hasGetTransferHandlerA;
            if (interfaceC1712d.equals(this.f9380a) && System.currentTimeMillis() - this.f9381b <= this.f9382c && this.f9385f.a(interfaceC1712d)) {
                mouseEvent.consume();
            }
        }
        this.f9381b = 0L;
        this.f9380a = null;
    }

    private AbstractC1420s a(MouseEvent mouseEvent) {
        AbstractC1420s abstractC1420s = (AbstractC1420s) mouseEvent.getSource();
        AbstractC1420s abstractC1420sA = this.f9385f.a(abstractC1420s.getX() + mouseEvent.getX(), abstractC1420s.getY() + mouseEvent.getY());
        return abstractC1420sA == null ? abstractC1420s : abstractC1420sA;
    }
}
