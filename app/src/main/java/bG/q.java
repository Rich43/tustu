package bG;

import W.ap;
import W.ar;
import bH.H;
import bH.R;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.cO;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.security.tools.policytool.ToolWindow;

/* loaded from: TunerStudioMS.jar:bG/q.class */
public class q extends JToolBar implements j {

    /* renamed from: a, reason: collision with root package name */
    m f6974a;

    /* renamed from: b, reason: collision with root package name */
    ap f6975b;

    /* renamed from: c, reason: collision with root package name */
    ArrayList f6976c = new ArrayList();

    public q(m mVar) {
        this.f6974a = mVar;
        b();
        this.f6975b = new ar(aE.a.A(), "TriggerWheelewditor_" + mVar.getName());
        mVar.a(this);
        mVar.setFocusable(true);
        mVar.addKeyListener(new D(this));
    }

    private JToolBar b() {
        setFloatable(false);
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(0, 1, 3, 3));
        add("North", jPanel);
        try {
            JButton jButton = new JButton(null, new ImageIcon(cO.a().a(cO.f11116F)));
            jButton.setFocusable(false);
            jButton.setToolTipText("Create new Trigger Wheel");
            jButton.addActionListener(new r(this));
            jButton.setPreferredSize(new Dimension(28, 28));
            jPanel.add(jButton);
        } catch (V.a e2) {
            Logger.getLogger(q.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        try {
            JButton jButton2 = new JButton(null, new ImageIcon(cO.a().a(cO.f11088d)));
            jButton2.setFocusable(false);
            jButton2.setToolTipText("Add a Trigger Tooth");
            jButton2.addActionListener(new v(this));
            jButton2.setPreferredSize(new Dimension(28, 28));
            jPanel.add(jButton2);
        } catch (V.a e3) {
            Logger.getLogger(q.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
        }
        try {
            JButton jButton3 = new JButton(null, new ImageIcon(cO.a().a(cO.f11121K)));
            jButton3.setFocusable(false);
            jButton3.setToolTipText("Resize Selected Teeth");
            jButton3.addActionListener(new w(this));
            jButton3.setPreferredSize(new Dimension(28, 28));
            jPanel.add(jButton3);
            this.f6976c.add(jButton3);
        } catch (V.a e4) {
            Logger.getLogger(q.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
        }
        try {
            JButton jButton4 = new JButton(null, new ImageIcon(cO.a().a(cO.f11117G)));
            jButton4.setFocusable(false);
            jButton4.setToolTipText("Move Selected Teeth");
            jButton4.addActionListener(new x(this));
            jButton4.setPreferredSize(new Dimension(28, 28));
            jPanel.add(jButton4);
            this.f6976c.add(jButton4);
        } catch (V.a e5) {
            Logger.getLogger(q.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
        }
        try {
            JButton jButton5 = new JButton(null, new ImageIcon(cO.a().a(cO.f11089e)));
            jButton5.setFocusable(false);
            jButton5.setToolTipText("Delete Selected Teeth");
            jButton5.addActionListener(new y(this));
            jButton5.setPreferredSize(new Dimension(28, 28));
            jPanel.add(jButton5);
            this.f6976c.add(jButton5);
        } catch (V.a e6) {
            Logger.getLogger(q.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e6);
        }
        a(new ArrayList());
        return this;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        C0989d c0989d = new C0989d(bV.b(this), this.f6975b);
        bV.a(this, c0989d);
        c0989d.setVisible(true);
        if (c0989d.d()) {
            int iA = c0989d.a();
            int iB = c0989d.b();
            double dC = c0989d.c();
            bH.C.c("Teeth = " + iA + ", NumMissingTeeth=" + iB + ", width=" + dC);
            C0986a c0986a = new C0986a(iA, iB);
            if (!Double.isNaN(dC) && dC > 0.0d) {
                c0986a.a(dC);
            }
            l lVarE = this.f6974a.e();
            if (lVarE instanceof C0987b) {
                ((C0987b) lVarE).a(c0986a.a());
                this.f6974a.repaint();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() throws NumberFormatException {
        String strA;
        l lVarE = this.f6974a.e();
        if (!(lVarE instanceof C0987b) || (strA = bV.a("{Position in Degrees of new tooth:}:{Size in Degrees of new tooth:}", true, "Add a tooth.", true, (Component) this)) == null || strA.isEmpty() || strA.equals(CallSiteDescriptor.TOKEN_DELIMITER)) {
            return;
        }
        String[] strArrSplit = strA.split(CallSiteDescriptor.TOKEN_DELIMITER);
        if (strArrSplit.length != 2) {
            return;
        }
        double d2 = Double.parseDouble(strArrSplit[0]);
        double d3 = Double.parseDouble(strArrSplit[1]);
        double d4 = d2 % 360.0d;
        C0987b c0987b = (C0987b) lVarE;
        k kVar = new k();
        kVar.a(d4);
        kVar.b(d3);
        c0987b.a().add(kVar);
        b(c0987b.a());
        c0987b.a(c0987b.a());
        this.f6974a.repaint();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e() {
        double d2 = 1.0d;
        l lVarE = this.f6974a.e();
        if (lVarE instanceof C0987b) {
            d2 = ((C0987b) lVarE).d();
        }
        a(-d2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f() {
        double d2 = 1.0d;
        l lVarE = this.f6974a.e();
        if (lVarE instanceof C0987b) {
            d2 = ((C0987b) lVarE).d();
        }
        a(d2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g() {
        String strA;
        if ((this.f6974a.e() instanceof C0987b) && (strA = bV.a("{Number of Degrees to shift selected teeth.}", true, "Number of Degrees to shift selected teeth.", true, (Component) this)) != null && H.a(strA)) {
            a(Double.parseDouble(strA));
        }
    }

    private void a(double d2) {
        l lVarE = this.f6974a.e();
        if (lVarE instanceof C0987b) {
            double d3 = d2 % 360.0d;
            C0987b c0987b = (C0987b) lVarE;
            Integer[] numArrA = R.a(this.f6974a.h());
            for (int length = numArrA.length - 1; length >= 0; length--) {
                if (numArrA[length].intValue() < c0987b.a().size()) {
                    k kVar = (k) c0987b.a().get(numArrA[length].intValue());
                    if (kVar.a() + d3 < 0.0d) {
                        d3 = 360.0d + d3;
                    }
                    kVar.a((kVar.a() + d3) % 360.0d);
                }
            }
            b(c0987b.a());
            c0987b.a(c0987b.a());
            this.f6974a.repaint();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void h() {
        String strA;
        if ((this.f6974a.e() instanceof C0987b) && (strA = bV.a("{Set selected teeth width in degrees }", true, "Resize selected teeth.", true, (Component) this)) != null && H.a(strA)) {
            b(Double.parseDouble(strA));
        }
    }

    private void b(double d2) {
        l lVarE = this.f6974a.e();
        if (lVarE instanceof C0987b) {
            double d3 = d2 % 360.0d;
            if (d3 <= 0.0d) {
                bV.d("Size must be greater than zero.", this);
                return;
            }
            C0987b c0987b = (C0987b) lVarE;
            Integer[] numArrA = R.a(this.f6974a.h());
            for (int length = numArrA.length - 1; length >= 0; length--) {
                ((k) c0987b.a().get(numArrA[length].intValue())).b(d3);
            }
            c0987b.a(c0987b.a());
            this.f6974a.repaint();
        }
    }

    public JMenu a() {
        JMenu jMenu = new JMenu(ToolWindow.EDIT_KEYSTORE);
        JMenuItem jMenuItem = new JMenuItem("New Wheel");
        jMenuItem.addActionListener(new z(this));
        jMenu.add(jMenuItem);
        JMenuItem jMenuItem2 = new JMenuItem("Add Tooth");
        jMenuItem2.addActionListener(new A(this));
        jMenu.add(jMenuItem2);
        JMenuItem jMenuItem3 = new JMenuItem("Move Selected Teeth");
        jMenuItem3.addActionListener(new B(this));
        jMenu.add(jMenuItem3);
        this.f6976c.add(jMenuItem3);
        JMenuItem jMenuItem4 = new JMenuItem("Resize Selected Teeth");
        jMenuItem4.addActionListener(new C(this));
        jMenu.add(jMenuItem4);
        this.f6976c.add(jMenuItem4);
        JMenuItem jMenuItem5 = new JMenuItem("Delete Selected Teeth");
        jMenuItem5.addActionListener(new s(this));
        jMenu.add(jMenuItem5);
        this.f6976c.add(jMenuItem5);
        jMenu.addSeparator();
        JMenuItem jMenuItem6 = new JMenuItem("Select All Teeth");
        jMenuItem6.addActionListener(new t(this));
        jMenu.add(jMenuItem6);
        JMenuItem jMenuItem7 = new JMenuItem("Clear Selected Teeth");
        jMenuItem7.addActionListener(new u(this));
        jMenu.add(jMenuItem7);
        this.f6976c.add(jMenuItem7);
        a(new ArrayList());
        return jMenu;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void i() {
        l lVarE = this.f6974a.e();
        if (lVarE instanceof C0987b) {
            C0987b c0987b = (C0987b) lVarE;
            Integer[] numArrA = R.a(this.f6974a.h());
            for (int length = numArrA.length - 1; length >= 0; length--) {
                c0987b.a(numArrA[length].intValue());
            }
            c0987b.a(c0987b.a());
            this.f6974a.i();
            this.f6974a.repaint();
        }
    }

    private List b(List list) {
        for (int i2 = 0; i2 < list.size(); i2++) {
            for (int i3 = i2 + 1; i3 < list.size(); i3++) {
                k kVar = (k) list.get(i2);
                k kVar2 = (k) list.get(i3);
                if (kVar.a() > kVar2.a()) {
                    list.set(i2, kVar2);
                    list.set(i3, kVar);
                }
            }
        }
        return list;
    }

    @Override // bG.j
    public void a(List list) {
        boolean z2 = (list == null || list.isEmpty()) ? false : true;
        Iterator it = this.f6976c.iterator();
        while (it.hasNext()) {
            ((JComponent) it.next()).setEnabled(z2);
        }
    }
}
