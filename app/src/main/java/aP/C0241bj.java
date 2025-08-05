package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: aP.bj, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/bj.class */
class C0241bj implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0240bi f3078a;

    C0241bj(C0240bi c0240bi) {
        this.f3078a = c0240bi;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        C0338f.a().d(((JCheckBoxMenuItem) actionEvent.getSource()).isSelected());
    }
}
