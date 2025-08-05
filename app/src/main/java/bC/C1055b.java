package bc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: bc.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bc/b.class */
class C1055b implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1054a f7866a;

    C1055b(C1054a c1054a) {
        this.f7866a = c1054a;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (this.f7866a.d()) {
            this.f7866a.c();
        } else {
            this.f7866a.b();
        }
        this.f7866a.a(this.f7866a.f7861a.a(), this.f7866a.a());
    }
}
