package bs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/* renamed from: bs.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bs/b.class */
class C1269b implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1268a f8573a;

    C1269b(C1268a c1268a) {
        this.f8573a = c1268a;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f8573a.a((JButton) actionEvent.getSource());
    }
}
