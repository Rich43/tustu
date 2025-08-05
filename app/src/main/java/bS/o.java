package bs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;

/* loaded from: TunerStudioMS.jar:bs/o.class */
class o implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ k f8625a;

    o(k kVar) {
        this.f8625a = kVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (((JCheckBox) actionEvent.getSource()).isSelected()) {
            this.f8625a.f8619p = "15";
            this.f8625a.a(this.f8625a.f8619p);
        } else {
            this.f8625a.f8619p = "Manually";
            this.f8625a.a(this.f8625a.f8619p);
        }
    }
}
