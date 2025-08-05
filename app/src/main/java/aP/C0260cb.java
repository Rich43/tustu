package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import r.C1798a;

/* renamed from: aP.cb, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/cb.class */
class C0260cb implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bZ f3153a;

    C0260cb(bZ bZVar) {
        this.f3153a = bZVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        C1798a.a().b(C1798a.f13308P, Boolean.toString(this.f3153a.f3051z.isSelected()));
        this.f3153a.e();
    }
}
