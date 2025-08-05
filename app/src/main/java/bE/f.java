package bE;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:bE/f.class */
class f implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ e f6717a;

    f(e eVar) {
        this.f6717a = eVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f6717a.d();
        this.f6717a.a(this.f6717a.f6713b.isSelected());
    }
}
