package br;

import G.aH;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:br/R.class */
class R implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ P f8392a;

    R(P p2) {
        this.f8392a = p2;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f8392a.f8375b.c(actionEvent.getActionCommand());
        this.f8392a.f8375b.r(null);
        for (ag agVar : this.f8392a.f8389p) {
            agVar.a((aH) null);
            agVar.f();
        }
        this.f8392a.f8386m.a("targetLambdaTableName", this.f8392a.f8375b.c());
        this.f8392a.f8386m.a("targetLambdaChannelName", "");
    }
}
