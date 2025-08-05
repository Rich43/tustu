package aW;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:aW/f.class */
class f implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ e f3979a;

    f(e eVar) {
        this.f3979a = eVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (this.f3979a.d()) {
            this.f3979a.c();
        } else {
            this.f3979a.b();
        }
        this.f3979a.a(this.f3979a.f3973a.c(), (String) this.f3979a.a());
    }
}
