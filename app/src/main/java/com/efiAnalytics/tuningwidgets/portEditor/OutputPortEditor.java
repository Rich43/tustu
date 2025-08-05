package com.efiAnalytics.tuningwidgets.portEditor;

import G.C0083bp;
import G.R;
import G.aM;
import G.aS;
import bH.C1007o;
import bH.W;
import bt.C1291a;
import bt.C1353l;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.sun.xml.internal.ws.model.RuntimeModeler;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.MenuContainer;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import org.icepdf.core.util.PdfOps;
import r.C1806i;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/portEditor/OutputPortEditor.class */
public class OutputPortEditor extends JPanel implements InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    R f10530a;

    /* renamed from: o, reason: collision with root package name */
    private aM f10531o;

    /* renamed from: p, reason: collision with root package name */
    private aM f10532p;

    /* renamed from: q, reason: collision with root package name */
    private aM f10533q;

    /* renamed from: r, reason: collision with root package name */
    private aM f10534r;

    /* renamed from: s, reason: collision with root package name */
    private aM f10535s;

    /* renamed from: t, reason: collision with root package name */
    private aM f10536t;

    /* renamed from: b, reason: collision with root package name */
    h f10539b;

    /* renamed from: c, reason: collision with root package name */
    s f10540c;

    /* renamed from: d, reason: collision with root package name */
    s f10541d;

    /* renamed from: e, reason: collision with root package name */
    g f10542e;

    /* renamed from: f, reason: collision with root package name */
    g f10543f;

    /* renamed from: g, reason: collision with root package name */
    j f10544g;

    /* renamed from: h, reason: collision with root package name */
    aS f10545h;

    /* renamed from: m, reason: collision with root package name */
    JButton f10550m;

    /* renamed from: n, reason: collision with root package name */
    JButton f10551n;

    /* renamed from: u, reason: collision with root package name */
    private List f10537u = new ArrayList();

    /* renamed from: v, reason: collision with root package name */
    private l f10538v = new l(this);

    /* renamed from: i, reason: collision with root package name */
    JLabel f10546i = new JLabel(C1818g.b("Active Delay"), 0);

    /* renamed from: j, reason: collision with root package name */
    JLabel f10547j = new JLabel(C1818g.b("InActive Delay"), 0);

    /* renamed from: k, reason: collision with root package name */
    List f10548k = new ArrayList();

    /* renamed from: l, reason: collision with root package name */
    List f10549l = new ArrayList();

    public OutputPortEditor(R r2, aS aSVar) throws IllegalArgumentException {
        this.f10530a = null;
        this.f10531o = null;
        this.f10532p = null;
        this.f10533q = null;
        this.f10534r = null;
        this.f10535s = null;
        this.f10536t = null;
        this.f10539b = null;
        this.f10540c = null;
        this.f10541d = null;
        this.f10542e = null;
        this.f10543f = null;
        this.f10544g = null;
        this.f10545h = null;
        this.f10550m = null;
        this.f10551n = null;
        this.f10545h = aSVar;
        this.f10530a = r2;
        this.f10531o = r2.c(aSVar.d());
        this.f10532p = r2.c(aSVar.h());
        this.f10533q = r2.c(aSVar.i());
        this.f10534r = r2.c(aSVar.j());
        this.f10535s = r2.c(aSVar.t());
        this.f10536t = r2.c(aSVar.u());
        Iterator it = aSVar.v().iterator();
        while (it.hasNext()) {
            this.f10537u.add(r2.c((String) it.next()));
        }
        Iterator it2 = aSVar.w().iterator();
        while (it2.hasNext()) {
            this.f10548k.add(new JLabel(C1818g.b((String) it2.next()), 0));
        }
        a();
        setLayout(new BorderLayout(5, 5));
        JPanel jPanel = new JPanel();
        jPanel.setBorder(BorderFactory.createTitledBorder(C1818g.b("Output Port")));
        this.f10551n = c();
        jPanel.setLayout(new GridLayout());
        int i2 = 0;
        Iterator itA = aSVar.a();
        while (itA.hasNext()) {
            String str = (String) itA.next();
            boolean zA = true;
            try {
                zA = C1007o.a(aSVar.a(i2), r2);
            } catch (V.g e2) {
                Logger.getLogger(OutputPortEditor.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            if (zA && !str.equals("INVALID") && !str.equals("\"INVALID\"")) {
                this.f10538v.a(new o(this, r2, aSVar.d(), str, i2));
            }
            i2++;
        }
        this.f10538v.setSelectedIndex(0);
        this.f10538v.a(new C1529a(this));
        this.f10538v.setBackground(getBackground());
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout());
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new BorderLayout());
        jPanel3.add("West", new JLabel(C1818g.b(RuntimeModeler.PORT)));
        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new FlowLayout(2));
        jPanel4.add(new C1353l(r2, aSVar.d()));
        if (C1806i.a().a("HF-0FD-0HHFJG")) {
            jPanel4.add(new C1291a(r2, aSVar.d()));
            this.f10550m = b();
            jPanel4.add(this.f10550m);
            jPanel4.add(this.f10551n);
            f();
        }
        jPanel3.add(BorderLayout.CENTER, jPanel4);
        jPanel2.add("North", jPanel3);
        jPanel2.add(BorderLayout.CENTER, new JScrollPane(this.f10538v));
        jPanel.add(jPanel2);
        add("West", jPanel);
        JPanel jPanel5 = new JPanel();
        jPanel5.setLayout(new BorderLayout());
        JPanel jPanel6 = new JPanel();
        jPanel6.setBorder(BorderFactory.createTitledBorder(C1818g.b("Port Settings")));
        jPanel6.setLayout(new FlowLayout(1, 10, 10));
        this.f10539b = new h(this, r2, this.f10531o.aJ(), C1818g.b("Enabled"));
        this.f10539b.addItemListener(new C1530b(this));
        JPanel jPanel7 = new JPanel();
        jPanel7.setLayout(new GridLayout(0, 1));
        jPanel7.add(new JLabel(""));
        jPanel7.add(this.f10539b);
        jPanel6.add(jPanel7);
        this.f10540c = new s(r2, aSVar.k(), C1818g.b("Power On Value"), false);
        jPanel6.add(this.f10540c);
        this.f10541d = new s(r2, aSVar.l(), C1818g.b("Active Value"), false);
        jPanel6.add(this.f10541d);
        if (aSVar.t() != null && r2.c(aSVar.t()) != null) {
            JPanel jPanel8 = new JPanel();
            jPanel8.setLayout(new GridLayout(0, 1));
            jPanel8.add(this.f10546i);
            C0083bp c0083bp = new C0083bp();
            c0083bp.a(aSVar.t());
            this.f10542e = new g(this, r2, c0083bp);
            jPanel8.add(this.f10542e);
            jPanel6.add(jPanel8);
        }
        if (aSVar.u() != null && r2.c(aSVar.u()) != null) {
            JPanel jPanel9 = new JPanel();
            jPanel9.setLayout(new GridLayout(0, 1));
            jPanel9.add(this.f10547j);
            C0083bp c0083bp2 = new C0083bp();
            c0083bp2.a(aSVar.u());
            this.f10543f = new g(this, r2, c0083bp2);
            jPanel9.add(this.f10543f);
            jPanel6.add(jPanel9);
        }
        for (int i3 = 0; i3 < this.f10537u.size(); i3++) {
            aM aMVar = (aM) this.f10537u.get(i3);
            if (aMVar != null) {
                JPanel jPanel10 = new JPanel();
                jPanel10.setLayout(new GridLayout(0, 1));
                if (this.f10548k.size() > i3) {
                    jPanel10.add((Component) this.f10548k.get(i3));
                } else {
                    jPanel10.add(new JLabel(C1818g.b("Attribute " + i3), 0));
                }
                C0083bp c0083bp3 = new C0083bp();
                c0083bp3.a(aMVar.aJ());
                g gVar = new g(this, r2, c0083bp3);
                jPanel10.add(gVar);
                this.f10549l.add(gVar);
                jPanel6.add(jPanel10);
            }
        }
        jPanel5.add("North", jPanel6);
        C1534f c1534f = new C1534f(this);
        c1534f.setLayout(new FlowLayout(1, 6, 6));
        c1534f.setBorder(BorderFactory.createTitledBorder(C1818g.b("Active Conditions")));
        this.f10544g = new j(this, r2, aSVar);
        c1534f.add(this.f10544g);
        jPanel5.add(BorderLayout.CENTER, c1534f);
        add(BorderLayout.CENTER, jPanel5);
        a(0);
        a(this.f10539b.isSelected());
        SwingUtilities.invokeLater(new RunnableC1531c(this));
    }

    private void a() {
        if (this.f10535s == null && this.f10536t == null) {
            return;
        }
        try {
            double[][] dArrI = this.f10531o.i(this.f10530a.h());
            if (this.f10535s != null) {
                double[][] dArrI2 = this.f10535s.i(this.f10530a.h());
                for (int i2 = 0; i2 < dArrI.length; i2++) {
                    if (dArrI[i2][0] == 0.0d) {
                        dArrI2[i2][0] = 0.0d;
                    }
                }
                try {
                    this.f10535s.a(this.f10530a.h(), dArrI2);
                } catch (V.j e2) {
                    Logger.getLogger(OutputPortEditor.class.getName()).log(Level.SEVERE, "Zero should be supported by onDelay", (Throwable) e2);
                }
            }
            if (this.f10536t != null) {
                double[][] dArrI3 = this.f10536t.i(this.f10530a.h());
                for (int i3 = 0; i3 < dArrI.length; i3++) {
                    if (dArrI[i3][0] == 0.0d) {
                        dArrI3[i3][0] = 0.0d;
                    }
                }
                try {
                    this.f10536t.a(this.f10530a.h(), dArrI3);
                } catch (V.j e3) {
                    Logger.getLogger(OutputPortEditor.class.getName()).log(Level.SEVERE, "Zero should be supported by offDelay", (Throwable) e3);
                }
            }
        } catch (V.g e4) {
            bH.C.a(e4);
        }
    }

    private JButton b() {
        JButton jButton = new JButton(null, new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("rename.png"))));
        jButton.setFocusable(false);
        jButton.setToolTipText(C1818g.b("Set an alias name for selected port"));
        jButton.addActionListener(new C1532d(this));
        jButton.setPreferredSize(new Dimension(18, 18));
        return jButton;
    }

    private JButton c() {
        JButton jButton = new JButton(null, new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("eraser16.png"))));
        jButton.setFocusable(false);
        jButton.setToolTipText(C1818g.b("Clear alias name for selected port, return to default naming."));
        jButton.addActionListener(new C1533e(this));
        jButton.setPreferredSize(new Dimension(18, 18));
        jButton.setEnabled(false);
        return jButton;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        String strB = W.b(((o) this.f10538v.a()).a(), PdfOps.DOUBLE_QUOTE__TOKEN, "");
        String strB2 = C1818g.b(strB);
        String strShowInputDialog = JOptionPane.showInputDialog(this, C1818g.b("Enter Alias for") + " " + strB2, strB2);
        if (strShowInputDialog != null && !strShowInputDialog.isEmpty()) {
            C1818g.c(strB, strShowInputDialog);
        }
        this.f10538v.repaint();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e() {
        C1818g.d(W.b(((o) this.f10538v.a()).a(), PdfOps.DOUBLE_QUOTE__TOKEN, ""));
        this.f10538v.repaint();
    }

    protected void a(String str) {
        for (int i2 = 0; i2 < this.f10538v.b(); i2++) {
            o oVar = (o) this.f10538v.a(i2);
            if (str.equals(oVar.a())) {
                a(oVar.f10583e);
                f();
                return;
            }
        }
    }

    private void f() {
        this.f10551n.setEnabled(C1818g.e(W.b(((o) this.f10538v.a()).a(), PdfOps.DOUBLE_QUOTE__TOKEN, "")));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(boolean z2) throws IllegalArgumentException {
        this.f10540c.setEnabled(z2);
        this.f10541d.setEnabled(z2);
        this.f10544g.setEnabled(z2);
        if (this.f10542e != null) {
            this.f10542e.setEnabled(z2);
            this.f10546i.setEnabled(z2);
        }
        if (this.f10543f != null) {
            this.f10543f.setEnabled(z2);
            this.f10547j.setEnabled(z2);
        }
        Iterator it = this.f10549l.iterator();
        while (it.hasNext()) {
            ((g) it.next()).setEnabled(z2);
        }
        Iterator it2 = this.f10548k.iterator();
        while (it2.hasNext()) {
            ((JLabel) it2.next()).setEnabled(z2);
        }
    }

    protected void a(int i2) {
        this.f10539b.a(i2);
        this.f10539b.b();
        this.f10540c.b(i2);
        this.f10541d.b(i2);
        this.f10544g.a(i2);
        if (this.f10542e != null) {
            this.f10542e.c(i2);
        }
        if (this.f10543f != null) {
            this.f10543f.c(i2);
        }
        Iterator it = this.f10549l.iterator();
        while (it.hasNext()) {
            ((g) it.next()).c(i2);
        }
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        for (int i2 = 0; i2 < getComponentCount(); i2++) {
            MenuContainer component = getComponent(i2);
            if (component instanceof InterfaceC1565bc) {
                ((InterfaceC1565bc) component).close();
            }
        }
        this.f10539b.close();
        if (this.f10533q != null) {
            this.f10533q.b(1.0d);
            this.f10533q.c(0.0d);
        }
        if (this.f10534r != null) {
            this.f10534r.b(1.0d);
            this.f10534r.c(0.0d);
        }
        if (this.f10542e != null) {
            this.f10542e.close();
        }
        if (this.f10543f != null) {
            this.f10543f.close();
        }
        Iterator it = this.f10549l.iterator();
        while (it.hasNext()) {
            ((g) it.next()).close();
        }
    }
}
