package be;

import com.efiAnalytics.ui.C1685fp;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:be/V.class */
class V implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ T f7949a;

    V(T t2) {
        this.f7949a = t2;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        C1685fp.a((Component) this.f7949a.f7943a, this.f7949a.f7946d.isSelected());
    }
}
