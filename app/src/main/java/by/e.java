package by;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* loaded from: TunerStudioMS.jar:by/e.class */
class e extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ d f9240a;

    e(d dVar) {
        this.f9240a = dVar;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) {
            this.f9240a.a();
        }
    }
}
