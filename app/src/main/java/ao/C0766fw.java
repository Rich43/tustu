package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: ao.fw, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/fw.class */
class C0766fw implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0764fu f5886a;

    C0766fw(C0764fu c0764fu) {
        this.f5886a = c0764fu;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f5886a.e(actionEvent.getActionCommand());
    }
}
