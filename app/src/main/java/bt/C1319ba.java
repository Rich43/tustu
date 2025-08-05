package bt;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* renamed from: bt.ba, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/ba.class */
class C1319ba extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aZ f8991a;

    C1319ba(aZ aZVar) {
        this.f8991a = aZVar;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
        if (this.f8991a.f8803a == null || this.f8991a.f8803a.isEmpty()) {
            return;
        }
        com.efiAnalytics.ui.aN.a(this.f8991a.f8803a);
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseEntered(MouseEvent mouseEvent) {
        if (this.f8991a.f8803a == null || this.f8991a.f8803a.isEmpty()) {
            return;
        }
        this.f8991a.setCursor(Cursor.getPredefinedCursor(12));
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseExited(MouseEvent mouseEvent) {
        if (this.f8991a.f8803a == null || this.f8991a.f8803a.isEmpty()) {
            return;
        }
        this.f8991a.setCursor(Cursor.getDefaultCursor());
    }
}
