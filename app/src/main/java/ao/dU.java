package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:ao/dU.class */
class dU implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ dT f5541a;

    dU(dT dTVar) {
        this.f5541a = dTVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f5541a.f5540a.b(actionEvent.getActionCommand(), false);
    }
}
