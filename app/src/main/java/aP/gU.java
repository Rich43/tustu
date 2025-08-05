package aP;

import bq.C1220b;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import r.C1798a;
import r.C1806i;

/* loaded from: TunerStudioMS.jar:aP/gU.class */
class gU extends JToolBar implements bA.d {

    /* renamed from: a, reason: collision with root package name */
    gB f3441a;

    /* renamed from: e, reason: collision with root package name */
    boolean f3445e;

    /* renamed from: g, reason: collision with root package name */
    final /* synthetic */ C0308dx f3447g;

    /* renamed from: b, reason: collision with root package name */
    JPanel f3442b = new JPanel();

    /* renamed from: c, reason: collision with root package name */
    JPanel f3443c = new JPanel();

    /* renamed from: d, reason: collision with root package name */
    Map f3444d = new HashMap();

    /* renamed from: f, reason: collision with root package name */
    String f3446f = null;

    gU(C0308dx c0308dx) {
        this.f3447g = c0308dx;
        this.f3441a = new gB(this.f3447g);
        this.f3445e = false;
        this.f3445e = C1806i.a().a("HF;'[PG-05");
        this.f3442b.setLayout(new GridLayout(1, 1));
        setLayout(new BorderLayout(4, 4));
        add(BorderLayout.CENTER, this.f3443c);
        this.f3443c.setLayout(new CardLayout());
        this.f3441a.a(new gV(this, c0308dx));
    }

    private JPanel c(String str) {
        if (!this.f3445e) {
            if (G.T.a().c() != null) {
                str = G.T.a().c().c();
                this.f3446f = str;
            } else {
                str = this.f3446f;
            }
        }
        JPanel jPanel = (JPanel) this.f3444d.get(str);
        if (jPanel == null) {
            jPanel = new JPanel();
            if (getOrientation() == 0) {
                jPanel.setLayout(new com.efiAnalytics.ui.cR(1, 0, 2, 2));
            } else {
                jPanel.setLayout(new com.efiAnalytics.ui.cR(0, 1, 2, 2));
            }
            this.f3441a.a(str);
            this.f3443c.add(jPanel, str);
            this.f3444d.put(str, jPanel);
        }
        if (!this.f3445e || this.f3444d.size() <= 1) {
            a(false);
        } else {
            a(true);
        }
        return jPanel;
    }

    @Override // bA.d
    public int a(String str) {
        if (!this.f3445e) {
            if (G.T.a().c() != null) {
                str = G.T.a().c().c();
                this.f3446f = str;
            } else {
                str = this.f3446f;
            }
        }
        if (this.f3444d.isEmpty()) {
            return 0;
        }
        return c(str).getComponentCount();
    }

    @Override // bA.d
    public Component a(String str, Component component, int i2) {
        c(str).add(component);
        if (component instanceof C1220b) {
            ((C1220b) component).b(getOrientation());
        }
        return component;
    }

    @Override // javax.swing.JToolBar
    public void setOrientation(int i2) {
        if (this.f3443c != null) {
            if (i2 == 1) {
                add("North", this.f3442b);
            } else {
                add("West", this.f3442b);
            }
            if (i2 == 1) {
                C1798a.a().b("tuningToolbarLocation", "West");
                for (JPanel jPanel : this.f3444d.values()) {
                    if (jPanel.getLayout() instanceof GridLayout) {
                        GridLayout gridLayout = (GridLayout) jPanel.getLayout();
                        gridLayout.setColumns(1);
                        gridLayout.setRows(0);
                    } else if (jPanel.getLayout() instanceof com.efiAnalytics.ui.cR) {
                        com.efiAnalytics.ui.cR cRVar = (com.efiAnalytics.ui.cR) jPanel.getLayout();
                        cRVar.b(1);
                        cRVar.a(0);
                    }
                }
            } else {
                C1798a.a().b("tuningToolbarLocation", "North");
                for (JPanel jPanel2 : this.f3444d.values()) {
                    if (jPanel2.getLayout() instanceof GridLayout) {
                        GridLayout gridLayout2 = (GridLayout) jPanel2.getLayout();
                        gridLayout2.setRows(1);
                        gridLayout2.setColumns(0);
                    } else if (jPanel2.getLayout() instanceof com.efiAnalytics.ui.cR) {
                        com.efiAnalytics.ui.cR cRVar2 = (com.efiAnalytics.ui.cR) jPanel2.getLayout();
                        cRVar2.a(1);
                        cRVar2.b(0);
                    }
                }
            }
            if (this.f3447g.f3270h != null && this.f3447g.f3270h.isVisible() && (this.f3447g.f3270h instanceof aU)) {
                ((aU) this.f3447g.f3270h).a();
            }
            Iterator it = this.f3444d.values().iterator();
            while (it.hasNext()) {
                for (Component component : ((JPanel) it.next()).getComponents()) {
                    if (component instanceof C1220b) {
                        ((C1220b) component).b(i2);
                    }
                }
            }
        }
        super.setOrientation(i2);
    }

    @Override // bA.d
    public Component getComponent() {
        return this;
    }

    @Override // bA.d
    public bA.f a(String str, int i2) {
        if (!this.f3445e) {
            if (G.T.a().c() != null) {
                str = G.T.a().c().c();
                this.f3446f = str;
            } else {
                str = this.f3446f;
            }
        }
        return (bA.f) c(str).getComponent(i2);
    }

    @Override // bA.d
    public void a(Component component) {
        for (String str : (String[]) this.f3444d.keySet().toArray(new String[this.f3444d.keySet().size()])) {
            JPanel jPanel = (JPanel) this.f3444d.get(str);
            jPanel.remove(component);
            if (jPanel.getComponentCount() == 0) {
                this.f3443c.remove(jPanel);
                this.f3444d.remove(str);
            }
        }
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        for (JPanel jPanel : this.f3444d.values()) {
            for (int i2 = 0; i2 < jPanel.getComponentCount(); i2++) {
                jPanel.getComponent(i2).setEnabled(z2);
            }
        }
        this.f3443c.setEnabled(z2);
    }

    @Override // bA.d
    public bA.b a() {
        return this.f3441a;
    }

    @Override // bA.d
    public int b() {
        int componentCount = 0;
        Iterator it = this.f3444d.values().iterator();
        while (it.hasNext()) {
            componentCount += ((JPanel) it.next()).getComponentCount();
        }
        return componentCount;
    }

    public void a(boolean z2) {
        if (z2) {
            if (this.f3442b.getComponentCount() == 0) {
                this.f3442b.add(this.f3441a);
            }
        } else if (this.f3442b.getComponentCount() > 0) {
            this.f3442b.remove(this.f3441a);
        }
    }

    @Override // bA.d
    public void b(String str) {
        if (!this.f3445e) {
            str = G.T.a().c().c();
        }
        JPanel jPanelC = c(str);
        jPanelC.removeAll();
        this.f3443c.remove(jPanelC);
        this.f3444d.remove(str);
        this.f3441a.c(str);
    }
}
