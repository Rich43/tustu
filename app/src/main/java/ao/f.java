package aO;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/* loaded from: TunerStudioMS.jar:aO/f.class */
class f implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ a f2656a;

    f(a aVar) {
        this.f2656a = aVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f2656a.f2649k.a(((JCheckBoxMenuItem) actionEvent.getSource()).getState());
        this.f2656a.f2649k.e();
    }
}
