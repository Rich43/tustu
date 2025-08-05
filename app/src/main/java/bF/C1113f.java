package bf;

import G.Q;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: bf.f, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bf/f.class */
class C1113f implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ Q f8052a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1108a f8053b;

    C1113f(C1108a c1108a, Q q2) {
        this.f8053b = c1108a;
        this.f8052a = q2;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f8053b.d(this.f8052a);
    }
}
