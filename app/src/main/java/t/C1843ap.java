package t;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* renamed from: t.ap, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:t/ap.class */
class C1843ap extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1837aj f13797a;

    C1843ap(C1837aj c1837aj) {
        this.f13797a = c1837aj;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
        this.f13797a.f13789c.a(((C1845ar) mouseEvent.getSource()).f13799a);
    }
}
