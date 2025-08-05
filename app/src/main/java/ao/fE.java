package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:ao/fE.class */
class fE implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0764fu f5703a;

    fE(C0764fu c0764fu) {
        this.f5703a = c0764fu;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f5703a.f5876q.b(true);
        this.f5703a.f5876q.a(true);
        this.f5703a.h();
    }
}
