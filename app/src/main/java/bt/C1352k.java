package bt;

import java.awt.HeadlessException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* renamed from: bt.k, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/k.class */
class C1352k extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1351j f9101a;

    C1352k(C1351j c1351j) {
        this.f9101a = c1351j;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) throws HeadlessException {
        if (this.f9101a.isEnabled()) {
            this.f9101a.a();
        }
    }
}
