package bt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/* loaded from: TunerStudioMS.jar:bt/aA.class */
class aA implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1303al f8741a;

    aA(C1303al c1303al) {
        this.f8741a = c1303al;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        JButton jButton = (JButton) actionEvent.getSource();
        this.f8741a.b(jButton.getX(), jButton.getY() + jButton.getHeight());
    }
}
