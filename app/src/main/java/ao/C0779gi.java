package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: ao.gi, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/gi.class */
class C0779gi implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ fX f5978a;

    C0779gi(fX fXVar) {
        this.f5978a = fXVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f5978a.c(this.f5978a.f5734j.getSelectedIndex() + 1);
    }
}
