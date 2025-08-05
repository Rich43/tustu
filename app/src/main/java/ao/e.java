package aO;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;

/* loaded from: TunerStudioMS.jar:aO/e.class */
class e implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ a f2655a;

    e(a aVar) {
        this.f2655a = aVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f2655a.a(((JCheckBox) actionEvent.getSource()).isSelected());
    }
}
