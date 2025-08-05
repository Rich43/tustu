package ao;

import W.C0184j;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:ao/aE.class */
class aE implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0184j f5118a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0625ap f5119b;

    aE(C0625ap c0625ap, C0184j c0184j) {
        this.f5119b = c0625ap;
        this.f5118a = c0184j;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f5118a.i(0);
        this.f5119b.i();
        this.f5119b.repaint();
    }
}
