package aP;

import bt.C1348g;
import bt.C1366y;
import c.InterfaceC1385d;
import com.efiAnalytics.ui.InterfaceC1565bc;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aP/S.class */
public class S extends C1348g implements G.aG, InterfaceC1385d, InterfaceC1565bc, ItemListener {

    /* renamed from: a, reason: collision with root package name */
    protected bt.aZ f2773a;

    /* renamed from: b, reason: collision with root package name */
    G.R f2774b;

    /* renamed from: c, reason: collision with root package name */
    C1366y f2775c;

    /* renamed from: d, reason: collision with root package name */
    JButton f2776d;

    /* renamed from: e, reason: collision with root package name */
    DefaultComboBoxModel f2777e;

    /* renamed from: i, reason: collision with root package name */
    private final Vector f2778i = new Vector();

    /* renamed from: f, reason: collision with root package name */
    W f2779f = new W(this);

    /* renamed from: g, reason: collision with root package name */
    boolean f2780g = false;

    /* renamed from: h, reason: collision with root package name */
    List f2781h = new ArrayList();

    public S(G.R r2) {
        String strB;
        this.f2773a = null;
        this.f2774b = null;
        this.f2775c = null;
        this.f2777e = null;
        this.f2774b = r2;
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout(3, 3));
        strB = "Connected Device";
        strB = strB != null ? C1818g.b(strB) : "Connected Device";
        if (strB != null && strB.length() == 0) {
            strB = " ";
        }
        this.f2773a = new bt.aZ(strB);
        jPanel.add(BorderLayout.CENTER, this.f2773a);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new GridLayout(1, 0, 2, 2));
        try {
            this.f2776d = new JButton(null, new ImageIcon(com.efiAnalytics.ui.cO.a().a(com.efiAnalytics.ui.cO.f11127Q, this, 20)));
            this.f2776d.setPreferredSize(com.efiAnalytics.ui.eJ.a(24, 24));
            this.f2776d.addActionListener(new T(this));
            JPanel jPanel3 = new JPanel();
            jPanel3.setLayout(new FlowLayout());
            jPanel3.add(this.f2776d);
            jPanel2.add(jPanel3);
            jPanel.add("West", jPanel2);
        } catch (V.a e2) {
            e2.printStackTrace();
        }
        this.f2775c = new C1366y();
        this.f2775c.b(3);
        this.f2775c.setMinimumSize(com.efiAnalytics.ui.eJ.a(200, 54));
        this.f2775c.setPreferredSize(com.efiAnalytics.ui.eJ.a(200, 54));
        for (int i2 = 0; i2 < 15; i2++) {
            U u2 = new U(this, i2);
            if (i2 != r2.O().x()) {
                this.f2778i.add(u2);
            }
        }
        this.f2777e = new DefaultComboBoxModel(this.f2778i);
        this.f2775c.setModel(this.f2777e);
        jPanel.add("East", this.f2775c);
        this.f2775c.addItemListener(this);
        add("North", jPanel);
        r2.C().a(this);
        aH.b.a().a(this.f2779f);
        c();
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        this.f2775c.removeItemListener(this);
        aH.b.a().b(this.f2779f);
        this.f2774b.C().b(this);
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getStateChange() == 1) {
            U u2 = (U) itemEvent.getItem();
            u2.a();
            a(u2);
        }
    }

    public void c() {
        aH.b.a().a(this.f2774b);
        this.f2780g = true;
        this.f2775c.repaint();
        this.f2775c.validate();
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        super.setEnabled(z2);
        this.f2775c.setEnabled(z2);
        this.f2773a.setEnabled(z2);
        this.f2776d.setEnabled(z2);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void setFont(Font font) {
        super.setFont(font);
        if (this.f2775c != null) {
            this.f2775c.setFont(font);
        }
        if (this.f2773a != null) {
            this.f2773a.setFont(font);
        }
    }

    public int d() {
        if (this.f2775c.getSelectedItem() != null) {
            return ((U) this.f2775c.getSelectedItem()).a();
        }
        return -1;
    }

    public void a(int i2) {
        U uB = b(i2);
        this.f2775c.setSelectedItem(uB);
        a((Container) this.f2775c, true);
        if (uB == null || uB.b() == null || uB.b().d() == null || uB.b().d().isEmpty()) {
            this.f2775c.setToolTipText(null);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<html>Firmware: ").append(bH.W.b(bH.W.h(uB.b().a()), "?", "")).append("<br>");
        sb.append("Signature: ").append(uB.b().d());
        this.f2775c.setToolTipText(sb.toString());
    }

    private void a(Container container, boolean z2) {
        Component[] components = container.getComponents();
        for (int i2 = 0; i2 < components.length; i2++) {
            components[i2].setEnabled(z2);
            if (components[i2] instanceof Container) {
                a((Container) components[i2], z2);
            } else {
                components[i2].setEnabled(z2);
            }
        }
    }

    @Override // c.InterfaceC1385d
    public G.R b_() {
        return this.f2774b;
    }

    @Override // G.aG
    public boolean a(String str, G.bS bSVar) {
        c();
        return true;
    }

    @Override // G.aG
    public void a(String str) {
    }

    public void e() {
        boolean z2 = false;
        Iterator it = this.f2778i.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            U u2 = (U) it.next();
            if (u2.f2783b != null && u2.b().c()) {
                this.f2775c.setSelectedItem(u2);
                z2 = true;
                break;
            }
        }
        if (z2) {
            return;
        }
        this.f2775c.setSelectedIndex(0);
    }

    private void a(U u2) {
        Iterator it = this.f2781h.iterator();
        while (it.hasNext()) {
            ((V) it.next()).a(u2);
        }
        if (u2.b() == null || u2.b().d() == null || u2.b().d().isEmpty()) {
            this.f2775c.setToolTipText(null);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<html>Firmware: ").append(bH.W.b(bH.W.h(u2.b().a()), "?", "")).append("<br>");
        sb.append("Signature: ").append(u2.b().d());
        this.f2775c.setToolTipText(sb.toString());
    }

    public void a(V v2) {
        if (this.f2781h.contains(v2)) {
            return;
        }
        this.f2781h.add(v2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public U b(int i2) {
        Iterator it = this.f2778i.iterator();
        while (it.hasNext()) {
            U u2 = (U) it.next();
            if (u2.a() == i2) {
                return u2;
            }
        }
        return null;
    }
}
