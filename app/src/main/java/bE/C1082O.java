package be;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import s.C1818g;

/* renamed from: be.O, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:be/O.class */
class C1082O implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1080M f7928a;

    C1082O(C1080M c1080m) {
        this.f7928a = c1080m;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) throws IllegalArgumentException {
        String strD = this.f7928a.d();
        C1083P c1083pC = null;
        if (this.f7928a.f7911c.isSelected() && !strD.isEmpty()) {
            c1083pC = this.f7928a.c(strD);
        }
        switch (this.f7928a.f7910b.getSelectedIndex()) {
            case 0:
                this.f7928a.f7926r = 5;
                this.f7928a.f7923o.setText(C1818g.b("Input Value"));
                break;
            case 1:
                this.f7928a.f7926r = 255;
                this.f7928a.f7923o.setText(C1818g.b("Input Voltage"));
                break;
            case 2:
                this.f7928a.f7926r = 1023;
                this.f7928a.f7923o.setText(C1818g.b("Input Voltage"));
                break;
            case 3:
                this.f7928a.f7926r = 4095;
                this.f7928a.f7923o.setText(C1818g.b("Input Voltage"));
                break;
        }
        if (!this.f7928a.f7911c.isSelected() || strD.isEmpty() || c1083pC == null) {
            return;
        }
        this.f7928a.a(c1083pC);
    }
}
