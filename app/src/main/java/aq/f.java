package aq;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;

/* loaded from: TunerStudioMS.jar:aq/f.class */
class f implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ JDialog f6212a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0833a f6213b;

    f(C0833a c0833a, JDialog jDialog) {
        this.f6213b = c0833a;
        this.f6212a = jDialog;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (this.f6213b.g()) {
            this.f6212a.dispose();
        }
    }
}
