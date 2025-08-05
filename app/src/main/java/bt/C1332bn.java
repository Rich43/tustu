package bt;

import G.C0089bv;
import G.C0090bw;
import bH.C1007o;
import com.efiAnalytics.ui.InterfaceC1565bc;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import s.C1818g;

/* renamed from: bt.bn, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/bn.class */
public class C1332bn extends JPanel implements G.aN, bX, bY, InterfaceC1349h, InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    private G.R f9033a;

    /* renamed from: b, reason: collision with root package name */
    private C0090bw f9034b;

    /* renamed from: d, reason: collision with root package name */
    private static String f9036d = C1818g.b("Custom");

    /* renamed from: e, reason: collision with root package name */
    private ArrayList f9037e;

    /* renamed from: c, reason: collision with root package name */
    private C1366y f9035c = new C1366y();

    /* renamed from: f, reason: collision with root package name */
    private boolean f9038f = false;

    /* renamed from: g, reason: collision with root package name */
    private String f9039g = null;

    public C1332bn(G.R r2, C0090bw c0090bw) {
        this.f9033a = null;
        this.f9034b = null;
        this.f9037e = null;
        this.f9033a = r2;
        this.f9034b = c0090bw;
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout(3, 3));
        jPanel.add(BorderLayout.CENTER, new aZ(C1818g.b(c0090bw.l())));
        jPanel.add("East", this.f9035c);
        this.f9035c.addItem(f9036d);
        Iterator itA = c0090bw.a();
        while (itA.hasNext()) {
            this.f9035c.addItem(C1818g.b(((C0089bv) itA.next()).a()));
        }
        this.f9035c.a(this);
        add("North", jPanel);
        this.f9037e = a(c0090bw);
        Iterator it = this.f9037e.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            try {
                G.aR.a().a(r2.c(), str, this);
            } catch (V.a e2) {
                e2.printStackTrace();
                bH.C.a("SettingSelector, Error subscribing to ParameterValue Changes. Parameter:" + str, e2, this);
            }
        }
        c();
    }

    private void c() {
        C0089bv c0089bvA;
        if (this.f9038f) {
            return;
        }
        C0089bv c0089bvA2 = this.f9034b.a(C1818g.c(this.f9035c.a()));
        if (c0089bvA2 == null || !a(c0089bvA2)) {
            if (this.f9039g == null || (c0089bvA = this.f9034b.a(this.f9039g)) == null || !a(c0089bvA)) {
                Iterator itA = this.f9034b.a();
                while (itA.hasNext()) {
                    if (a((C0089bv) itA.next())) {
                        return;
                    }
                }
                this.f9035c.a(f9036d);
            }
        }
    }

    private boolean a(C0089bv c0089bv) {
        Iterator itB = c0089bv.b();
        while (itB.hasNext()) {
            String str = (String) itB.next();
            try {
                if (Math.abs(this.f9033a.c(str).j(this.f9033a.h()) - c0089bv.a(str)) > 1.0E-8d) {
                    return false;
                }
                if (!itB.hasNext()) {
                    this.f9035c.setSelectedItem(C1818g.b(c0089bv.a()));
                    return true;
                }
            } catch (V.g e2) {
                return false;
            }
        }
        return false;
    }

    private void b(C0089bv c0089bv) {
        this.f9038f = true;
        Iterator itB = c0089bv.b();
        while (itB.hasNext()) {
            String str = (String) itB.next();
            try {
                this.f9033a.c(str).a(this.f9033a.h(), c0089bv.a(str));
            } catch (V.g e2) {
                bH.C.c("Unable to set parameter value for " + str);
                e2.printStackTrace();
            } catch (V.j e3) {
                bH.C.b("Invalid value set in settingSelector '" + this.f9034b.l() + "' for parameter:" + str);
            }
        }
        this.f9038f = false;
        c();
    }

    private ArrayList a(C0090bw c0090bw) {
        ArrayList arrayList = new ArrayList();
        Iterator itA = c0090bw.a();
        while (itA.hasNext()) {
            Iterator itB = ((C0089bv) itA.next()).b();
            while (itB.hasNext()) {
                String str = (String) itB.next();
                if (!arrayList.contains(str)) {
                    arrayList.add(str);
                }
            }
        }
        return arrayList;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        G.aR.a().a(this);
    }

    @Override // G.aN
    public void a(String str, String str2) {
        c();
    }

    @Override // bt.bX
    public void b(String str) {
        if (str.equals(f9036d)) {
            return;
        }
        C0089bv c0089bvA = this.f9034b.a(C1818g.c(str));
        if (c0089bvA != null) {
            b(c0089bvA);
        }
        this.f9039g = str;
    }

    @Override // bt.InterfaceC1349h
    public void a() {
        if (this.f9034b == null || this.f9034b.aH() == null || this.f9034b.aH().isEmpty()) {
            return;
        }
        try {
            boolean zA = C1007o.a(this.f9034b.aH(), this.f9033a);
            if (zA ^ this.f9035c.isEnabled()) {
                this.f9035c.setEnabled(zA);
            }
        } catch (V.g e2) {
            Logger.getLogger(C1332bn.class.getName()).log(Level.SEVERE, "Bad enable expresstion  on settingSelector", (Throwable) e2);
        }
    }

    @Override // bt.bY
    public void b() {
        if (this.f9034b == null || this.f9034b.m() == null || this.f9034b.m().isEmpty()) {
            return;
        }
        try {
            boolean zA = C1007o.a(this.f9034b.m(), this.f9033a);
            if (zA ^ this.f9035c.isVisible()) {
                this.f9035c.setVisible(zA);
            }
        } catch (V.g e2) {
            Logger.getLogger(C1332bn.class.getName()).log(Level.SEVERE, "Bad enable expresstion  on settingSelector", (Throwable) e2);
        }
    }
}
