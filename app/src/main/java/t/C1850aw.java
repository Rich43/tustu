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

/* renamed from: t.aw, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:t/aw.class */
class C1850aw extends bn implements InterfaceC1407f {

    /* renamed from: a, reason: collision with root package name */
    int f13821a;

    /* renamed from: b, reason: collision with root package name */
    int f13822b;

    /* renamed from: g, reason: collision with root package name */
    private boolean f13823g;

    C1850aw(Window window, String str) {
        super(window, C1818g.b(str) + " " + C1818g.b("Slider"));
        this.f13821a = 0;
        this.f13822b = 6;
        this.f13823g = false;
        a(new C1851ax(this));
        this.f13849c.setMinimum(this.f13821a);
        this.f13849c.setMaximum(this.f13822b);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        if (this.f13823g || c() == null) {
            return;
        }
        c().l(this.f13849c.getValue());
    }

    @Override // t.bn, com.efiAnalytics.apps.ts.dashboard.InterfaceC1407f
    public void a(ArrayList arrayList) throws IllegalArgumentException {
        this.f13823g = true;
        Gauge gaugeB = b(arrayList);
        if (gaugeB != null) {
            this.f13849c.setEnabled(true);
            this.f13851e.setForeground(UIManager.getColor("Label.foreground"));
            this.f13849c.setValue(gaugeB.valueDigits());
            this.f13851e.setText("" + gaugeB.valueDigits());
            Iterator it = arrayList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
                if ((abstractC1420s instanceof Gauge) && ((Gauge) abstractC1420s).valueDigits() != gaugeB.valueDigits()) {
                    this.f13851e.setForeground(Color.GRAY);
                    break;
                }
            }
        } else {
            this.f13849c.setEnabled(false);
        }
        this.f13823g = false;
    }
}
