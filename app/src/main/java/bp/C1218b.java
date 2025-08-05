package bp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: bp.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bp/b.class */
class C1218b implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1217a f8322a;

    C1218b(C1217a c1217a) {
        this.f8322a = c1217a;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f8322a.b();
        this.f8322a.f8319a.requestFocus();
    }
}
