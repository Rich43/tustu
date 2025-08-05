package br;

import com.efiAnalytics.ui.bV;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:br/aj.class */
class aj implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ ag f8440a;

    aj(ag agVar) {
        this.f8440a = agVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        try {
            String string = this.f8440a.f8434j.getSelectedItem().toString();
            this.f8440a.f8425a.a(string);
            this.f8440a.f8428d.a("lambdaChannel", string);
        } catch (V.g e2) {
            bV.d(e2.getLocalizedMessage(), this.f8440a.f8427c);
        }
    }
}
