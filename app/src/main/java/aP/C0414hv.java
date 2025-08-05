package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import r.C1798a;

/* renamed from: aP.hv, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/hv.class */
class C0414hv implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0413hu f3620a;

    C0414hv(C0413hu c0413hu) {
        this.f3620a = c0413hu;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        C1798a.a().b(C1798a.f13416bT, Boolean.toString(this.f3620a.f3618a.isSelected()));
    }
}
