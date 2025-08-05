package bt;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* renamed from: bt.m, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/m.class */
class C1354m extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1353l f9105a;

    C1354m(C1353l c1353l) {
        this.f9105a = c1353l;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
        if (this.f9105a.isEnabled()) {
            this.f9105a.a();
        }
    }
}
