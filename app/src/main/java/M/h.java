package m;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;

/* loaded from: TunerStudioMS.jar:m/h.class */
class h implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ d f12919a;

    h(d dVar) {
        this.f12919a = dVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) throws NumberFormatException {
        JCheckBox jCheckBox = (JCheckBox) actionEvent.getSource();
        int i2 = Integer.parseInt(actionEvent.getActionCommand());
        if (jCheckBox.isSelected()) {
            this.f12919a.a().add(Integer.valueOf(i2));
        } else {
            this.f12919a.a().remove(new Integer(i2));
        }
    }
}
