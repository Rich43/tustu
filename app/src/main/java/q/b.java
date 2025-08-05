package Q;

import com.efiAnalytics.ui.bV;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JRadioButton;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:Q/b.class */
class b implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ a f1776a;

    b(a aVar) {
        this.f1776a = aVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        String text = ((JRadioButton) actionEvent.getSource()).getText();
        this.f1776a.f1774j = Integer.parseInt(text);
        if (this.f1776a.f1774j < 3) {
            bV.d(C1818g.b(C1818g.b("Please explain what you have had issues with below so they can be addressed.")), this.f1776a.f1766b);
        }
    }
}
