package com.efiAnalytics.apps.ts.tuningViews;

import c.C1383b;
import com.efiAnalytics.ui.cO;
import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import n.InterfaceC1761a;
import r.C1806i;

/* renamed from: com.efiAnalytics.apps.ts.tuningViews.n, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/n.class */
public class C1441n extends n.n implements G.S, InterfaceC1761a, n.g {

    /* renamed from: e, reason: collision with root package name */
    boolean f9793e;

    /* renamed from: a, reason: collision with root package name */
    int f9789a = 0;

    /* renamed from: b, reason: collision with root package name */
    HashMap f9790b = new HashMap();

    /* renamed from: c, reason: collision with root package name */
    ArrayList f9791c = new ArrayList();

    /* renamed from: d, reason: collision with root package name */
    C1451x f9792d = new C1451x(this);

    /* renamed from: k, reason: collision with root package name */
    private boolean f9794k = false;

    /* renamed from: f, reason: collision with root package name */
    boolean f9795f = false;

    /* renamed from: g, reason: collision with root package name */
    List f9796g = new ArrayList();

    /* renamed from: h, reason: collision with root package name */
    List f9797h = new ArrayList();

    /* renamed from: i, reason: collision with root package name */
    List f9798i = new ArrayList();

    /* renamed from: j, reason: collision with root package name */
    int f9799j = 0;

    public C1441n() {
        Image imageA;
        this.f9793e = false;
        setTabPlacement(3);
        this.f9793e = !C1806i.a().a(";LFDS;LFDS0943;L");
        if (this.f9793e) {
            return;
        }
        try {
            imageA = cO.a().a(cO.f11116F, this, 16);
        } catch (V.a e2) {
            imageA = null;
            Logger.getLogger(C1441n.class.getName()).log(Level.WARNING, "Failed to get TuningView Tab New image", (Throwable) e2);
        }
        addTab("", new ImageIcon(imageA), new JPanel(), "Add Tuning View");
        addChangeListener(new C1442o(this));
        addMouseListener(this.f9792d);
    }

    public void a(List list) {
        if (this.f9794k) {
            b(list);
        } else {
            this.f9796g.addAll(list);
        }
    }

    private void b(List list) {
        if (0 != 0) {
            new C1443p(this, list).start();
            return;
        }
        Iterator it = list.iterator();
        while (it.hasNext()) {
            b((C1438k) it.next());
        }
        list.clear();
        c(0);
    }

    public J a(C1438k c1438k) {
        F fD = c1438k.d();
        J jA = a(fD, fD.b());
        jA.c(c1438k.a().getAbsolutePath());
        return jA;
    }

    private void b(C1438k c1438k) {
        J j2 = new J();
        j2.c(c1438k.a().getAbsolutePath());
        j2.d(c1438k.c());
        this.f9797h.add(new y(this, c1438k, j2));
        a(j2, c1438k.b());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(int i2) {
        if (this.f9797h == null || this.f9797h.size() <= i2 || ((y) this.f9797h.get(i2)).a()) {
            return;
        }
        ((y) this.f9797h.get(i2)).start();
    }

    public void a(int i2) {
        if (this.f9797h == null || this.f9797h.size() <= i2 || ((y) this.f9797h.get(i2)).a()) {
            return;
        }
        ((y) this.f9797h.get(i2)).run();
    }

    public J a(F f2, String str) {
        J j2 = new J();
        j2.a(f2);
        a(j2, str);
        return j2;
    }

    @Override // javax.swing.JTabbedPane
    public void setSelectedIndex(int i2) throws HeadlessException {
        int selectedIndex = getSelectedIndex();
        super.setSelectedIndex(i2);
        J j2 = (J) this.f9790b.get(getTitleAt(i2));
        if (j2 == null) {
            bH.C.b("Unable to set Main cluster for title:" + getTitleAt(i2));
        }
        if (selectedIndex >= 0) {
            J j3 = (J) this.f9790b.get(getTitleAt(selectedIndex));
            if (j3 != null && j3.s()) {
                if (j2 != null) {
                    j2.t();
                }
                SwingUtilities.invokeLater(new RunnableC1444q(this, j3));
            }
        }
    }

    public J a(J j2, String str) {
        return a(j2, str, getTabCount() < 1 ? 0 : this.f9793e ? getTabCount() : getTabCount() - 1);
    }

    public J a(J j2, String str, int i2) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add(BorderLayout.CENTER, j2);
        int i3 = 2;
        while (d(str)) {
            str = str + "(" + i3 + ")";
            i3++;
        }
        this.f9790b.put(str, j2);
        setName(str);
        j2.setName(str);
        this.f9795f = true;
        insertTab(str, null, jPanel, null, i2);
        C1383b.a().a(new C1450w(this, j2));
        a(j2);
        this.f9795f = false;
        return j2;
    }

    @Override // javax.swing.JTabbedPane, java.awt.Container
    public void remove(int i2) {
        this.f9790b.remove(getTitleAt(i2));
        if (this.f9796g.size() > i2) {
            this.f9796g.remove(i2);
        }
        if (this.f9797h.size() > i2) {
            this.f9797h.remove(i2);
        }
        super.remove(i2);
    }

    public void c() {
        for (int tabCount = this.f9793e ? getTabCount() - 1 : getTabCount() - 2; tabCount >= 0; tabCount--) {
            String titleAt = getTitleAt(tabCount);
            J j2 = (J) this.f9790b.get(titleAt);
            if (j2 != null) {
                j2.close();
                this.f9790b.remove(titleAt);
            }
            remove(tabCount);
        }
        this.f9796g.clear();
        this.f9797h.clear();
        this.f9798i.clear();
    }

    public J c_() {
        String titleAt = getTitleAt(getSelectedIndex());
        if (titleAt != null) {
            return (J) this.f9790b.get(titleAt);
        }
        return null;
    }

    public Iterator e() {
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < getTabCount() - 1; i2++) {
            arrayList.add(a(getTitleAt(i2)));
        }
        return arrayList.iterator();
    }

    @Override // javax.swing.JTabbedPane
    public void setTitleAt(int i2, String str) {
        String titleAt = getTitleAt(i2);
        J j2 = (J) this.f9790b.get(titleAt);
        this.f9790b.remove(titleAt);
        this.f9790b.put(str, j2);
        super.setTitleAt(i2, str);
    }

    public J a(String str) {
        return (J) this.f9790b.get(str);
    }

    public J b(int i2) {
        return a(getTitleAt(i2));
    }

    public void a(InterfaceC1428a interfaceC1428a) {
        this.f9791c.add(interfaceC1428a);
    }

    public boolean b(String str) {
        if (str == null || str.equals("") || f(str) < this.f9799j || !c(str)) {
            return false;
        }
        for (int i2 = 0; i2 < getTabCount(); i2++) {
            if (getTitleAt(i2).equals(str)) {
                removeTabAt(i2);
                J j2 = (J) this.f9790b.get(str);
                if (j2 != null) {
                    j2.close();
                }
                this.f9790b.remove(str);
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g() {
        this.f9795f = true;
        Iterator it = this.f9791c.iterator();
        while (it.hasNext()) {
            ((InterfaceC1428a) it.next()).a();
        }
        this.f9795f = false;
    }

    private boolean c(String str) {
        a(str);
        Iterator it = this.f9791c.iterator();
        while (it.hasNext()) {
            if (!((InterfaceC1428a) it.next()).a(str, this)) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str, int i2) {
        J jA = a(str);
        Iterator it = this.f9791c.iterator();
        while (it.hasNext()) {
            ((InterfaceC1428a) it.next()).a(jA, i2);
        }
    }

    private void a(J j2) {
        Iterator it = this.f9791c.iterator();
        while (it.hasNext()) {
            ((InterfaceC1428a) it.next()).a(j2);
        }
    }

    protected void a(MouseEvent mouseEvent) {
        int iIndexAtLocation = indexAtLocation(mouseEvent.getX(), mouseEvent.getY());
        String titleAt = getTitleAt(iIndexAtLocation);
        if (titleAt == null || titleAt.equals("") || getSelectedIndex() < this.f9799j) {
            return;
        }
        JPopupMenu jPopupMenu = new JPopupMenu();
        JMenuItem jMenuItem = new JMenuItem("Remove Tuning View");
        jMenuItem.addActionListener(new C1445r(this, iIndexAtLocation));
        jPopupMenu.add(jMenuItem);
        JMenuItem jMenuItem2 = new JMenuItem("Rename Tab");
        jMenuItem2.addActionListener(new C1446s(this, iIndexAtLocation));
        jPopupMenu.add(jMenuItem2);
        if (iIndexAtLocation > 0) {
            JMenuItem jMenuItem3 = new JMenuItem("Move Tab Left");
            jMenuItem3.addActionListener(new C1447t(this, iIndexAtLocation));
            jPopupMenu.add(jMenuItem3);
        }
        if (iIndexAtLocation < getTabCount() - 2) {
            JMenuItem jMenuItem4 = new JMenuItem("Move Tab Right");
            jMenuItem4.addActionListener(new C1448u(this, iIndexAtLocation));
            jPopupMenu.add(jMenuItem4);
        }
        jPopupMenu.show(this, mouseEvent.getX(), mouseEvent.getY());
    }

    @Override // n.InterfaceC1761a
    public boolean a() {
        bH.C.c("TuneViewPanel Activated.");
        this.f9794k = true;
        if (this.f9796g.size() > 0) {
            b(this.f9796g);
            return true;
        }
        if (getTabCount() > 1) {
            return true;
        }
        g();
        return true;
    }

    @Override // n.n, javax.swing.JComponent
    public boolean isOptimizedDrawingEnabled() {
        return false;
    }

    @Override // n.g
    public void b() {
        bH.C.c("TuneViewPanel Deactivated.");
        this.f9794k = false;
    }

    @Override // G.S
    public void a(G.R r2) {
    }

    @Override // G.S
    public void b(G.R r2) {
        for (String str : this.f9790b.keySet()) {
            J j2 = (J) this.f9790b.get(str);
            b(str);
            if (j2 != null) {
                j2.close();
            }
        }
        this.f9797h.clear();
        this.f9798i.clear();
    }

    @Override // G.S
    public void c(G.R r2) {
    }

    public boolean f() {
        return this.f9794k;
    }
}
