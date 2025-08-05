package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:ao/gV.class */
class gV implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ gS f5965a;

    gV(gS gSVar) {
        this.f5965a = gSVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f5965a.a(actionEvent.getActionCommand());
    }
}
