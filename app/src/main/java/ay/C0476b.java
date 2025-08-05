package aY;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: aY.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aY/b.class */
class C0476b implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0475a f4044a;

    C0476b(C0475a c0475a) {
        this.f4044a = c0475a;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        String strD = this.f4044a.d();
        if (strD == null || strD.length() <= 0) {
            return;
        }
        this.f4044a.f4033a.setText(strD);
    }
}
