package bt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;

/* renamed from: bt.bc, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/bc.class */
class C1321bc implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1320bb f9005a;

    C1321bc(C1320bb c1320bb) {
        this.f9005a = c1320bb;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        JCheckBox jCheckBox = (JCheckBox) actionEvent.getSource();
        this.f9005a.f9002j.a(C1320bb.f9004m, "" + jCheckBox.isSelected());
        this.f9005a.a(jCheckBox.isSelected());
    }
}
