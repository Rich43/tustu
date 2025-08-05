package br;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:br/ai.class */
class ai implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ ag f8439a;

    ai(ag agVar) {
        this.f8439a = agVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (this.f8439a.f8435k.getItemCount() > this.f8439a.f8436l.getSelectedIndex()) {
            this.f8439a.f8435k.setSelectedIndex(this.f8439a.f8436l.getSelectedIndex());
        } else {
            this.f8439a.f8435k.setSelectedIndex(this.f8439a.f8435k.getItemCount() - 1);
        }
        if (this.f8439a.f8434j.getItemCount() > this.f8439a.f8436l.getSelectedIndex()) {
            this.f8439a.f8434j.setSelectedIndex(this.f8439a.f8436l.getSelectedIndex());
        } else {
            this.f8439a.f8434j.setSelectedIndex(this.f8439a.f8434j.getItemCount() - 1);
        }
        this.f8439a.f8428d.a("egoSensorIndex", this.f8439a.f8434j.getSelectedIndex() + "");
    }
}
