package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/* loaded from: TunerStudioMS.jar:aP/cN.class */
class cN implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bZ f3123a;

    cN(bZ bZVar) {
        this.f3123a = bZVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        h.i.c(h.i.f12331ax, Boolean.toString(((JCheckBoxMenuItem) actionEvent.getSource()).getState()));
    }
}
