package br;

import G.aH;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: br.u, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:br/u.class */
class C1257u implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1255s f8529a;

    C1257u(C1255s c1255s) {
        this.f8529a = c1255s;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f8529a.f8513b.c(actionEvent.getActionCommand());
        this.f8529a.f8513b.r(null);
        this.f8529a.f8519h.a((aH) null);
        this.f8529a.f8519h.a();
        this.f8529a.f8524m.a("targetLambdaTableName", this.f8529a.f8513b.c());
        this.f8529a.f8524m.a("targetLambdaChannelName", "");
    }
}
