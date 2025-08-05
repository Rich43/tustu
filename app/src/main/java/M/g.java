package m;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import javax.swing.JCheckBox;

/* loaded from: TunerStudioMS.jar:m/g.class */
class g implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ d f12918a;

    g(d dVar) {
        this.f12918a = dVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        Iterator it = this.f12918a.f12912a.iterator();
        while (it.hasNext()) {
            ((JCheckBox) it.next()).setSelected(false);
        }
        this.f12918a.a().clear();
    }
}
