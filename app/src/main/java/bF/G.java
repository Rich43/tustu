package bF;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:bF/G.class */
class G implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ D f6815a;

    G(D d2) {
        this.f6815a = d2;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f6815a.a(actionEvent.getActionCommand());
    }
}
