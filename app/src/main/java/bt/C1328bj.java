package bt;

import G.C0091bx;
import G.C0126i;
import bH.C1007o;
import com.efiAnalytics.plugin.ecu.ControllerParameter;
import com.efiAnalytics.ui.C1685fp;
import com.efiAnalytics.ui.InterfaceC1565bc;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.FocusAdapter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import org.icepdf.core.util.PdfOps;
import r.C1798a;
import r.C1806i;
import s.C1818g;

/* renamed from: bt.bj, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/bj.class */
public class C1328bj extends C1348g implements G.aN, InterfaceC1282J, bY, InterfaceC1349h, InterfaceC1356o, InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    protected C0091bx f9020a;

    /* renamed from: h, reason: collision with root package name */
    private G.R f9021h;

    /* renamed from: b, reason: collision with root package name */
    G.aM f9022b;

    /* renamed from: i, reason: collision with root package name */
    private JPanel f9023i = new JPanel();

    /* renamed from: c, reason: collision with root package name */
    ButtonGroup f9024c = new ButtonGroup();

    /* renamed from: d, reason: collision with root package name */
    List f9025d = new ArrayList();

    /* renamed from: e, reason: collision with root package name */
    FocusAdapter f9026e = new C1329bk(this);

    /* renamed from: f, reason: collision with root package name */
    C1331bm f9027f = null;

    /* renamed from: g, reason: collision with root package name */
    boolean f9028g = false;

    public C1328bj(G.R r2, C0091bx c0091bx) {
        this.f9020a = null;
        this.f9021h = null;
        this.f9021h = r2;
        this.f9020a = c0091bx;
        super.b_(c0091bx.aH());
        if (c0091bx.l() == null || c0091bx.l().isEmpty()) {
            setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(0), ""));
        } else {
            setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(0), C1818g.b(c0091bx.l())));
        }
        if (c0091bx.k()) {
            this.f9023i.setLayout(new GridLayout(1, 1));
        } else {
            this.f9023i.setLayout(new GridLayout(0, 1));
        }
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, this.f9023i);
        this.f9022b = r2.c(this.f9020a.b());
        if (this.f9022b == null || !this.f9022b.i().equals(ControllerParameter.PARAM_CLASS_BITS)) {
            bH.C.a("Invalid Parameter type for Radio. Only Bit fields supported.");
        } else {
            JPanel jPanel = new JPanel();
            jPanel.setLayout(new GridLayout(1, 0, 2, 2));
            if (C1806i.a().a("lkjfgblkjgdoijre98u")) {
                jPanel.add(new C1291a(r2, c0091bx.b()));
            }
            jPanel.add(new C1353l(r2, c0091bx.b()));
            JPanel jPanel2 = new JPanel();
            jPanel2.setLayout(new BorderLayout());
            add("West", jPanel2);
            jPanel2.add("North", jPanel);
            C1330bl c1330bl = new C1330bl(this);
            Iterator it = this.f9022b.x().iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                if (!str.contains("INVALID")) {
                    String strSubstring = str;
                    if (strSubstring.startsWith(PdfOps.DOUBLE_QUOTE__TOKEN) && strSubstring.endsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
                        String strSubstring2 = strSubstring.substring(1);
                        strSubstring = strSubstring2.substring(0, strSubstring2.length() - 1);
                    }
                    JRadioButton jRadioButton = new JRadioButton(C1818g.b(strSubstring));
                    jRadioButton.setActionCommand(str);
                    jRadioButton.addActionListener(c1330bl);
                    jRadioButton.addFocusListener(this.f9026e);
                    jRadioButton.setOpaque(false);
                    this.f9024c.add(jRadioButton);
                    this.f9023i.add(jRadioButton);
                }
            }
            e();
        }
        int iA = C1798a.a().a(C1798a.f13352aH, C1798a.a().o());
        String family = getFont().getFamily();
        if (iA > 12) {
            setFont(new Font(family, 1, iA));
        } else {
            setFont(new Font(family, 0, iA));
        }
        try {
            if (this.f9022b != null) {
                C0126i.a(r2.c(), this.f9022b, this);
            }
        } catch (V.a e2) {
            Logger.getLogger(aT.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    public void e() {
        try {
            if (g().p().j() || !c(this.f9022b.f(g().p())) || this.f9024c.getButtonCount() <= 0) {
                if (g().p().j()) {
                    String strF = this.f9022b.f(g().p());
                    if (strF.equals("INVALID") || strF.equals("\"INVALID\"")) {
                        JRadioButton jRadioButton = new JRadioButton(strF);
                        jRadioButton.setActionCommand(strF);
                        this.f9024c.add(jRadioButton);
                        this.f9023i.add(jRadioButton);
                    }
                    c(strF);
                }
            } else if (isEnabled()) {
                b(f());
            }
        } catch (Exception e2) {
            bH.C.b(e2.getMessage());
        }
    }

    public void b(String str) {
        if (this.f9020a == null) {
            return;
        }
        G.aM aMVarC = g().c(this.f9020a.b());
        try {
            if (aMVarC.i().equals(ControllerParameter.PARAM_CLASS_BITS)) {
                aMVarC.a(g().p(), str);
            } else {
                bH.C.c("Didn't update it: " + ((Object) aMVarC));
            }
        } catch (V.g e2) {
            e2.printStackTrace();
            com.efiAnalytics.ui.bV.d("Failed to update " + this.f9020a.b() + "\n Error logged", this);
        }
    }

    public boolean c(String str) {
        Enumeration<AbstractButton> elements = this.f9024c.getElements();
        while (elements.hasMoreElements()) {
            AbstractButton abstractButtonNextElement2 = elements.nextElement2();
            if (abstractButtonNextElement2.getActionCommand() != null && abstractButtonNextElement2.getActionCommand().equals(str)) {
                abstractButtonNextElement2.setSelected(true);
                return true;
            }
        }
        return false;
    }

    public String f() {
        Enumeration<AbstractButton> elements = this.f9024c.getElements();
        while (elements.hasMoreElements()) {
            AbstractButton abstractButtonNextElement2 = elements.nextElement2();
            if (abstractButtonNextElement2.isSelected()) {
                return abstractButtonNextElement2.getActionCommand();
            }
        }
        return null;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        G.aR.a().a(this);
        this.f9020a = null;
    }

    public G.R g() {
        return this.f9021h;
    }

    @Override // G.aN
    public void a(String str, String str2) {
        e();
    }

    @Override // bt.InterfaceC1349h
    public void a() {
        if (this.f9020a == null || a_() == null || a_().isEmpty()) {
            return;
        }
        try {
            setEnabled(C1007o.a(a_(), g()));
        } catch (Exception e2) {
            bH.C.a(e2.getMessage());
        }
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        super.setEnabled(z2);
        C1685fp.a((Container) this, z2);
    }

    @Override // bt.bY
    public void b() {
        if (this.f9020a == null || this.f9020a.m() == null || this.f9020a.m().equals("")) {
            return;
        }
        boolean zA = true;
        try {
            zA = C1007o.a(this.f9020a.m(), g());
        } catch (V.g e2) {
            Logger.getLogger(aT.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        if (isVisible() && !zA) {
            setVisible(false);
            if (getParent() instanceof JPanel) {
                ((JPanel) getParent()).revalidate();
                return;
            }
            return;
        }
        if (isVisible() || !zA) {
            return;
        }
        setVisible(true);
        if (getParent() instanceof JPanel) {
            ((JPanel) getParent()).revalidate();
        }
    }

    @Override // bt.InterfaceC1282J
    public void a(InterfaceC1281I interfaceC1281I) {
        this.f9025d.add(interfaceC1281I);
    }

    @Override // bt.InterfaceC1282J
    public void b(InterfaceC1281I interfaceC1281I) {
        this.f9025d.remove(interfaceC1281I);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d(String str) {
        Iterator it = this.f9025d.iterator();
        while (it.hasNext()) {
            ((InterfaceC1281I) it.next()).b(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e(String str) {
        Iterator it = this.f9025d.iterator();
        while (it.hasNext()) {
            ((InterfaceC1281I) it.next()).a(str);
        }
    }

    @Override // bt.InterfaceC1282J
    public String d() {
        return this.f9020a.b();
    }

    @Override // bt.InterfaceC1356o
    public String c() {
        return this.f9022b.aJ();
    }

    @Override // bt.InterfaceC1356o
    public void a(boolean z2) {
        if (z2) {
            this.f9023i.setOpaque(true);
            this.f9023i.setBackground(Color.YELLOW);
            this.f9023i.setForeground(Color.BLACK);
            h();
            return;
        }
        if (this.f9020a.d()) {
            this.f9023i.setOpaque(true);
            this.f9023i.setBackground(Color.BLUE);
            this.f9023i.setForeground(Color.WHITE);
        } else if (this.f9020a.c()) {
            this.f9023i.setOpaque(true);
            this.f9023i.setBackground(Color.RED);
            this.f9023i.setForeground(Color.WHITE);
        } else {
            this.f9023i.setOpaque(false);
            this.f9023i.setForeground(UIManager.getColor("Label.foreground"));
            this.f9023i.setBackground(UIManager.getColor("Label.background"));
        }
        i();
    }

    public void h() {
        if (this.f9023i != null) {
            this.f9028g = true;
            this.f9027f = new C1331bm(this);
            this.f9027f.start();
        }
    }

    public void i() {
        this.f9028g = false;
        if (this.f9027f != null) {
            this.f9027f.a();
        }
    }
}
