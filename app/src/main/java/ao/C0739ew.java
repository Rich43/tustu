package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;

/* renamed from: ao.ew, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/ew.class */
class C0739ew implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0737eu f5692a;

    C0739ew(C0737eu c0737eu) {
        this.f5692a = c0737eu;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        JComboBox jComboBox = (JComboBox) actionEvent.getSource();
        this.f5692a.f5674j = jComboBox.getSelectedItem().toString();
        h.i.c(h.i.f12308ac, this.f5692a.f5674j);
    }
}
