package t;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: t.az, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:t/az.class */
class C1853az implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1852ay f13827a;

    C1853az(C1852ay c1852ay) {
        this.f13827a = c1852ay;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f13827a.c().a(this.f13827a.f13826b.isSelected());
    }
}
