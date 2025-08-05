package bx;

import com.efiAnalytics.ui.bV;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: bx.e, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bx/e.class */
class C1379e implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1377c f9195a;

    C1379e(C1377c c1377c) {
        this.f9195a = c1377c;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (bV.a(this.f9195a.c("Are you sure you want to delete the selected Data Filter?"), (Component) this.f9195a.f9189b, true)) {
            this.f9195a.c();
        }
    }
}
