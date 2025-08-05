package be;

import G.C0043ac;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: be.h, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:be/h.class */
class C1092h implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1091g f7979a;

    C1092h(C1091g c1091g) {
        this.f7979a = c1091g;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f7979a.f7973d.setEnabled(this.f7979a.f7975f.getSelectedItem().equals(C0043ac.f702h));
    }
}
