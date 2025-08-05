package aP;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* loaded from: TunerStudioMS.jar:aP/jo.class */
class jo extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ iX f3801a;

    jo(iX iXVar) {
        this.f3801a = iXVar;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == 3) {
            a(mouseEvent.getX(), mouseEvent.getY());
        }
    }

    public void a(int i2, int i3) {
        int width = i2 / (this.f3801a.f3700g.getWidth() / this.f3801a.f3700g.getColumnModel().getColumnCount());
        int rowHeight = i3 / this.f3801a.f3700g.getRowHeight();
        if (this.f3801a.f3700g.isCellSelected(rowHeight, width)) {
            return;
        }
        this.f3801a.f3700g.changeSelection(rowHeight, width, false, false);
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == 3) {
            this.f3801a.a(mouseEvent.getX(), mouseEvent.getY());
        }
    }
}
