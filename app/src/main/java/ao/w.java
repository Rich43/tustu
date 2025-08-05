package aO;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:aO/w.class */
class w implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ k f2724a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ q f2725b;

    w(q qVar, k kVar) {
        this.f2725b = qVar;
        this.f2724a = kVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (this.f2725b.f2713d.h()) {
            this.f2725b.f2713d.f(this.f2725b.f2713d.f2669e.f());
        }
    }
}
