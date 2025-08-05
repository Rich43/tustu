package aP;

import com.efiAnalytics.apps.ts.dashboard.C1425x;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import n.InterfaceC1761a;
import r.C1798a;

/* loaded from: TunerStudioMS.jar:aP/bT.class */
public class bT extends n.n implements InterfaceC1761a, n.g {

    /* renamed from: e, reason: collision with root package name */
    boolean f3010e;

    /* renamed from: f, reason: collision with root package name */
    aT f3011f;

    /* renamed from: a, reason: collision with root package name */
    int f3006a = 0;

    /* renamed from: b, reason: collision with root package name */
    HashMap f3007b = new HashMap();

    /* renamed from: c, reason: collision with root package name */
    ArrayList f3008c = new ArrayList();

    /* renamed from: d, reason: collision with root package name */
    bX f3009d = new bX(this);

    /* renamed from: h, reason: collision with root package name */
    private boolean f3012h = false;

    /* renamed from: g, reason: collision with root package name */
    boolean f3013g = C1798a.a().b(C1798a.f13329ak, false);

    public bT() {
        this.f3010e = false;
        this.f3011f = null;
        setTabPlacement(3);
        this.f3010e = C1798a.a().a(C1798a.da, C1798a.db);
        if (!this.f3010e) {
            addTab("", new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/new16.gif"))), new JPanel(), "Add Gauge Cluster");
            addChangeListener(new bU(this));
            addMouseListener(this.f3009d);
        }
        this.f3011f = new aT(this);
    }

    public C1425x a(com.efiAnalytics.apps.ts.dashboard.Z z2, String str) {
        C1425x c1425x = new C1425x(G.T.a().c());
        c1425x.a(z2);
        a(c1425x, str);
        return c1425x;
    }

    public void a(boolean z2) {
        if (getSelectedIndex() < (this.f3010e ? getTabCount() - 1 : getTabCount() - 2)) {
            a(getSelectedIndex() + 1, z2);
        } else {
            a(0, z2);
        }
    }

    public void b(boolean z2) {
        int tabCount = this.f3010e ? getTabCount() - 1 : getTabCount() - 2;
        if (getSelectedIndex() > 0) {
            a(getSelectedIndex() - 1, z2);
        } else {
            a(tabCount, z2);
        }
    }

    public int c() {
        return this.f3010e ? getTabCount() : getTabCount() - 1;
    }

    public void a(int i2, boolean z2) {
        getSelectedIndex();
        super.setSelectedIndex(i2);
        C1425x c1425xB = cZ.a().b();
        C1425x c1425x = (C1425x) this.f3007b.get(getTitleAt(i2));
        boolean z3 = c1425xB != null && c1425xB.s();
        if (c1425x != null) {
            cZ.a().a(c1425x);
            if (z2 && z3) {
                SwingUtilities.invokeLater(new bV(this, c1425xB, c1425x));
            }
        }
    }

    @Override // javax.swing.JTabbedPane
    public void setSelectedIndex(int i2) {
        getSelectedIndex();
        super.setSelectedIndex(i2);
        C1425x c1425xB = cZ.a().b();
        C1425x c1425x = (C1425x) this.f3007b.get(getTitleAt(i2));
        if (this.f3013g) {
            if (c1425x != null) {
                c1425x.h(true);
            }
            if (c1425xB != null && !c1425xB.equals(c1425x)) {
                c1425xB.h(false);
            }
        }
        boolean z2 = c1425xB != null && c1425xB.s();
        if (c1425x != null) {
            cZ.a().a(c1425x);
        }
    }

    public C1425x a(C1425x c1425x, String str) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new bz.b());
        jPanel.add(c1425x);
        if (this.f3013g && !str.equals("Main Dashboard")) {
            c1425x.h(false);
        }
        int i2 = 2;
        while (d(str)) {
            str = str + "(" + i2 + ")";
            i2++;
        }
        this.f3007b.put(str, c1425x);
        setName(str);
        c1425x.setName(str);
        c1425x.a(new W.ar(C1798a.a().f13332an, str));
        if (getTabCount() < 1) {
            insertTab(str, null, jPanel, null, 0);
        } else {
            insertTab(str, null, jPanel, null, this.f3010e ? getTabCount() : getTabCount() - 1);
        }
        c1425x.addMouseListener(this.f3011f);
        c1425x.addMouseMotionListener(this.f3011f);
        return c1425x;
    }

    public void f_() {
        for (int tabCount = this.f3010e ? getTabCount() - 1 : getTabCount() - 2; tabCount > 0; tabCount--) {
            String titleAt = getTitleAt(tabCount);
            C1425x c1425x = (C1425x) this.f3007b.get(titleAt);
            if (c1425x != null) {
                c1425x.c();
                this.f3007b.remove(titleAt);
                c1425x.removeMouseListener(this.f3011f);
                c1425x.removeMouseMotionListener(this.f3011f);
            }
            remove(tabCount);
        }
    }

    public C1425x e() {
        String titleAt = getTitleAt(getSelectedIndex());
        if (titleAt != null) {
            return (C1425x) this.f3007b.get(titleAt);
        }
        return null;
    }

    public Iterator f() {
        return this.f3007b.values().iterator();
    }

    public C1425x a(String str) {
        return (C1425x) this.f3007b.get(str);
    }

    public void a(InterfaceC0285d interfaceC0285d) {
        this.f3008c.add(interfaceC0285d);
    }

    public boolean b(String str) {
        if (str == null || str.equals("") || str.equals("Main Dashboard") || !h(str)) {
            return false;
        }
        for (int i2 = 0; i2 < getTabCount(); i2++) {
            if (getTitleAt(i2).equals(str)) {
                removeTabAt(i2);
                C1425x c1425x = (C1425x) this.f3007b.get(str);
                if (c1425x != null) {
                    c1425x.removeMouseListener(this.f3011f);
                    c1425x.removeMouseMotionListener(this.f3011f);
                }
                this.f3007b.remove(str);
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void h() {
        Iterator it = this.f3008c.iterator();
        while (it.hasNext()) {
            ((InterfaceC0285d) it.next()).a();
        }
    }

    private boolean h(String str) {
        C1425x c1425xA = a(str);
        Iterator it = this.f3008c.iterator();
        while (it.hasNext()) {
            if (!((InterfaceC0285d) it.next()).a(str, c1425xA)) {
                return false;
            }
        }
        return true;
    }

    public void c(String str) {
        Iterator it = this.f3007b.values().iterator();
        while (it.hasNext()) {
            try {
                ((C1425x) it.next()).c(str);
            } catch (Exception e2) {
                bH.C.b("Failed to set new signature for GaugeCluster.");
                e2.printStackTrace();
            }
        }
    }

    protected void a(MouseEvent mouseEvent) {
        String titleAt = getTitleAt(getSelectedIndex());
        if (titleAt == null || titleAt.equals("") || titleAt.equals("Main Dashboard")) {
            return;
        }
        JPopupMenu jPopupMenu = new JPopupMenu();
        JMenuItem jMenuItem = new JMenuItem("Remove Gauge Cluster");
        jMenuItem.addActionListener(new bW(this));
        jPopupMenu.add(jMenuItem);
        jPopupMenu.show(this, mouseEvent.getX(), mouseEvent.getY());
    }

    @Override // n.InterfaceC1761a
    public boolean a() {
        this.f3012h = true;
        return true;
    }

    @Override // n.g
    public void b() {
        this.f3012h = false;
    }

    public boolean g() {
        return this.f3012h;
    }
}
