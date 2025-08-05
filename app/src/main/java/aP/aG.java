package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:aP/aG.class */
class aG implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aF f2816a;

    aG(aF aFVar) {
        this.f2816a = aFVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getActionCommand().equals("min")) {
            this.f2816a.f2806b.setText(this.f2816a.a());
        } else if (actionEvent.getActionCommand().equals("max")) {
            this.f2816a.f2807c.setText(this.f2816a.a());
        }
    }
}
