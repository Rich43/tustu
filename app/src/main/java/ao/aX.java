package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:ao/aX.class */
class aX implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aQ f5164a;

    aX(aQ aQVar) {
        this.f5164a = aQVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f5164a.f5149g.j();
        this.f5164a.b(false);
        this.f5164a.f5149g.b(false);
    }
}
