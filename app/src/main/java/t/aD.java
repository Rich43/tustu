package t;

import com.efiAnalytics.apps.ts.dashboard.AbstractC1420s;
import com.efiAnalytics.apps.ts.dashboard.Gauge;
import com.efiAnalytics.apps.ts.dashboard.InterfaceC1407f;
import com.efiAnalytics.ui.eJ;
import java.awt.Color;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.UIManager;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:t/aD.class */
class aD extends bn implements InterfaceC1407f {

    /* renamed from: a, reason: collision with root package name */
    int f13721a;

    /* renamed from: b, reason: collision with root package name */
    int f13722b;

    /* renamed from: g, reason: collision with root package name */
    private boolean f13723g;

    aD(Window window, String str) {
        super(window, C1818g.b(str) + " " + C1818g.b("Slider"));
        this.f13721a = eJ.a(-30);
        this.f13722b = eJ.a(30);
        this.f13723g = false;
        a(new aE(this));
        this.f13849c.setMinimum(this.f13721a);
        this.f13849c.setMaximum(this.f13722b);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        if (this.f13723g || c() == null) {
            return;
        }
        c().g(this.f13849c.getValue());
    }

    @Override // t.bn, com.efiAnalytics.apps.ts.dashboard.InterfaceC1407f
    public void a(ArrayList arrayList) throws IllegalArgumentException {
        this.f13723g = true;
        Gauge gaugeB = b(arrayList);
        if (gaugeB != null) {
            this.f13849c.setEnabled(true);
            this.f13851e.setForeground(UIManager.getColor("Label.foreground"));
            this.f13849c.setValue(gaugeB.getFontSizeAdjustment());
            this.f13851e.setText("" + gaugeB.getFontSizeAdjustment());
            Iterator it = arrayList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
                if ((abstractC1420s instanceof Gauge) && ((Gauge) abstractC1420s).getFontSizeAdjustment() != gaugeB.getFontSizeAdjustment()) {
                    this.f13851e.setForeground(Color.GRAY);
                    break;
                }
            }
        } else {
            this.f13849c.setEnabled(false);
        }
        this.f13723g = false;
    }
}
