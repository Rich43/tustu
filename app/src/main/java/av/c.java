package aV;

import com.efiAnalytics.ui.C1685fp;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:aV/c.class */
class c implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0474a f3941a;

    c(C0474a c0474a) {
        this.f3941a = c0474a;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        C1685fp.a((Component) this.f3941a.f3937b, this.f3941a.f3938c.isSelected());
        x.a().a(this.f3941a.f3938c.isSelected());
        if (x.a().h() || !this.f3941a.f3938c.isSelected()) {
            return;
        }
        this.f3941a.b();
    }
}
