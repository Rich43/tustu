package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: ao.gq, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/gq.class */
class C0787gq implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ fX f5986a;

    C0787gq(fX fXVar) {
        this.f5986a = fXVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f5986a.a(actionEvent.getActionCommand());
    }
}
