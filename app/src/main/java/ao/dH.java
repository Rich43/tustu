package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/* loaded from: TunerStudioMS.jar:ao/dH.class */
class dH implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5521a;

    dH(bP bPVar) {
        this.f5521a = bPVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        h.i.c(h.i.f12306aa, Boolean.toString(((JCheckBoxMenuItem) actionEvent.getSource()).isSelected()));
        this.f5521a.T();
        this.f5521a.f5347a.o();
    }
}
