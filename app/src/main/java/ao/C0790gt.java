package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: ao.gt, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/gt.class */
class C0790gt implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ fX f5991a;

    C0790gt(fX fXVar) {
        this.f5991a = fXVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f5991a.d(actionEvent.getActionCommand());
    }
}
