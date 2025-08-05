package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:aP/bG.class */
class bG implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bD f2977a;

    bG(bD bDVar) {
        this.f2977a = bDVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (this.f2977a.f2970c.isSelected()) {
            this.f2977a.f2971d.setEnabled(true);
            this.f2977a.f2972e.setEnabled(true);
        } else {
            this.f2977a.f2971d.setEnabled(false);
            this.f2977a.f2971d.setText("");
            this.f2977a.f2972e.setEnabled(false);
        }
    }
}
