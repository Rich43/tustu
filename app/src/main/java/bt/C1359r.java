package bt;

import G.C0051ak;
import G.C0086bs;
import G.C0113cs;
import bH.C1007o;
import com.efiAnalytics.apps.ts.dashboard.AbstractC1420s;
import com.efiAnalytics.apps.ts.dashboard.Indicator;
import com.efiAnalytics.apps.ts.dashboard.InterfaceC1390ac;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.dD;
import com.efiAnalytics.ui.eJ;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/* renamed from: bt.r, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/r.class */
public class C1359r extends C1324bf implements bY, InterfaceC1349h, InterfaceC1390ac, InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    dD f9112a = new dD(this);

    /* renamed from: b, reason: collision with root package name */
    GridLayout f9113b = new GridLayout(0, 1, eJ.a(3), eJ.a(4));

    /* renamed from: c, reason: collision with root package name */
    List f9114c = new ArrayList();

    /* renamed from: d, reason: collision with root package name */
    G.R f9115d;

    /* renamed from: e, reason: collision with root package name */
    C0086bs f9116e;

    public C1359r(G.R r2, C0086bs c0086bs) {
        this.f9115d = r2;
        this.f9116e = c0086bs;
        a(r2);
        setLayout(this.f9113b);
        setBorder(BorderFactory.createEmptyBorder(eJ.a(2), eJ.a(2), eJ.a(1), eJ.a(2)));
    }

    public void a(int i2) {
        this.f9113b.setColumns(i2);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        super.paint(graphics);
        if (isEnabled()) {
            return;
        }
        graphics.setColor(new Color(64, 64, 64, 100));
        graphics.fillRect(0, 0, getWidth(), getHeight());
    }

    public void a(C0051ak c0051ak) {
        boolean zA = true;
        try {
            zA = C1007o.a(c0051ak.aH(), b_());
        } catch (V.g e2) {
            Logger.getLogger(aT.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        if (zA) {
            Indicator indicator = new Indicator();
            indicator.setEcuConfigurationName(this.f9115d.c());
            indicator.setOutputChannel(c0051ak.f());
            indicator.setOnText(c0051ak.a().toString());
            indicator.setOnTextColor(new Color(c0051ak.i().a()));
            indicator.setOffTextColor(new Color(c0051ak.j().a()));
            indicator.setOnBackgroundColor(new Color(c0051ak.g().a()));
            indicator.setOffBackgroundColor(new Color(c0051ak.h().a()));
            indicator.setOffText(c0051ak.d().toString());
            indicator.setShortClickAction(c0051ak.l());
            indicator.setLongClickAction(c0051ak.m());
            this.f9114c.add(indicator);
            try {
                indicator.subscribeToOutput();
            } catch (V.a e3) {
                com.efiAnalytics.ui.bV.d(e3.getMessage(), this);
            }
            add(indicator);
            indicator.setDirty(true);
            indicator.repaint();
        }
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.InterfaceC1390ac
    public void a(AbstractC1420s abstractC1420s) {
        this.f9112a.a();
    }

    @Override // bt.C1324bf, com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        Iterator it = this.f9114c.iterator();
        while (it.hasNext()) {
            C0113cs.a().a((Indicator) it.next());
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        return new Dimension(3 + (eJ.a(116) * this.f9113b.getColumns()), 4 + ((eJ.a(29) * this.f9114c.size()) / this.f9113b.getColumns()));
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override // bt.C1324bf, bt.InterfaceC1349h
    public void a() {
        if (this.f9116e == null || this.f9116e.aH() == null || this.f9116e.aH().equals("")) {
            return;
        }
        boolean zA = true;
        try {
            zA = C1007o.a(this.f9116e.aH(), b_());
        } catch (V.g e2) {
            Logger.getLogger(aT.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        if (isEnabled() && !zA) {
            setEnabled(false);
            if (getParent() instanceof JPanel) {
                ((JPanel) getParent()).revalidate();
                return;
            }
            return;
        }
        if (isEnabled() || !zA) {
            return;
        }
        setEnabled(true);
        if (getParent() instanceof JPanel) {
            ((JPanel) getParent()).revalidate();
        }
    }

    @Override // bt.C1324bf, bt.bY
    public void b() {
        if (this.f9116e == null || this.f9116e.V() == null || this.f9116e.V().equals("")) {
            return;
        }
        boolean zA = true;
        try {
            zA = C1007o.a(this.f9116e.V(), b_());
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
}
