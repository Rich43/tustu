package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* loaded from: TunerStudioMS.jar:ao/eO.class */
class eO implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0737eu f5593a;

    eO(C0737eu c0737eu) {
        this.f5593a = c0737eu;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        h.i.c(h.i.f12340aG, ((JCheckBoxMenuItem) itemEvent.getSource()).getState() + "");
    }
}
