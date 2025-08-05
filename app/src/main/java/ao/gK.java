package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:ao/gK.class */
class gK implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ gJ f5933a;

    gK(gJ gJVar) {
        this.f5933a = gJVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f5933a.f5926b.setEnabled(this.f5933a.f5928d.isSelected());
    }
}
