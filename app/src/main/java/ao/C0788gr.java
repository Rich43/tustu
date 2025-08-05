package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: ao.gr, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/gr.class */
class C0788gr implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ fX f5987a;

    C0788gr(fX fXVar) {
        this.f5987a = fXVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f5987a.b(actionEvent.getActionCommand());
    }
}
