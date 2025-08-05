package ao;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* renamed from: ao.ao, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/ao.class */
class C0624ao extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0620ak f5228a;

    C0624ao(C0620ak c0620ak) {
        this.f5228a = c0620ak;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseReleased(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == 3 || mouseEvent.isPopupTrigger()) {
            this.f5228a.a(mouseEvent.getX(), mouseEvent.getY());
        } else {
            this.f5228a.l();
        }
    }
}
