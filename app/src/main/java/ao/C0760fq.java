package ao;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/* renamed from: ao.fq, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/fq.class */
class C0760fq implements ComponentListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0759fp f5823a;

    C0760fq(C0759fp c0759fp) {
        this.f5823a = c0759fp;
    }

    @Override // java.awt.event.ComponentListener
    public void componentResized(ComponentEvent componentEvent) {
        this.f5823a.g();
    }

    @Override // java.awt.event.ComponentListener
    public void componentMoved(ComponentEvent componentEvent) {
        this.f5823a.g();
    }

    @Override // java.awt.event.ComponentListener
    public void componentShown(ComponentEvent componentEvent) {
    }

    @Override // java.awt.event.ComponentListener
    public void componentHidden(ComponentEvent componentEvent) {
    }
}
