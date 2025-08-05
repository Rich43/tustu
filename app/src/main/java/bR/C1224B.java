package br;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: br.B, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:br/B.class */
class C1224B implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1255s f8336a;

    C1224B(C1255s c1255s) {
        this.f8336a = c1255s;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f8336a.f8513b.c(this.f8336a.f8513b.n());
        this.f8336a.f8513b.r(actionEvent.getActionCommand());
        this.f8336a.f8519h.a();
        this.f8336a.f8519h.a(this.f8336a.f8512a.g(this.f8336a.f8513b.r()));
        this.f8336a.f8524m.a("targetLambdaTableName", this.f8336a.f8513b.c());
        this.f8336a.f8524m.a("targetLambdaChannelName", this.f8336a.f8513b.r());
    }
}
