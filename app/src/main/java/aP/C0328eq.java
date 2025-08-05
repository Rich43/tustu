package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: aP.eq, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/eq.class */
class C0328eq implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3324a;

    C0328eq(C0308dx c0308dx) {
        this.f3324a = c0308dx;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f3324a.f3264c.f(G.T.a().c(), ((gS) actionEvent.getSource()).getName());
    }
}
