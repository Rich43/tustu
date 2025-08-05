package ao;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* renamed from: ao.bh, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/bh.class */
class C0644bh extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0643bg f5402a;

    C0644bh(C0643bg c0643bg) {
        this.f5402a = c0643bg;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getX() <= this.f5402a.getWidth() - this.f5402a.f5400b || mouseEvent.getY() >= this.f5402a.f5401c) {
            return;
        }
        this.f5402a.a(mouseEvent.getX(), mouseEvent.getY());
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
    }
}
