package br;

import com.efiAnalytics.ui.bV;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:br/ak.class */
class ak implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ ag f8441a;

    ak(ag agVar) {
        this.f8441a = agVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        try {
            String string = this.f8441a.f8435k.getSelectedItem().toString();
            this.f8441a.f8425a.b(string);
            this.f8441a.f8428d.a("egoCorChannel", string);
        } catch (V.g e2) {
            bV.d(e2.getLocalizedMessage(), this.f8441a.f8427c);
        }
    }
}
