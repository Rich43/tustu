package br;

import G.aH;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:br/Y.class */
class Y implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ P f8399a;

    Y(P p2) {
        this.f8399a = p2;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f8399a.f8375b.c(this.f8399a.f8375b.n());
        this.f8399a.f8375b.r(actionEvent.getActionCommand());
        aH aHVarG = this.f8399a.f8374a.g(this.f8399a.f8375b.r());
        for (ag agVar : this.f8399a.f8389p) {
            agVar.f();
            agVar.a(aHVarG);
        }
        this.f8399a.f8386m.a("targetLambdaTableName", this.f8399a.f8375b.c());
        this.f8399a.f8386m.a("targetLambdaChannelName", this.f8399a.f8375b.r());
    }
}
