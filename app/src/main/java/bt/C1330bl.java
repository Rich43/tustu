package bt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JRadioButton;

/* renamed from: bt.bl, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/bl.class */
class C1330bl implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1328bj f9030a;

    C1330bl(C1328bj c1328bj) {
        this.f9030a = c1328bj;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f9030a.b(((JRadioButton) actionEvent.getSource()).getActionCommand());
    }
}
