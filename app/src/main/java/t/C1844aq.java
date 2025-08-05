package t;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* renamed from: t.aq, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:t/aq.class */
class C1844aq extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1837aj f13798a;

    C1844aq(C1837aj c1837aj) {
        this.f13798a = c1837aj;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
        this.f13798a.f13789c.a(((C1845ar) mouseEvent.getSource()).f13799a);
    }
}
