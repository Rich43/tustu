package t;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:t/bl.class */
class bl implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bi f13845a;

    bl(bi biVar) {
        this.f13845a = biVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f13845a.setVisible(false);
    }
}
