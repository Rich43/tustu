package be;

import com.efiAnalytics.ui.C1685fp;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:be/U.class */
class U implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ T f7948a;

    U(T t2) {
        this.f7948a = t2;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        C1685fp.a((Component) this.f7948a.f7944b, this.f7948a.f7945c.isSelected());
    }
}
