package aq;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;

/* loaded from: TunerStudioMS.jar:aq/g.class */
class g implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ JDialog f6214a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0833a f6215b;

    g(C0833a c0833a, JDialog jDialog) {
        this.f6215b = c0833a;
        this.f6214a = jDialog;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (this.f6215b.h()) {
            this.f6214a.dispose();
        }
    }
}
