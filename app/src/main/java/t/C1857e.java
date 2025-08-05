package t;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JRadioButton;

/* renamed from: t.e, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:t/e.class */
class C1857e implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1856d f13888a;

    C1857e(C1856d c1856d) {
        this.f13888a = c1856d;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f13888a.a(((JRadioButton) actionEvent.getSource()).getText());
    }
}
