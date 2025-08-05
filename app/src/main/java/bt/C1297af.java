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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import r.C1806i;
import s.C1818g;

/* renamed from: bt.af, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/af.class */
public class C1297af extends C1348g implements G.aN, InterfaceC1282J, bX, InterfaceC1349h, InterfaceC1385d, InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    protected aZ f8820a;

    /* renamed from: b, reason: collision with root package name */
    G.D f8821b;

    /* renamed from: c, reason: collision with root package name */
    G.R f8822c;

    /* renamed from: d, reason: collision with root package name */
    C1366y f8823d;

    /* renamed from: g, reason: collision with root package name */
    private boolean f8824g = false;

    /* renamed from: e, reason: collision with root package name */
    List f8825e = new ArrayList();

    /* renamed from: f, reason: collision with root package name */
    FocusAdapter f8826f = new C1298ag(this);

    public C1297af(G.R r2, G.D d2) {
        this.f8820a = null;
        this.f8822c = null;
        this.f8823d = null;
        this.f8822c = r2;
        this.f8821b = d2;
        b_(d2.aH());
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout(3, 3));
        String strL = d2.l();
        strL = strL != null ? C1818g.b(strL) : strL;
        if (strL != null && strL.length() == 0) {
            strL = " ";
        }
        try {
            this.f8820a = (aZ) q.h.a().a(aZ.class);
            this.f8820a.setText(strL);
        } catch (Exception e2) {
            this.f8820a = new aZ(strL);
            bH.C.b("Failed to get SettingsLabel from cache, creating...");
        }
        jPanel.add(BorderLayout.CENTER, this.f8820a);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new GridLayout(1, 0, 2, 2));
        if (C1806i.a().a("lkjfgblkjgdoijre98u")) {
            jPanel2.add(new C1291a(this.f8822c, d2.a()));
        }
        jPanel2.add(new C1353l(this.f8822c, d2.a()));
        jPanel.add("West", jPanel2);
        try {
            this.f8823d = (C1366y) q.h.a().a(C1366y.class);
        } catch (Exception e3) {
            this.f8823d = new C1366y();
            bH.C.b("Cache Failed, creating new UI Component");
        }
        for (String str : (String[]) bH.R.a((Object[]) G.T.a().d())) {
            this.f8823d.a(G.T.a().c(str).O().x() + "", str);
        }
        c();
        jPanel.add("East", this.f8823d);
        this.f8823d.a(this);
        this.f8823d.addFocusListener(this.f8826f);
        try {
            G.aR.a().a(r2.c(), d2.a(), this);
        } catch (V.a e4) {
            Logger.getLogger(C1299ah.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
        }
        add("North", jPanel);
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        this.f8823d.removeFocusListener(this.f8826f);
        this.f8823d.b(this);
        q.h.a().a(this.f8820a);
        q.h.a().a(this.f8823d);
        G.aR.a().a(this);
    }

    @Override // G.aN
    public void a(String str, String str2) {
        c();
    }

    private void c() {
        try {
            int iJ = (int) this.f8822c.c(this.f8821b.a()).j(this.f8822c.h());
            for (int i2 = 0; i2 < this.f8823d.getItemCount(); i2++) {
                if (iJ == Integer.parseInt(this.f8823d.a(i2))) {
                    this.f8823d.setSelectedIndex(i2);
                    return;
                }
            }
        } catch (V.g e2) {
            Logger.getLogger(C1299ah.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    @Override // bt.bX
    public void b(String str) throws NumberFormatException {
        try {
            this.f8822c.c(this.f8821b.a()).a(this.f8822c.h(), Integer.parseInt(str));
        } catch (V.g e2) {
            com.efiAnalytics.ui.bV.d(e2.getMessage(), this.f8823d);
        } catch (V.j e3) {
            com.efiAnalytics.ui.bV.d(e3.getMessage(), this.f8823d);
        }
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        super.setEnabled(z2);
        a(this, z2);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void setFont(Font font) {
        super.setFont(font);
        if (this.f8823d != null) {
            this.f8823d.setFont(font);
        }
        if (this.f8820a != null) {
            this.f8820a.setFont(font);
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
        if (a_() == null || this.f8823d == null) {
            return;
        }
        try {
            setEnabled(C1806i.a().a("HF;'[PG-05") ? C1007o.a(a_(), this.f8822c) : false);
        } catch (Exception e2) {
            if (!this.f8824g) {
                com.efiAnalytics.ui.bV.d("Invalid enable condition on field " + this.f8821b.l() + ":\n { " + a_() + " } ", this);
                this.f8824g = true;
            }
            bH.C.a(e2.getMessage());
        }
    }

    @Override // c.InterfaceC1385d
    public G.R b_() {
        return this.f8822c;
    }

    @Override // bt.InterfaceC1282J
    public void a(InterfaceC1281I interfaceC1281I) {
        this.f8825e.add(interfaceC1281I);
    }

    @Override // bt.InterfaceC1282J
    public void b(InterfaceC1281I interfaceC1281I) {
        this.f8825e.remove(interfaceC1281I);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(String str) {
        Iterator it = this.f8825e.iterator();
        while (it.hasNext()) {
            ((InterfaceC1281I) it.next()).b(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d(String str) {
        Iterator it = this.f8825e.iterator();
        while (it.hasNext()) {
            ((InterfaceC1281I) it.next()).a(str);
        }
    }

    @Override // bt.InterfaceC1282J
    public String d() {
        return this.f8821b.a();
    }
}
