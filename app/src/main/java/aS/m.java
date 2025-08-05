package as;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;

/* loaded from: TunerStudioMS.jar:as/m.class */
class m implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ JComboBox f6263a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0855j f6264b;

    m(C0855j c0855j, JComboBox jComboBox) {
        this.f6264b = c0855j;
        this.f6263a = jComboBox;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        h.i.c(h.i.f12327at, "" + this.f6263a.getSelectedIndex());
        this.f6264b.f6258a.setEnabled(this.f6263a.getSelectedIndex() == h.i.f12329av);
        this.f6264b.f6259b.setEnabled(this.f6263a.getSelectedIndex() == h.i.f12329av);
    }
}
