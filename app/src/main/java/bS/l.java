package bs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JToggleButton;

/* loaded from: TunerStudioMS.jar:bs/l.class */
class l implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ k f8622a;

    l(k kVar) {
        this.f8622a = kVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f8622a.a(((JToggleButton) actionEvent.getSource()).isSelected());
    }
}
