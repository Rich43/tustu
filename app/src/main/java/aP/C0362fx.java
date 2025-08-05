package aP;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* renamed from: aP.fx, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/fx.class */
class C0362fx extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3394a;

    C0362fx(C0308dx c0308dx) {
        this.f3394a = c0308dx;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() != 3) {
            new Thread(new RunnableC0363fy(this)).start();
        } else {
            C0413hu.a(this.f3394a.f3270h);
            mouseEvent.consume();
        }
    }
}
