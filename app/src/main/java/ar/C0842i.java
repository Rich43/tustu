package ar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: ar.i, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ar/i.class */
class C0842i implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0840g f6238a;

    C0842i(C0840g c0840g) {
        this.f6238a = c0840g;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f6238a.b(actionEvent.getActionCommand());
    }
}
