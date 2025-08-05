package aV;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:aV/e.class */
class e implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0474a f3943a;

    e(C0474a c0474a) {
        this.f3943a = c0474a;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (this.f3943a.a()) {
            this.f3943a.close();
        }
    }
}
