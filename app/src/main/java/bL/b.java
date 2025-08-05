package bL;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:bL/b.class */
class b implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ a f7129a;

    b(a aVar) {
        this.f7129a = aVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f7129a.a((e) this.f7129a.f7128g.getSelectedItem());
    }
}
