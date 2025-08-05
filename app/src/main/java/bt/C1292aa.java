package bt;

import bH.C1007o;
import c.InterfaceC1385d;
import com.efiAnalytics.ui.InterfaceC1565bc;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import r.C1806i;
import s.C1818g;

/* renamed from: bt.aa, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/aa.class */
public class C1292aa extends C1348g implements G.aG, G.aN, InterfaceC1282J, InterfaceC1349h, InterfaceC1385d, InterfaceC1565bc, ItemListener {

    /* renamed from: a, reason: collision with root package name */
    protected aZ f8804a;

    /* renamed from: b, reason: collision with root package name */
    G.C f8805b;

    /* renamed from: c, reason: collision with root package name */
    G.R f8806c;

    /* renamed from: d, reason: collision with root package name */
    C1366y f8807d;

    /* renamed from: e, reason: collision with root package name */
    DefaultComboBoxModel f8809e;

    /* renamed from: i, reason: collision with root package name */
    private boolean f8808i = false;

    /* renamed from: j, reason: collision with root package name */
    private final Vector f8810j = new Vector();

    /* renamed from: f, reason: collision with root package name */
    C1295ad f8811f = new C1295ad(this);

    /* renamed from: g, reason: collision with root package name */
    List f8812g = new ArrayList();

    /* renamed from: h, reason: collision with root package name */
    FocusAdapter f8813h = new C1293ab(this);

    public C1292aa(G.R r2, G.C c2) {
        this.f8804a = null;
        this.f8806c = null;
        this.f8807d = null;
        this.f8809e = null;
        this.f8806c = r2;
        this.f8805b = c2;
        b_(c2.aH());
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout(3, 3));
        String strL = c2.l();
        strL = strL != null ? C1818g.b(strL) : strL;
        if (strL != null && strL.length() == 0) {
            strL = " ";
        }
        try {
            this.f8804a = (aZ) q.h.a().a(aZ.class);
            this.f8804a.setText(strL);
        } catch (Exception e2) {
            this.f8804a = new aZ(strL);
            bH.C.b("Failed to get SettingsLabel from cache, creating...");
        }
        jPanel.add(BorderLayout.CENTER, this.f8804a);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new GridLayout(1, 0, 2, 2));
        if (C1806i.a().a("lkjfgblkjgdoijre98u")) {
            jPanel2.add(new C1291a(this.f8806c, c2.a()));
        }
        jPanel2.add(new C1353l(this.f8806c, c2.a()));
        jPanel.add("West", jPanel2);
        this.f8807d = new C1366y();
        this.f8807d.b(2);
        for (int i2 = 0; i2 < 15; i2++) {
            C1294ac c1294ac = new C1294ac(this, i2);
            if (i2 != r2.O().x()) {
                this.f8810j.add(c1294ac);
            }
        }
        this.f8809e = new DefaultComboBoxModel(this.f8810j);
        this.f8807d.setModel(this.f8809e);
        this.f8807d.addFocusListener(this.f8813h);
        e();
        jPanel.add("East", this.f8807d);
        this.f8807d.addItemListener(this);
        try {
            G.aR.a().a(r2.c(), c2.a(), this);
        } catch (V.a e3) {
            Logger.getLogger(C1299ah.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
        }
        add("North", jPanel);
        r2.C().a(this);
        aH.b.a().a(this.f8811f);
        c();
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        this.f8807d.removeItemListener(this);
        this.f8807d.removeFocusListener(this.f8813h);
        q.h.a().a(this.f8804a);
        G.aR.a().a(this);
        aH.b.a().b(this.f8811f);
        this.f8806c.C().b(this);
    }

    @Override // G.aN
    public void a(String str, String str2) {
        e();
    }

    private void e() {
        try {
            this.f8807d.setSelectedItem(a((int) this.f8806c.c(this.f8805b.a()).j(this.f8806c.h())));
        } catch (V.g e2) {
            Logger.getLogger(C1299ah.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getStateChange() == 1) {
            C1294ac c1294ac = (C1294ac) itemEvent.getItem();
            try {
                this.f8806c.c(this.f8805b.a()).a(this.f8806c.h(), c1294ac.a());
            } catch (V.g e2) {
                com.efiAnalytics.ui.bV.d(e2.getMessage(), this.f8807d);
            } catch (V.j e3) {
                com.efiAnalytics.ui.bV.d(e3.getMessage(), this.f8807d);
            }
        }
    }

    public void c() {
        aH.b.a().a(this.f8806c);
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        super.setEnabled(z2);
        a(this, z2);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void setFont(Font font) {
        super.setFont(font);
        if (this.f8807d != null) {
            this.f8807d.setFont(font);
        }
        if (this.f8804a != null) {
            this.f8804a.setFont(font);
        }
    }

    private void a(Container container, boolean z2) {
        Component[] components = container.getComponents();
        for (int i2 = 0; i2 < components.length; i2++) {
            components[i2].setEnabled(z2);
            if (components[i2] instanceof Container) {
                a((Container) components[i2], z2);
            }
        }
    }

    @Override // bt.InterfaceC1349h
    public void a() {
        if (a_() == null || this.f8807d == null) {
            return;
        }
        try {
            setEnabled(C1007o.a(a_(), this.f8806c));
        } catch (Exception e2) {
            if (!this.f8808i) {
                com.efiAnalytics.ui.bV.d("Invalid enable condition on field " + this.f8805b.l() + ":\n { " + a_() + " } ", this);
                this.f8808i = true;
            }
            bH.C.a(e2.getMessage());
        }
    }

    @Override // c.InterfaceC1385d
    public G.R b_() {
        return this.f8806c;
    }

    @Override // G.aG
    public boolean a(String str, G.bS bSVar) {
        c();
        return true;
    }

    @Override // G.aG
    public void a(String str) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public C1294ac a(int i2) {
        Iterator it = this.f8810j.iterator();
        while (it.hasNext()) {
            C1294ac c1294ac = (C1294ac) it.next();
            if (c1294ac.a() == i2) {
                return c1294ac;
            }
        }
        return null;
    }

    @Override // bt.InterfaceC1282J
    public void a(InterfaceC1281I interfaceC1281I) {
        this.f8812g.add(interfaceC1281I);
    }

    @Override // bt.InterfaceC1282J
    public void b(InterfaceC1281I interfaceC1281I) {
        this.f8812g.remove(interfaceC1281I);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(String str) {
        Iterator it = this.f8812g.iterator();
        while (it.hasNext()) {
            ((InterfaceC1281I) it.next()).b(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(String str) {
        Iterator it = this.f8812g.iterator();
        while (it.hasNext()) {
            ((InterfaceC1281I) it.next()).a(str);
        }
    }

    @Override // bt.InterfaceC1282J
    public String d() {
        return this.f8805b.a();
    }
}
