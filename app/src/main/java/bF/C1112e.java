package bf;

import G.Q;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: bf.e, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bf/e.class */
class C1112e implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ Q f8050a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1108a f8051b;

    C1112e(C1108a c1108a, Q q2) {
        this.f8051b = c1108a;
        this.f8050a = q2;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f8051b.e(this.f8050a);
    }
}
