package p;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:p/H.class */
class H implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ D f13160a;

    H(D d2) {
        this.f13160a = d2;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (this.f13160a.a()) {
            this.f13160a.d();
        }
    }
}
