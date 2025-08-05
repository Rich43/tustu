package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: aP.bo, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/bo.class */
class C0246bo implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0240bi f3091a;

    C0246bo(C0240bi c0240bi) {
        this.f3091a = c0240bi;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f3091a.b(actionEvent.getActionCommand());
    }
}
