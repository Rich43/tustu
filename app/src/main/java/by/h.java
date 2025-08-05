package by;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JList;

/* loaded from: TunerStudioMS.jar:by/h.class */
class h extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ d f9243a;

    h(d dVar) {
        this.f9243a = dVar;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
        JList jList = (JList) mouseEvent.getSource();
        if (mouseEvent.getClickCount() == 2) {
            this.f9243a.a((k) jList.getSelectedValue());
        }
    }
}
