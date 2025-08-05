package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:aP/gE.class */
class gE implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    G.R f3406a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0308dx f3407b;

    gE(C0308dx c0308dx, G.R r2) {
        this.f3407b = c0308dx;
        this.f3406a = r2;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f3407b.f3264c.a(this.f3406a, actionEvent.getActionCommand(), this.f3407b.f3270h);
    }
}
