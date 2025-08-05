package t;

import G.C0113cs;
import com.efiAnalytics.ui.bV;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:t/bj.class */
class bj implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bi f13843a;

    bj(bi biVar) {
        this.f13843a = biVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        JComboBox jComboBox = (JComboBox) actionEvent.getSource();
        if (this.f13843a.f13841c) {
            return;
        }
        if (this.f13843a.c().a().size() == 1 || bV.a(C1818g.b("There is more than 1 gauge selected.") + "\n" + C1818g.b("Are you sure you want to set them all to Controller") + ": " + jComboBox.getSelectedItem() + "?", (Component) jComboBox, true)) {
            String string = jComboBox.getSelectedItem().toString();
            if (jComboBox.getSelectedItem() instanceof bm) {
                string = ((bm) jComboBox.getSelectedItem()).a();
            }
            if (string.equals(C0113cs.f1154a)) {
                this.f13843a.d();
            } else {
                this.f13843a.a(this.f13843a.a(string));
            }
            this.f13843a.c().c(string);
        }
    }
}
