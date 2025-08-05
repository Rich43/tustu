package t;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:t/bp.class */
class bp implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bn f13853a;

    bp(bn bnVar) {
        this.f13853a = bnVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f13853a.dispose();
    }
}
