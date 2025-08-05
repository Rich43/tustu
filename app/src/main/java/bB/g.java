package bB;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:bB/g.class */
class g implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ d f6546a;

    g(d dVar) {
        this.f6546a = dVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f6546a.f6540i.setEditable(!this.f6546a.f6537f.isSelected());
        if (this.f6546a.f6537f.isSelected()) {
            this.f6546a.f6540i.setText("Auto");
        } else {
            this.f6546a.f6540i.a(0.0d);
        }
    }
}
