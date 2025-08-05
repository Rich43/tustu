package bo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: bo.e, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bo/e.class */
class C1209e implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1206b f8309a;

    C1209e(C1206b c1206b) {
        this.f8309a = c1206b;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f8309a.close();
    }
}
