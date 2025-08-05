package at;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:at/e.class */
class e implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ c f6273a;

    e(c cVar) {
        this.f6273a = cVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f6273a.c(actionEvent.getActionCommand());
    }
}
