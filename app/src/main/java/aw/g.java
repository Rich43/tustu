package aW;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:aW/g.class */
class g implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ a f3980a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ e f3981b;

    g(e eVar, a aVar) {
        this.f3981b = eVar;
        this.f3980a = aVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        r.a().a(this.f3980a, this.f3981b.f3973a.c());
    }
}
