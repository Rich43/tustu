package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: ao.cb, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/cb.class */
class C0665cb implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5484a;

    C0665cb(bP bPVar) {
        this.f5484a = bPVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        h.i.c(h.i.f12335aB, Boolean.toString(((JCheckBoxMenuItem) actionEvent.getSource()).getState()));
    }
}
