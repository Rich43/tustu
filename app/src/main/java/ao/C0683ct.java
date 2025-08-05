package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: ao.ct, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/ct.class */
class C0683ct implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5503a;

    C0683ct(bP bPVar) {
        this.f5503a = bPVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        h.i.c("lastUpdateCheckDate", "0");
        new dX(this.f5503a, true, C0645bi.a().b()).start();
    }
}
