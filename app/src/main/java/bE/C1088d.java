package be;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: be.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:be/d.class */
class C1088d implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1087c f7967a;

    C1088d(C1087c c1087c) {
        this.f7967a = c1087c;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f7967a.a(actionEvent.getActionCommand());
    }
}
