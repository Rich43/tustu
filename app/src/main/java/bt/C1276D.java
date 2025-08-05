package bt;

import G.C0048ah;
import G.C0083bp;
import G.C0092by;
import G.C0113cs;
import bH.C1007o;
import com.efiAnalytics.apps.ts.dashboard.AbstractC1420s;
import com.efiAnalytics.apps.ts.dashboard.Gauge;
import com.efiAnalytics.apps.ts.dashboard.InterfaceC1390ac;
import com.efiAnalytics.apps.ts.dashboard.renderers.BasicReadoutGaugePainter;
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

/* renamed from: bt.D, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/D.class */
public class C1276D extends C1324bf implements bY, InterfaceC1349h, InterfaceC1390ac, InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    dD f8656a;

    /* renamed from: b, reason: collision with root package name */
    GridLayout f8657b;

    /* renamed from: c, reason: collision with root package name */
    List f8658c;

    /* renamed from: d, reason: collision with root package name */
    G.R f8659d;

    /* renamed from: e, reason: collision with root package name */
    C0092by f8660e;

    public C1276D(G.R r2, C0092by c0092by) {
        this.f8656a = new dD(this);
        this.f8657b = new GridLayout(0, 1, eJ.a(3), eJ.a(4));
        this.f8658c = new ArrayList();
        this.f8659d = r2;
        this.f8660e = c0092by;
        a(r2);
        setLayout(this.f8657b);
        setBorder(BorderFactory.createEmptyBorder(eJ.a(2), eJ.a(2), eJ.a(1), eJ.a(2)));
    }

    public C1276D(G.R r2, C0083bp c0083bp) {
        this.f8656a = new dD(this);
        this.f8657b = new GridLayout(0, 1, eJ.a(3), eJ.a(4));
        this.f8658c = new ArrayList();
        this.f8659d = r2;
        this.f8660e = new C0092by();
        this.f8660e.u(c0083bp.aH());
        this.f8660e.x(c0083bp.m());
        a(r2);
        setLayout(this.f8657b);
        setBorder(BorderFactory.createEmptyBorder(eJ.a(1), eJ.a(1), eJ.a(1), eJ.a(1)));
        a(1);
        G.aH aHVarG = r2.g(c0083bp.b());
        if (aHVarG == null) {
            bH.C.a("Invalid Channel Name!!!" + c0083bp.b());
        }
        a(c0083bp.b(), (int) (-Math.round(Math.log10(aHVarG.h()))));
    }

    public void a(int i2) {
        this.f8657b.setColumns(i2);
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

    public void a(C0048ah c0048ah) {
        boolean zA = true;
        try {
            zA = C1007o.a(c0048ah.aH(), b_());
        } catch (V.g e2) {
            Logger.getLogger(aT.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        if (zA) {
            Gauge gauge = new Gauge();
            gauge.setEcuConfigurationName(this.f8659d.c());
            gauge.setOutputChannel(c0048ah.i());
            try {
                gauge.setTitle(c0048ah.k().a());
            } catch (Exception e3) {
                bH.C.b("Unable to get Title for Gauge Def.. " + c0048ah.i());
                gauge.setTitle(c0048ah.i());
            }
            try {
                gauge.setUnits(c0048ah.j().a());
            } catch (V.g e4) {
                bH.C.b("Unable to get Units for Gauge Def.. " + ((Object) c0048ah.j()) + "\nErr: " + e4.getLocalizedMessage());
                gauge.setTitle(c0048ah.i());
            }
            gauge.setMin(c0048ah.b());
            gauge.setMax(c0048ah.e());
            gauge.setLowCritical(c0048ah.o());
            gauge.setLowWarning(c0048ah.f());
            gauge.setHighCritical(c0048ah.h());
            gauge.setHighWarning(c0048ah.g());
            gauge.setValueDigitsVP(c0048ah.m());
            gauge.setShortClickAction(c0048ah.r());
            gauge.setLongClickAction(c0048ah.s());
            gauge.setBackColor(Color.black);
            gauge.setFontColor(Color.white);
            gauge.setWarnColor(Color.YELLOW.darker());
            gauge.setCriticalColor(Color.RED.darker());
            gauge.setGaugePainter(new BasicReadoutGaugePainter());
            gauge.setBorderWidth(0);
            this.f8658c.add(gauge);
            try {
                gauge.subscribeToOutput();
            } catch (V.a e5) {
                com.efiAnalytics.ui.bV.d(e5.getMessage(), this);
            }
            add(gauge);
            gauge.setDirty(true);
            gauge.repaint();
        }
    }

    protected void a(String str, int i2) {
        boolean zA = true;
        try {
            zA = C1007o.a(this.f8660e.V(), b_());
        } catch (V.g e2) {
            Logger.getLogger(aT.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        if (zA) {
            Gauge gauge = new Gauge();
            gauge.setEcuConfigurationName(this.f8659d.c());
            gauge.setOutputChannel(str);
            gauge.setTitle("");
            gauge.setUnits("");
            gauge.setMin(-100000000);
            gauge.setMax(100000000);
            gauge.setLowCritical(-100000000);
            gauge.setLowWarning(-100000000);
            gauge.setHighCritical(100000000);
            gauge.setHighWarning(100000000);
            gauge.setValueDigits(Integer.valueOf(i2));
            gauge.setBackColor(Color.black);
            gauge.setFontColor(Color.white);
            gauge.setWarnColor(Color.YELLOW.darker());
            gauge.setCriticalColor(Color.RED.darker());
            gauge.setGaugePainter(new BasicReadoutGaugePainter());
            gauge.setBorderWidth(0);
            gauge.setFontSizeAdjustment(6);
            this.f8658c.add(gauge);
            try {
                gauge.subscribeToOutput();
            } catch (V.a e3) {
                com.efiAnalytics.ui.bV.d(e3.getMessage(), this);
            }
            add(gauge);
            gauge.setDirty(true);
            gauge.repaint();
        }
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.InterfaceC1390ac
    public void a(AbstractC1420s abstractC1420s) {
        this.f8656a.a();
    }

    @Override // bt.C1324bf, com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        Iterator it = this.f8658c.iterator();
        while (it.hasNext()) {
            C0113cs.a().a((Gauge) it.next());
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        if (this.f8658c.size() > 1) {
            return new Dimension(eJ.a(115) * this.f8657b.getColumns(), (eJ.a(38) * this.f8658c.size()) / this.f8657b.getColumns());
        }
        return new Dimension(eJ.a(125) * this.f8657b.getColumns(), (eJ.a(28) * this.f8658c.size()) / this.f8657b.getColumns());
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override // bt.C1324bf, bt.InterfaceC1349h
    public void a() {
        if (this.f8660e == null || this.f8660e.aH() == null || this.f8660e.aH().equals("")) {
            return;
        }
        boolean zA = true;
        try {
            zA = C1007o.a(this.f8660e.aH(), b_());
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
        if (this.f8660e == null || this.f8660e.V() == null || this.f8660e.V().equals("")) {
            return;
        }
        boolean zA = true;
        try {
            zA = C1007o.a(this.f8660e.V(), b_());
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
