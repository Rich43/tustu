package bB;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:bB/f.class */
class f implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ d f6545a;

    f(d dVar) {
        this.f6545a = dVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f6545a.f6539h.setEditable(!this.f6545a.f6536e.isSelected());
        if (this.f6545a.f6536e.isSelected()) {
            this.f6545a.f6539h.setText("Auto");
        } else {
            this.f6545a.f6539h.a(0.0d);
        }
    }
}
