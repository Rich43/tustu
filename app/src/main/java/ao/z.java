package aO;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:aO/z.class */
class z implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ k f2730a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ q f2731b;

    z(q qVar, k kVar) {
        this.f2731b = qVar;
        this.f2730a = kVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (this.f2731b.f2713d.h()) {
            this.f2731b.f2713d.f(this.f2731b.f2713d.f2669e.e());
        }
    }
}
