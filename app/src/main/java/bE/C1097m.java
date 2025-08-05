package be;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: be.m, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:be/m.class */
class C1097m implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1094j f7989a;

    C1097m(C1094j c1094j) {
        this.f7989a = c1094j;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f7989a.a(this.f7989a.f7985e.n(actionEvent.getActionCommand()));
    }
}
