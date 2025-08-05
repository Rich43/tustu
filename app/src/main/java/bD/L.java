package bD;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:bD/L.class */
class L implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ I f6641a;

    L(I i2) {
        this.f6641a = i2;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f6641a.a(!this.f6641a.f6636e.isSelected());
    }
}
