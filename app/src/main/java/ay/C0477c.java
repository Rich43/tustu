package aY;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: aY.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aY/c.class */
class C0477c implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0475a f4045a;

    C0477c(C0475a c0475a) {
        this.f4045a = c0475a;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        String strD = this.f4045a.d();
        if (strD == null || strD.length() <= 0) {
            return;
        }
        this.f4045a.f4034b.setText(strD);
    }
}
