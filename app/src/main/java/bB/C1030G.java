package bb;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: bb.G, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bb/G.class */
class C1030G implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1028E f7729a;

    C1030G(C1028E c1028e) {
        this.f7729a = c1028e;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (this.f7729a.f7721f.isSelected()) {
            this.f7729a.b();
        } else {
            this.f7729a.f7720e.setText("");
        }
    }
}
