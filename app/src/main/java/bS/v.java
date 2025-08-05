package bs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;

/* loaded from: TunerStudioMS.jar:bs/v.class */
class v implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ k f8632a;

    v(k kVar) {
        this.f8632a = kVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f8632a.b((JComponent) actionEvent.getSource());
    }
}
