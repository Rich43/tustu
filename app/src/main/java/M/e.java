package m;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/* loaded from: TunerStudioMS.jar:m/e.class */
class e implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ JButton f12915a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ d f12916b;

    e(d dVar, JButton jButton) {
        this.f12916b = dVar;
        this.f12915a = jButton;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f12916b.a(this.f12915a, 0, this.f12915a.getHeight());
    }
}
