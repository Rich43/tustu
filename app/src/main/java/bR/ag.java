package br;

import G.C0072be;
import G.C0088bu;
import G.C0126i;
import G.aH;
import G.aI;
import G.dc;
import G.dd;
import bt.C1324bf;
import bt.bO;
import com.efiAnalytics.ui.C1562b;
import com.efiAnalytics.ui.C1589c;
import com.efiAnalytics.ui.C1685fp;
import com.efiAnalytics.ui.C1701s;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.dQ;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:br/ag.class */
public class ag extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    C1250n f8425a;

    /* renamed from: c, reason: collision with root package name */
    C1324bf f8427c;

    /* renamed from: d, reason: collision with root package name */
    dQ f8428d;

    /* renamed from: e, reason: collision with root package name */
    dc f8429e;

    /* renamed from: f, reason: collision with root package name */
    dd f8430f;

    /* renamed from: g, reason: collision with root package name */
    G.R f8431g;

    /* renamed from: h, reason: collision with root package name */
    String f8432h;

    /* renamed from: b, reason: collision with root package name */
    C1589c f8426b = null;

    /* renamed from: i, reason: collision with root package name */
    JLabel f8433i = new JLabel();

    /* renamed from: j, reason: collision with root package name */
    JComboBox f8434j = new JComboBox();

    /* renamed from: k, reason: collision with root package name */
    JComboBox f8435k = new JComboBox();

    /* renamed from: l, reason: collision with root package name */
    JComboBox f8436l = new JComboBox();

    /* renamed from: m, reason: collision with root package name */
    JCheckBox f8437m = new JCheckBox(C1818g.b("Deactivate"));

    public ag(G.R r2, dc dcVar, String str, C1589c c1589c) throws IllegalArgumentException {
        this.f8425a = null;
        this.f8427c = null;
        this.f8428d = null;
        this.f8431g = r2;
        this.f8429e = dcVar;
        this.f8432h = str;
        this.f8430f = dcVar.a(str);
        this.f8428d = new dQ(aE.a.A(), "TrimTablePanel_" + str);
        this.f8427c = new C1324bf();
        setLayout(new BorderLayout());
        add(this.f8427c, BorderLayout.CENTER);
        this.f8425a = C1254r.a().a(r2, dcVar, str, c1589c);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(0));
        add(jPanel, "North");
        jPanel.add(this.f8433i);
        Iterator itL = dcVar.l();
        while (itL.hasNext()) {
            this.f8434j.addItem(itL.next());
        }
        Iterator itM = dcVar.m();
        while (itM.hasNext()) {
            this.f8435k.addItem(itM.next());
        }
        Iterator itU = dcVar.u();
        while (itU.hasNext()) {
            this.f8436l.addItem(itU.next());
        }
        if (this.f8430f.f() == null || this.f8430f.f().trim().isEmpty()) {
            jPanel.add(new JLabel(C1818g.b("AFR:")));
            jPanel.add(this.f8434j);
            jPanel.add(new JLabel(C1818g.b("EGO Corr:")));
            jPanel.add(this.f8435k);
        } else {
            jPanel.add(new JLabel("       "));
            jPanel.add(new JLabel(C1818g.b("EGO Sensor:")));
            jPanel.add(this.f8436l);
            jPanel.add(new JLabel("       "));
        }
        jPanel.add(this.f8437m);
        this.f8437m.setSelected(Boolean.parseBoolean(this.f8428d.b("deactivated", "false")));
        this.f8437m.addActionListener(new ah(this));
        try {
            String strA = this.f8428d.a("egoCorChannel");
            if (strA != null) {
                this.f8435k.setSelectedItem(strA);
            } else {
                bH.C.c("Invalid EGO Correction Channle, using first");
                this.f8435k.setSelectedIndex(0);
            }
        } catch (Exception e2) {
            bV.d("Invalid Lambda Sensor Index, setting first", this.f8427c);
        }
        try {
            String strA2 = this.f8428d.a("lambdaChannel");
            if (strA2 != null) {
                this.f8434j.setSelectedItem(strA2);
            } else {
                bH.C.c("Invalid Lambda Channel, using first");
                this.f8434j.setSelectedIndex(0);
            }
        } catch (Exception e3) {
            bV.d("Invalid Lambda Sensor Index, setting first", this.f8427c);
        }
        this.f8436l.addActionListener(new ai(this));
        this.f8434j.addActionListener(new aj(this));
        this.f8435k.addActionListener(new ak(this));
        a(this.f8430f);
    }

    private void a(dd ddVar) throws IllegalArgumentException {
        int iRound;
        C0072be c0072beClone = ((C0072be) this.f8431g.e().c(ddVar.b())).clone();
        c0072beClone.a(true);
        this.f8433i.setText(C1818g.b(ddVar.e()));
        c0072beClone.h("veAnalyze_");
        c0072beClone.u(ddVar.a());
        try {
            bO.a().a(this.f8431g, c0072beClone.aJ(), c0072beClone.l(), c0072beClone.aJ()).c(1);
        } catch (V.g e2) {
            bH.C.a("Unable to get Table Model for " + c0072beClone.aJ() + " with prefix:" + c0072beClone.l());
            e2.printStackTrace();
        }
        this.f8427c.a(this.f8431g, (C0088bu) c0072beClone);
        this.f8427c.b_(ddVar.a());
        if (ddVar.f() == null || ddVar.f().trim().isEmpty()) {
            this.f8434j.setSelectedItem(this.f8428d.b("lambdaChannel", ddVar.c()));
            this.f8435k.setSelectedItem(this.f8428d.b("egoCorChannel", ddVar.d()));
            return;
        }
        try {
            iRound = (int) Math.round(C0126i.a(ddVar.f(), (aI) this.f8431g));
        } catch (ax.U e3) {
            Logger.getLogger(ag.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            iRound = 0;
        }
        try {
            this.f8436l.setSelectedIndex(Integer.parseInt(this.f8428d.b("egoSensorIndex", iRound + "")));
        } catch (NumberFormatException e4) {
            bV.d(ddVar.e() + ": Error retrieving Sensor Index", this);
            this.f8436l.setSelectedIndex(0);
        }
    }

    public void a() {
        try {
            C1562b[][] c1562bArrD = bO.a().a(this.f8431g, this.f8432h, "veAnalyze_", this.f8432h).D();
            C1701s c1701sA = bO.a().a(this.f8431g, this.f8432h, "", this.f8432h);
            for (int i2 = 0; i2 < c1701sA.getRowCount(); i2++) {
                for (int i3 = 0; i3 < c1701sA.getColumnCount(); i3++) {
                    c1701sA.setValueAt(c1562bArrD[(c1562bArrD.length - i2) - 1][i3].i(), i2, i3);
                }
            }
        } catch (V.g e2) {
            bH.C.a("Unable to get Table Model for " + this.f8432h + " with prefix:veAnalyze_");
            e2.printStackTrace();
        }
    }

    public void b() {
        if (!this.f8437m.isSelected()) {
            this.f8427c.a();
            if (this.f8427c.isEnabled()) {
                C1685fp.a((Component) this.f8427c, true);
                return;
            }
            return;
        }
        C1685fp.a((Component) this.f8427c, false);
        if (this.f8425a.b()) {
            try {
                a(false);
            } catch (V.a e2) {
                Logger.getLogger(ag.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x001d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean c() {
        /*
            r5 = this;
            r0 = 1
            r6 = r0
            r0 = r5
            G.dd r0 = r0.f8430f     // Catch: V.g -> L26
            java.lang.String r0 = r0.a()     // Catch: V.g -> L26
            if (r0 == 0) goto L1d
            r0 = r5
            G.dd r0 = r0.f8430f     // Catch: V.g -> L26
            java.lang.String r0 = r0.a()     // Catch: V.g -> L26
            r1 = r5
            G.R r1 = r1.f8431g     // Catch: V.g -> L26
            boolean r0 = bH.C1007o.a(r0, r1)     // Catch: V.g -> L26
            if (r0 == 0) goto L21
        L1d:
            r0 = 1
            goto L22
        L21:
            r0 = 0
        L22:
            r6 = r0
            goto L37
        L26:
            r7 = move-exception
            java.lang.Class<br.ag> r0 = br.ag.class
            java.lang.String r0 = r0.getName()
            java.util.logging.Logger r0 = java.util.logging.Logger.getLogger(r0)
            java.util.logging.Level r1 = java.util.logging.Level.SEVERE
            r2 = 0
            r3 = r7
            r0.log(r1, r2, r3)
        L37:
            r0 = r5
            javax.swing.JCheckBox r0 = r0.f8437m
            boolean r0 = r0.isSelected()
            if (r0 != 0) goto L49
            r0 = r6
            if (r0 == 0) goto L49
            r0 = 1
            goto L4a
        L49:
            r0 = 0
        L4a:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: br.ag.c():boolean");
    }

    public boolean d() {
        try {
            C1701s c1701sB = bO.a().b(this.f8431g, this.f8432h, "");
            C1701s c1701sB2 = bO.a().b(this.f8431g, this.f8432h, "veAnalyze_");
            if (c1701sB != null && c1701sB2 != null) {
                C1562b[][] c1562bArrD = c1701sB2.D();
                if (c1562bArrD == null) {
                    return false;
                }
                for (int i2 = 0; i2 < c1701sB.getRowCount(); i2++) {
                    for (int i3 = 0; i3 < c1701sB.getColumnCount(); i3++) {
                        if (Math.abs(c1701sB.getValueAt(i2, i3).doubleValue() - c1562bArrD[(c1562bArrD.length - i2) - 1][i3].i().doubleValue()) > 1.0E-6d) {
                            return true;
                        }
                    }
                }
            }
            return false;
        } catch (Exception e2) {
            bH.C.a("Unable to get Table Model for " + this.f8432h + " with prefix:veAnalyze_");
            e2.printStackTrace();
            return false;
        }
    }

    protected void a(boolean z2) {
        boolean z3 = true;
        try {
            z3 = C0126i.a(this.f8430f.a(), (aI) this.f8431g) != 0.0d;
        } catch (ax.U e2) {
        }
        if (z3 && z2 && !this.f8437m.isSelected()) {
            this.f8425a.c();
        } else {
            this.f8425a.e();
        }
    }

    public void e() {
        this.f8425a.close();
    }

    public void f() {
        this.f8425a.a();
    }

    public void a(aH aHVar) {
        this.f8425a.a(aHVar);
    }
}
