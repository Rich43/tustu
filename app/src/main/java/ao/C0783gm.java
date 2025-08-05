package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: ao.gm, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/gm.class */
class C0783gm implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ fX f5982a;

    C0783gm(fX fXVar) {
        this.f5982a = fXVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f5982a.e((String) this.f5982a.f5763E.getSelectedItem());
        if (this.f5982a.f5763E.getSelectedItem().equals("Don't Generate X & Y Axis")) {
            return;
        }
        this.f5982a.o();
    }
}
