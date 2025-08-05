package bf;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: bf.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bf/d.class */
class C1111d implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ String f8048a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1108a f8049b;

    C1111d(C1108a c1108a, String str) {
        this.f8049b = c1108a;
        this.f8048a = str;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f8049b.c(this.f8048a);
    }
}
