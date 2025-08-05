package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: ao.gg, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/gg.class */
class C0777gg implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ fX f5976a;

    C0777gg(fX fXVar) {
        this.f5976a = fXVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f5976a.b(this.f5976a.f5733i.getSelectedIndex());
    }
}
