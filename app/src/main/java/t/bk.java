package t;

import com.efiAnalytics.ui.bV;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:t/bk.class */
class bk implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bi f13844a;

    bk(bi biVar) {
        this.f13844a = biVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        JComboBox jComboBox = (JComboBox) actionEvent.getSource();
        if (this.f13844a.f13841c) {
            return;
        }
        if (this.f13844a.c().a().size() == 1 || bV.a(C1818g.b("There is more than 1 gauge selected.") + "\n" + C1818g.b("Are you sure you want to set them all to Output Channel") + ": " + jComboBox.getSelectedItem() + "?", (Component) jComboBox, true)) {
            this.f13844a.c().b((String) jComboBox.getSelectedItem());
        }
    }
}
