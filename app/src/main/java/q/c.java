package Q;

import com.efiAnalytics.ui.InterfaceC1632dq;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;

/* loaded from: TunerStudioMS.jar:Q/c.class */
class c implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    InterfaceC1632dq f1777a;

    /* renamed from: b, reason: collision with root package name */
    JDialog f1778b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ a f1779c;

    c(a aVar, InterfaceC1632dq interfaceC1632dq, JDialog jDialog) {
        this.f1779c = aVar;
        this.f1777a = null;
        this.f1778b = null;
        this.f1777a = interfaceC1632dq;
        this.f1778b = jDialog;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (this.f1777a == null) {
            this.f1778b.dispose();
        } else if (this.f1777a.b()) {
            this.f1778b.dispose();
        }
    }
}
