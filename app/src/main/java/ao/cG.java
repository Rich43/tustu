package ao;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBoxMenuItem;
import k.C1756d;

/* loaded from: TunerStudioMS.jar:ao/cG.class */
class cG extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ List f5461a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ bP f5462b;

    cG(bP bPVar, List list) {
        this.f5462b = bPVar;
        this.f5461a = list;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        for (JCheckBoxMenuItem jCheckBoxMenuItem : this.f5461a) {
            try {
                HashSet hashSet = new HashSet();
                for (String str : C1756d.a().a(jCheckBoxMenuItem.getActionCommand()).b()) {
                    hashSet.add(str);
                }
                jCheckBoxMenuItem.setToolTipText("Required fields :" + Arrays.toString(hashSet.toArray()).replace("Field.", ""));
            } catch (ax.U e2) {
                Logger.getLogger(bP.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
    }
}
