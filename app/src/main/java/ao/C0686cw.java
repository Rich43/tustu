package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: ao.cw, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/cw.class */
class C0686cw implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5506a;

    C0686cw(bP bPVar) {
        this.f5506a = bPVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (((JCheckBoxMenuItem) actionEvent.getSource()).isSelected()) {
            h.i.c("forceOpenGL", Boolean.toString(true));
            h.i.c("disableD3d", Boolean.toString(false));
            this.f5506a.D();
        }
    }
}
