package t;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;

/* loaded from: TunerStudioMS.jar:t/aB.class */
class aB implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aA f13719a;

    aB(aA aAVar) {
        this.f13719a = aAVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        JComboBox jComboBox = (JComboBox) actionEvent.getSource();
        if (this.f13719a.f13718d) {
            return;
        }
        if (jComboBox.getSelectedItem().equals(this.f13719a.f13717c)) {
            this.f13719a.c().a("");
        } else {
            this.f13719a.c().a((String) jComboBox.getSelectedItem());
        }
    }
}
