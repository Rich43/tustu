package at;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:at/d.class */
class d implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ c f6272a;

    d(c cVar) {
        this.f6272a = cVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f6272a.c(actionEvent.getActionCommand());
    }
}
