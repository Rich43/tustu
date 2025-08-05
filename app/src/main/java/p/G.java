package p;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:p/G.class */
class G implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ D f13159a;

    G(D d2) {
        this.f13159a = d2;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (this.f13159a.f13148f.getSelectedItem() == null) {
            this.f13159a.a((String) null, (d.k) null);
        } else {
            this.f13159a.a(((I) this.f13159a.f13148f.getSelectedItem()).a().a(), (d.k) null);
        }
    }
}
