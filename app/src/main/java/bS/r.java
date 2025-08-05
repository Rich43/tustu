package bs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;

/* loaded from: TunerStudioMS.jar:bs/r.class */
class r implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ k f8628a;

    r(k kVar) {
        this.f8628a = kVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (((JCheckBox) actionEvent.getSource()).isSelected()) {
            this.f8628a.f8605b.b(true);
            this.f8628a.f8617n.a("extendBeyondData", Boolean.toString(true));
        } else {
            this.f8628a.f8605b.b(false);
            this.f8628a.f8617n.a("extendBeyondData", Boolean.toString(false));
        }
    }
}
