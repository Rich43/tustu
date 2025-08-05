package be;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:be/z.class */
class z implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ y f8031a;

    z(y yVar) {
        this.f8031a = yVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (this.f8031a.f8025l.getSelectedItem() == null || !this.f8031a.f8025l.getSelectedItem().equals(y.f8029p)) {
            return;
        }
        this.f8031a.d();
    }
}
