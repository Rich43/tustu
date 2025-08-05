package aX;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;

/* loaded from: TunerStudioMS.jar:aX/g.class */
class g implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ JDialog f4011a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ c f4012b;

    g(c cVar, JDialog jDialog) {
        this.f4012b = cVar;
        this.f4011a = jDialog;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f4011a.dispose();
    }
}
