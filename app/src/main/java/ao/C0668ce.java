package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: ao.ce, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/ce.class */
class C0668ce implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5487a;

    C0668ce(bP bPVar) {
        this.f5487a = bPVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        h.i.c(h.i.f12331ax, Boolean.toString(((JCheckBoxMenuItem) actionEvent.getSource()).getState()));
    }
}
