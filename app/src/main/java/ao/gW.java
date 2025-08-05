package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:ao/gW.class */
class gW implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ gS f5966a;

    gW(gS gSVar) {
        this.f5966a = gSVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f5966a.c(actionEvent.getActionCommand());
    }
}
