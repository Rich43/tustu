package bh;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/* renamed from: bh.q, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bh/q.class */
class C1157q implements ComponentListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1154n f8128a;

    C1157q(C1154n c1154n) {
        this.f8128a = c1154n;
    }

    @Override // java.awt.event.ComponentListener
    public void componentResized(ComponentEvent componentEvent) {
        if (this.f8128a.getParent().getParent() != null) {
            this.f8128a.getParent().getParent().doLayout();
        }
    }

    @Override // java.awt.event.ComponentListener
    public void componentMoved(ComponentEvent componentEvent) {
    }

    @Override // java.awt.event.ComponentListener
    public void componentShown(ComponentEvent componentEvent) {
    }

    @Override // java.awt.event.ComponentListener
    public void componentHidden(ComponentEvent componentEvent) {
    }
}
