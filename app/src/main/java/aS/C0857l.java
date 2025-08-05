package as;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;

/* renamed from: as.l, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:as/l.class */
class C0857l implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ JComboBox f6261a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0855j f6262b;

    C0857l(C0855j c0855j, JComboBox jComboBox) {
        this.f6262b = c0855j;
        this.f6261a = jComboBox;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        h.i.c(h.i.f12322ao, "" + this.f6261a.getSelectedIndex());
    }
}
