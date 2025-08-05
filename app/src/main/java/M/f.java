package m;

import am.C0577e;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import javax.swing.JCheckBox;

/* loaded from: TunerStudioMS.jar:m/f.class */
class f implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ d f12917a;

    f(d dVar) {
        this.f12917a = dVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        Iterator it = this.f12917a.f12912a.iterator();
        while (it.hasNext()) {
            ((JCheckBox) it.next()).setSelected(true);
        }
        this.f12917a.a().clear();
        Iterator it2 = this.f12917a.f12913b.iterator();
        while (it2.hasNext()) {
            this.f12917a.f12911d.add(Integer.valueOf(((C0577e) it2.next()).h()));
        }
    }
}
