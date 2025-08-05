package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: ao.ag, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/ag.class */
class C0616ag implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0615af f5175a;

    C0616ag(C0615af c0615af) {
        this.f5175a = c0615af;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (this.f5175a.f5171b == null || this.f5175a.f5171b.trim().isEmpty()) {
            bH.C.c("No Column Selected, skipping...");
        } else {
            this.f5175a.a();
        }
    }
}
