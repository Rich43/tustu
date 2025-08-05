package com.efiAnalytics.tuningwidgets.panels;

import G.C0083bp;
import G.C0088bu;
import G.bL;
import bt.C1287O;
import bt.C1290R;
import bt.C1303al;
import bt.C1320bb;
import bt.C1324bf;
import bt.C1346e;
import com.efiAnalytics.apps.ts.dashboard.InterfaceC1421t;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.InterfaceC1662et;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.eJ;
import i.C1743c;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.LayerUI;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/SelectableTablePanel.class */
public class SelectableTablePanel extends JComponent implements InterfaceC1421t, InterfaceC1565bc {

    /* renamed from: d, reason: collision with root package name */
    G.R f10299d;

    /* renamed from: f, reason: collision with root package name */
    private JButton f10300f;

    /* renamed from: a, reason: collision with root package name */
    JPanel f10296a = new JPanel();

    /* renamed from: b, reason: collision with root package name */
    JLabel f10297b = new JLabel();

    /* renamed from: c, reason: collision with root package name */
    C1324bf f10298c = null;

    /* renamed from: e, reason: collision with root package name */
    ArrayList f10301e = new ArrayList();

    /* renamed from: g, reason: collision with root package name */
    private boolean f10302g = true;

    /* renamed from: h, reason: collision with root package name */
    private int f10303h = 3;

    /* renamed from: i, reason: collision with root package name */
    private InterfaceC1662et f10304i = null;

    /* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/SelectableTablePanel$BlurLayerUI.class */
    class BlurLayerUI extends LayerUI {

        /* renamed from: a, reason: collision with root package name */
        private BufferedImage f10305a;

        /* renamed from: b, reason: collision with root package name */
        private BufferedImageOp f10306b;

        @Override // javax.swing.plaf.LayerUI, javax.swing.plaf.ComponentUI
        public void paint(Graphics graphics, JComponent jComponent) {
            int width = jComponent.getWidth();
            int height = jComponent.getHeight();
            if (width == 0 || height == 0) {
                return;
            }
            if (this.f10305a == null || this.f10305a.getWidth() != width || this.f10305a.getHeight() != height) {
                this.f10305a = new BufferedImage(width, height, 1);
            }
            Graphics2D graphics2DCreateGraphics = this.f10305a.createGraphics();
            graphics2DCreateGraphics.setClip(graphics.getClip());
            super.paint(graphics2DCreateGraphics, jComponent);
            graphics2DCreateGraphics.dispose();
            ((Graphics2D) graphics).drawImage(this.f10305a, this.f10306b, 0, 0);
        }
    }

    public SelectableTablePanel(G.R r2) {
        this.f10299d = null;
        this.f10300f = null;
        this.f10299d = r2;
        setLayout(new BorderLayout());
        this.f10296a.setLayout(new BorderLayout());
        this.f10296a.add(BorderLayout.CENTER, this.f10297b);
        this.f10300f = new JButton(C1818g.b("Select a table"));
        this.f10300f.setPreferredSize(eJ.a(150, 16));
        this.f10300f.setToolTipText(C1818g.b("Click to select Table"));
        this.f10300f.addActionListener(new aw(this));
        this.f10300f.setFocusable(false);
        this.f10296a.add("East", this.f10300f);
        add("North", this.f10296a);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(int i2, int i3) {
        C1287O c1287o = new C1287O(b(), this.f10303h);
        c1287o.a(new ax(this));
        this.f10296a.add(c1287o);
        c1287o.show(this, i2, i3);
    }

    private G.R b() {
        String strD = d();
        if (strD == null || strD.isEmpty()) {
            strD = G.T.a().c().c();
        }
        return G.T.a().c(strD);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        int iA = eJ.a(450);
        Dimension preferredSize = super.getPreferredSize();
        if (preferredSize.height > iA) {
            preferredSize.height = iA;
        }
        return preferredSize;
    }

    public void a(String str) {
        G.R rB = b();
        C0088bu c0088buC = rB.e().c(str);
        c();
        if (c0088buC != null) {
            if (!W.a(rB, c0088buC, bV.c())) {
                c0088buC = new C0088bu();
                C0083bp c0083bp = new C0083bp();
                c0083bp.e(C1818g.b("Password Protected Information"));
                c0088buC.a(c0083bp);
                C0083bp c0083bp2 = new C0083bp();
                c0083bp2.e(C1818g.b("You must have the password to table."));
                c0088buC.a(c0083bp2);
            }
            this.f10298c = new C1324bf(rB, c0088buC, getName(), this.f10304i);
            add(BorderLayout.CENTER, this.f10298c);
            C1346e.a().a(rB.c(), this.f10298c);
            b(str);
            if (this.f10302g) {
                if (c0088buC.S()) {
                    for (C1320bb c1320bb : a(this.f10298c)) {
                        Q q2 = new Q(rB, c1320bb.g(), c1320bb);
                        c1320bb.a(q2);
                        if (C1743c.a().e() != null) {
                            q2.a(C1743c.a().f());
                        }
                    }
                }
                if (c0088buC.T()) {
                    for (C1303al c1303al : b(this.f10298c)) {
                        O o2 = new O(rB, c1303al.l(), c1303al);
                        c1303al.a(o2);
                        c1303al.i().j(false);
                        if (C1743c.a().e() != null) {
                            o2.a(C1743c.a().f());
                        }
                        C1290R c1290rJ = c1303al.j();
                        if (c1290rJ != null) {
                            P p2 = new P(rB, c1290rJ.g(), c1290rJ);
                            c1303al.b(p2);
                            if (C1743c.a().e() != null) {
                                p2.a(C1743c.a().f());
                            }
                        }
                    }
                }
            }
        } else {
            b((String) null);
        }
        String strC = bL.c(rB, str);
        if (strC == null || strC.isEmpty()) {
            strC = C1818g.b("Select Table");
        }
        a().setText(strC);
    }

    private List a(JPanel jPanel) {
        return a(jPanel, new ArrayList());
    }

    private List a(JPanel jPanel, List list) {
        if (jPanel instanceof C1320bb) {
            list.add((C1320bb) jPanel);
        } else {
            for (Component component : jPanel.getComponents()) {
                if (component instanceof JPanel) {
                    a((JPanel) component, list);
                }
            }
        }
        return list;
    }

    private List b(JPanel jPanel) {
        return b(jPanel, new ArrayList());
    }

    private List b(JPanel jPanel, List list) {
        if (jPanel instanceof C1303al) {
            list.add((C1303al) jPanel);
        } else {
            for (Component component : jPanel.getComponents()) {
                if (component instanceof JPanel) {
                    b((JPanel) component, list);
                }
            }
        }
        return list;
    }

    public void a(InterfaceC1662et interfaceC1662et) {
        this.f10304i = interfaceC1662et;
    }

    private void c() {
        if (this.f10298c != null) {
            if (b() != null) {
                C1346e.a().b(b().c(), this.f10298c);
            }
            this.f10298c.close();
            remove(this.f10298c);
        }
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        c();
    }

    private String d() {
        return this.f10299d.c();
    }

    public void a(ay ayVar) {
        this.f10301e.add(ayVar);
    }

    private void b(String str) {
        Iterator it = this.f10301e.iterator();
        while (it.hasNext()) {
            ((ay) it.next()).panelSelectionChanged(this.f10299d.c(), str);
        }
    }

    public void a(boolean z2) {
        this.f10302g = z2;
    }

    public void a(int i2) {
        this.f10303h = i2;
    }

    public void b(boolean z2) {
        a().setVisible(z2);
    }

    public JButton a() {
        return this.f10300f;
    }
}
