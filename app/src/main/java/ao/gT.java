package ao;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* loaded from: TunerStudioMS.jar:ao/gT.class */
class gT extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ gS f5963a;

    gT(gS gSVar) {
        this.f5963a = gSVar;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseEntered(MouseEvent mouseEvent) {
        if (System.currentTimeMillis() - 700 <= this.f5963a.f5954j || mouseEvent.getModifiers() != 0) {
            return;
        }
        this.f5963a.f5954j = System.currentTimeMillis();
        if (this.f5963a.f5955r != null) {
            this.f5963a.f5955r.a();
        }
        this.f5963a.d();
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseExited(MouseEvent mouseEvent) {
        if ((!this.f5963a.f5953i || mouseEvent.getPoint().f12370x >= 5) && mouseEvent.getPoint().f12370x <= this.f5963a.f5959n.getWidth() && mouseEvent.getPoint().f12371y >= 10 && mouseEvent.getPoint().f12371y <= this.f5963a.f5959n.getHeight() - 10) {
            return;
        }
        this.f5963a.f5955r = this.f5963a.f();
        this.f5963a.f5955r.b();
    }
}
