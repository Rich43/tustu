package ao;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* renamed from: ao.s, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/s.class */
class C0821s extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0820r f6167a;

    C0821s(C0820r c0820r) {
        this.f6167a = c0820r;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
        this.f6167a.d();
    }
}
