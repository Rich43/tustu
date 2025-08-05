package t;

import com.efiAnalytics.apps.ts.dashboard.AbstractC1420s;
import com.efiAnalytics.apps.ts.dashboard.Gauge;
import com.efiAnalytics.apps.ts.dashboard.InterfaceC1407f;
import java.awt.Color;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.UIManager;
import s.C1818g;

/* renamed from: t.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:t/b.class */
class C1854b extends bn implements InterfaceC1407f {

    /* renamed from: a, reason: collision with root package name */
    private boolean f13828a;

    C1854b(Window window, String str) {
        super(window, C1818g.b(str) + " " + C1818g.b("Slider"));
        this.f13828a = false;
        a(new C1855c(this));
        this.f13849c.setMinimum(0);
        this.f13849c.setMaximum(100);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        if (this.f13828a || c() == null) {
            return;
        }
        c().f(this.f13849c.getValue());
    }

    @Override // t.bn, com.efiAnalytics.apps.ts.dashboard.InterfaceC1407f
    public void a(ArrayList arrayList) throws IllegalArgumentException {
        this.f13828a = true;
        Gauge gaugeB = b(arrayList);
        if (gaugeB != null) {
            this.f13849c.setEnabled(true);
            this.f13851e.setForeground(UIManager.getColor("Label.foreground"));
            this.f13849c.setValue(gaugeB.getBorderWidth());
            this.f13851e.setText("" + gaugeB.getBorderWidth());
            Iterator it = arrayList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
                if ((abstractC1420s instanceof Gauge) && ((Gauge) abstractC1420s).getBorderWidth() != gaugeB.getBorderWidth()) {
                    this.f13851e.setForeground(Color.GRAY);
                    break;
                }
            }
        } else {
            this.f13849c.setEnabled(false);
        }
        this.f13828a = false;
    }
}
