package by;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JList;

/* loaded from: TunerStudioMS.jar:by/g.class */
class g extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ d f9242a;

    g(d dVar) {
        this.f9242a = dVar;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
        JList jList = (JList) mouseEvent.getSource();
        if (mouseEvent.getClickCount() == 2) {
            this.f9242a.c((String) jList.getSelectedValue());
        }
    }
}
