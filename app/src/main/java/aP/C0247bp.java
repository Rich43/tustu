package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: aP.bp, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/bp.class */
class C0247bp implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0240bi f3092a;

    C0247bp(C0240bi c0240bi) {
        this.f3092a = c0240bi;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f3092a.a(actionEvent.getActionCommand());
    }
}
