package bB;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:bB/e.class */
class e implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ d f6544a;

    e(d dVar) {
        this.f6544a = dVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f6544a.f6538g.setEditable(!this.f6544a.f6535d.isSelected());
        if (this.f6544a.f6535d.isSelected()) {
            this.f6544a.f6538g.setText("Auto");
        } else {
            this.f6544a.f6538g.a(0.0d);
        }
    }
}
