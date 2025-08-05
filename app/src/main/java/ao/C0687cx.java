package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: ao.cx, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/cx.class */
class C0687cx implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5507a;

    C0687cx(bP bPVar) {
        this.f5507a = bPVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (((JCheckBoxMenuItem) actionEvent.getSource()).isSelected()) {
            h.i.c("forceOpenGL", Boolean.toString(false));
            h.i.c("disableD3d", Boolean.toString(true));
            this.f5507a.D();
        }
    }
}
