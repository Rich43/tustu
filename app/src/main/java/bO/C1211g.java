package bo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: bo.g, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bo/g.class */
class C1211g implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1206b f8311a;

    C1211g(C1206b c1206b) {
        this.f8311a = c1206b;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (this.f8311a.f8287m.getSelectedItem().equals(C1206b.f8289x)) {
            if (this.f8311a.f8281g.e() > 100.0d) {
                this.f8311a.f8281g.setText("100");
            }
            if (this.f8311a.f8284j.e() > 100.0d) {
                this.f8311a.f8284j.setText("100");
            }
        }
    }
}
