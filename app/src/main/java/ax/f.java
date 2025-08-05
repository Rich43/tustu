package aX;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;

/* loaded from: TunerStudioMS.jar:aX/f.class */
class f implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ JDialog f4009a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ c f4010b;

    f(c cVar, JDialog jDialog) {
        this.f4010b = cVar;
        this.f4009a = jDialog;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        while (!this.f4010b.c()) {
            this.f4010b.a();
        }
        this.f4009a.dispose();
    }
}
