package t;

import com.efiAnalytics.apps.ts.dashboard.AbstractC1420s;
import com.efiAnalytics.apps.ts.dashboard.DashLabel;
import com.efiAnalytics.apps.ts.dashboard.Gauge;
import com.efiAnalytics.apps.ts.dashboard.Indicator;
import com.efiAnalytics.ui.InterfaceC1662et;
import com.efiAnalytics.ui.bV;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JDialog;

/* renamed from: t.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:t/a.class */
public abstract class AbstractC1827a extends JDialog {

    /* renamed from: a, reason: collision with root package name */
    private C1836ai f13712a;

    /* renamed from: b, reason: collision with root package name */
    private InterfaceC1662et f13713b;

    /* renamed from: f, reason: collision with root package name */
    boolean f13714f;

    public AbstractC1827a(Window window, String str) {
        super(window, str);
        this.f13712a = null;
        this.f13713b = null;
        this.f13714f = true;
    }

    public InterfaceC1662et b() {
        return this.f13713b;
    }

    public void a(InterfaceC1662et interfaceC1662et) {
        this.f13713b = interfaceC1662et;
        enableEvents(201L);
    }

    @Override // javax.swing.JDialog, java.awt.Window
    public void processWindowEvent(WindowEvent windowEvent) {
        if (windowEvent.getID() == 201 && this.f13713b != null) {
            this.f13713b.a("X", getX() + "");
            this.f13713b.a(Constants._TAG_Y, getY() + "");
            this.f13714f = true;
        }
        super.processWindowEvent(windowEvent);
    }

    protected boolean a(Component component, String str) {
        if (c().a().size() <= 1 || !this.f13714f) {
            return true;
        }
        this.f13714f = !bV.a(new StringBuilder().append("There is more than 1 item selected.\nAre you sure you want to set all ").append(str).append(" values?").toString(), component, true);
        return !this.f13714f;
    }

    @Override // java.awt.Dialog, java.awt.Window, java.awt.Component
    public void setVisible(boolean z2) {
        if (z2) {
            setBounds(bV.a(getBounds()));
        }
        super.setVisible(z2);
    }

    public C1836ai c() {
        return this.f13712a;
    }

    public void a(C1836ai c1836ai) {
        this.f13712a = c1836ai;
    }

    public Gauge b(ArrayList arrayList) {
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Gauge) {
                return (Gauge) abstractC1420s;
            }
        }
        return null;
    }

    public Indicator c(ArrayList arrayList) {
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Indicator) {
                return (Indicator) abstractC1420s;
            }
        }
        return null;
    }

    public DashLabel d(ArrayList arrayList) {
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof DashLabel) {
                return (DashLabel) abstractC1420s;
            }
        }
        return null;
    }
}
