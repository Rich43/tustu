package u;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: u.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:u/b.class */
class C1880b implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    g f13980a;

    /* renamed from: b, reason: collision with root package name */
    c f13981b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C1879a f13982c;

    C1880b(C1879a c1879a, g gVar, c cVar) {
        this.f13982c = c1879a;
        this.f13980a = null;
        this.f13981b = null;
        this.f13980a = gVar;
        this.f13981b = cVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (this.f13980a.d()) {
            this.f13981b.a();
        }
    }
}
