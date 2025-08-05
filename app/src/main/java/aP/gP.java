package aP;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import s.C1813b;
import s.InterfaceC1817f;

/* loaded from: TunerStudioMS.jar:aP/gP.class */
class gP extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    InterfaceC1817f f3432a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0308dx f3433b;

    public gP(C0308dx c0308dx, InterfaceC1817f interfaceC1817f) {
        this.f3433b = c0308dx;
        this.f3432a = null;
        this.f3432a = interfaceC1817f;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == 3) {
            mouseEvent.consume();
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == 3) {
            a();
            mouseEvent.consume();
        }
    }

    private void a() {
        C1813b.a(this.f3432a);
    }
}
