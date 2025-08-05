package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:ao/fP.class */
class fP implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0764fu f5714a;

    fP(C0764fu c0764fu) {
        this.f5714a = c0764fu;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f5714a.f5876q.b(true);
        this.f5714a.f5876q.a(true);
        this.f5714a.f5884v.setVisible((this.f5714a.f5876q.a() && this.f5714a.f5876q.d()) ? false : true);
        this.f5714a.h();
    }
}
