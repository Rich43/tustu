package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;

/* renamed from: aP.ad, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/ad.class */
class C0208ad implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0207ac f2906a;

    C0208ad(C0207ac c0207ac) {
        this.f2906a = c0207ac;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) throws IllegalArgumentException {
        JComboBox jComboBox = (JComboBox) actionEvent.getSource();
        try {
            this.f2906a.b((String) jComboBox.getSelectedItem());
        } catch (V.a e2) {
            com.efiAnalytics.ui.bV.d(e2.getMessage(), jComboBox);
        }
    }
}
