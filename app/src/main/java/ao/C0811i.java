package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: ao.i, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/i.class */
public class C0811i implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    ArrayList f6147a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    JCheckBoxMenuItem f6148b = null;

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getStateChange() == 1) {
            Iterator it = this.f6147a.iterator();
            while (it.hasNext()) {
                JCheckBoxMenuItem jCheckBoxMenuItem = (JCheckBoxMenuItem) it.next();
                if (!jCheckBoxMenuItem.equals(itemEvent.getItem()) && jCheckBoxMenuItem.getState()) {
                    jCheckBoxMenuItem.setState(false);
                }
            }
        }
    }

    public void a(JCheckBoxMenuItem jCheckBoxMenuItem) {
        this.f6147a.add(jCheckBoxMenuItem);
        jCheckBoxMenuItem.addItemListener(this);
    }
}
