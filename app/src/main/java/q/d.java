package Q;

import com.efiAnalytics.ui.InterfaceC1632dq;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;

/* loaded from: TunerStudioMS.jar:Q/d.class */
class d implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    InterfaceC1632dq f1780a;

    /* renamed from: b, reason: collision with root package name */
    JDialog f1781b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ a f1782c;

    d(a aVar, InterfaceC1632dq interfaceC1632dq, JDialog jDialog) {
        this.f1782c = aVar;
        this.f1780a = null;
        this.f1781b = null;
        this.f1780a = interfaceC1632dq;
        this.f1781b = jDialog;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (this.f1780a == null) {
            this.f1781b.dispose();
        } else if (this.f1780a.a()) {
            this.f1781b.dispose();
        }
    }
}
