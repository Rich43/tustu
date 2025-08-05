package aV;

import aP.C0338f;
import aP.cZ;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* loaded from: TunerStudioMS.jar:aV/b.class */
class b extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0474a f3940a;

    b(C0474a c0474a) {
        this.f3940a = c0474a;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
        C0338f.a().a(this.f3940a.f3939d, cZ.a().c());
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseEntered(MouseEvent mouseEvent) {
        this.f3940a.setCursor(Cursor.getPredefinedCursor(12));
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseExited(MouseEvent mouseEvent) {
        this.f3940a.setCursor(Cursor.getPredefinedCursor(0));
    }
}
