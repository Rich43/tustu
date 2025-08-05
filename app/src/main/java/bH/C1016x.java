package bH;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: bH.x, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bH/x.class */
class C1016x implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1015w f7063a;

    C1016x(C1015w c1015w) {
        this.f7063a = c1015w;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f7063a.f7061a.setText(Integer.toHexString(Float.floatToIntBits(Float.parseFloat(this.f7063a.f7062b.getText()))));
    }
}
