package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: ao.al, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/al.class */
class C0621al implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0620ak f5223a;

    C0621al(C0620ak c0620ak) {
        this.f5223a = c0620ak;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f5223a.a((C0623an) actionEvent.getSource());
    }
}
