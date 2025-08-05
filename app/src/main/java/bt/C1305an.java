package bt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: bt.an, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/an.class */
class C1305an implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1303al f8888a;

    C1305an(C1303al c1303al) {
        this.f8888a = c1303al;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) throws NumberFormatException {
        this.f8888a.f(actionEvent.getActionCommand());
    }
}
