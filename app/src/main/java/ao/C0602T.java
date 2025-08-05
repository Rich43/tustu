package ao;

import W.C0184j;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: ao.T, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/T.class */
final class C0602T implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0184j f5109a;

    C0602T(C0184j c0184j) {
        this.f5109a = c0184j;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (((JCheckBoxMenuItem) actionEvent.getSource()).isSelected()) {
            C0804hg.a().a(this.f5109a, this.f5109a.s());
        } else {
            C0804hg.a().a(this.f5109a);
        }
    }
}
