package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: ao.cd, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/cd.class */
class C0667cd implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5486a;

    C0667cd(bP bPVar) {
        this.f5486a = bPVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        h.i.c(h.i.f12333az, Boolean.toString(((JCheckBoxMenuItem) actionEvent.getSource()).getState()));
    }
}
