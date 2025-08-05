package aP;

import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:aP/eH.class */
class eH implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3288a;

    eH(C0308dx c0308dx) {
        this.f3288a = c0308dx;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) throws HeadlessException {
        this.f3288a.f3264c.j((Frame) this.f3288a.f3270h);
    }
}
