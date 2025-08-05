package as;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: as.k, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:as/k.class */
class C0856k implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0855j f6260a;

    C0856k(C0855j c0855j) {
        this.f6260a = c0855j;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f6260a.close();
    }
}
