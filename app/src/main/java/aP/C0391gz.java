package aP;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: aP.gz, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/gz.class */
class C0391gz implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    ArrayList f3502a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    JCheckBoxMenuItem f3503b = null;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C0308dx f3504c;

    C0391gz(C0308dx c0308dx) {
        this.f3504c = c0308dx;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getStateChange() == 1) {
            Iterator it = this.f3502a.iterator();
            while (it.hasNext()) {
                JCheckBoxMenuItem jCheckBoxMenuItem = (JCheckBoxMenuItem) it.next();
                if (!jCheckBoxMenuItem.equals(itemEvent.getItem()) && jCheckBoxMenuItem.getState()) {
                    jCheckBoxMenuItem.setState(false);
                }
            }
        }
    }

    public void a(JCheckBoxMenuItem jCheckBoxMenuItem) {
        this.f3502a.add(jCheckBoxMenuItem);
        jCheckBoxMenuItem.addItemListener(this);
    }
}
