package bp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:bp/c.class */
class c implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1217a f8323a;

    c(C1217a c1217a) {
        this.f8323a = c1217a;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f8323a.a();
        this.f8323a.f8319a.requestFocus();
    }
}
